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
package org.ros2.rcljava.examples.minimal.subscriber;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.topic.Subscription;
import org.ros2.rcljava.node.topic.SubscriptionCallback;

public class MemberFunction {

    public static class MinimalSubscriber extends NativeNode {
        private Subscription<std_msgs.msg.String> sub;

        public MinimalSubscriber() {
            super("minimal_subscriber");

            // Subscriptions are type safe, so we'll pass the message type. We use the fully
            // qualified
            // class name to avoid any collision with Java's String class
            this.sub = this.<std_msgs.msg.String>createSubscription(std_msgs.msg.String.class, "topic",
                    new SubscriptionCallback<std_msgs.msg.String>() {
                        // We define the callback inline, this works with Java 8's lambdas too, but we
                        // use
                        // our own Consumer interface because Android supports lambdas via retrolambda,
                        // but not
                        // the lambda API
                        @Override
                        public void dispatch(std_msgs.msg.String msg) {
                            MinimalSubscriber.chatterCallback(msg);
                        }
                    });
        }

        public static void chatterCallback(final std_msgs.msg.String msg) {
            System.out.println("I heard: [" + msg.getData() + "]");
        }

        @Override
        public void dispose() {
            this.sub.dispose();
            super.dispose();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit(args);

        RCLJava.spin(new MinimalSubscriber());

        RCLJava.shutdown();
    }
}
