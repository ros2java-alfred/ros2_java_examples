/* Copyright 2017 Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ros2.rcljava.examples.executors;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.executor.SingleThreadedExecutor;
import org.ros2.rcljava.executor.ThreadedExecutor;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.NativePublisher;
import org.ros2.rcljava.node.topic.NativeSubscription;
import org.ros2.rcljava.node.topic.SubscriptionCallback;

import std_msgs.msg.UInt32;

public class SingleThreadNodes {

    public static class TestConsumer implements SubscriptionCallback<UInt32> {
        private String name;

        public TestConsumer(String name) {
            this.name = name;
        }

        public final void dispatch(final UInt32 msg) {
            System.out.println(this.name + " : " + Thread.currentThread().getId() + " :  " + msg.getData());

            // For test if thread-safe
//            throw new NullPointerException();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit(args);

        // Create executor
        ThreadedExecutor executor = new SingleThreadedExecutor();
        executor.spin();

        // Let's create a new Node
        final Node publisherNode = RCLJava.createNode("publisher_node");
        final Node subscriptionNodeOne = RCLJava.createNode("subscription_node_one");
        final Node subscriptionNodeTwo = RCLJava.createNode("subscription_node_two");

        // Add to executor.
        executor.addNode(publisherNode);
        executor.addNode(subscriptionNodeOne);
        executor.addNode(subscriptionNodeTwo);

        //
        NativePublisher<UInt32> publisher = (NativePublisher<UInt32>) publisherNode.<UInt32>createPublisher(UInt32.class, "chatter");
        NativeSubscription<UInt32> subscriptionOne = (NativeSubscription<UInt32>) subscriptionNodeOne.<UInt32>createSubscription(UInt32.class, "chatter", new TestConsumer("1"));
        NativeSubscription<UInt32> subscriptionTwo = (NativeSubscription<UInt32>) subscriptionNodeTwo.<UInt32>createSubscription(UInt32.class, "chatter", new TestConsumer("2"));

        int i = 0;
        UInt32 msg = new UInt32();

        final long then = System.nanoTime();

        while (RCLJava.ok()) {
            msg.setData(i++);
            publisher.publish(msg);
            Thread.sleep(1);

            if (i == 5000) {
                break;
            }
        }

        final long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - then);
        System.out.println("Slept for (ms): " + millis); // = something around 1000.


        executor.removeNode(subscriptionNodeTwo);
        executor.removeNode(subscriptionNodeOne);
        executor.removeNode(publisherNode);
        executor.cancel();

        publisher.dispose();
        subscriptionOne.dispose();
        subscriptionTwo.dispose();

        publisherNode.dispose();
        subscriptionNodeOne.dispose();
        subscriptionNodeTwo.dispose();

        RCLJava.shutdown();
    }
}
