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
package org.ros2.rcljava.examples.minimal.composition;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.executor.SingleThreadedExecutor;
import org.ros2.rcljava.node.Node;

public class Composed {

    public static void main(String[] args) throws InterruptedException {
        RCLJava.rclJavaInit(args);

        SingleThreadedExecutor executor = new SingleThreadedExecutor();

        Node pubNode = new PublisherNode();
        Node subNode = new SubscriberNode();

        executor.addNode(pubNode);
        executor.addNode(subNode);

        executor.spin();

        boolean isRuning = true;
        while(isRuning) {
            Thread.sleep(60000);
        }

        pubNode.dispose();
        subNode.dispose();
        RCLJava.shutdown();
    }
}
