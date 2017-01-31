/* Copyright 2016 Esteve Fernandez <esteve@apache.org>
 * Copyright 2016-2017 Mickael Gaillard <mick.gaillard@gmail.com>
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
package org.ros2.rcljava.examples.topics;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Publisher;

public class Talker {
    private static final String NODE_NAME = Talker.class.getSimpleName().toLowerCase();

    public static void main(String[] args) throws InterruptedException {

        int i = 1;

        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a Node
        Node node = RCLJava.createNode(NODE_NAME);

        std_msgs.msg.String msg = new std_msgs.msg.String();
        Publisher<std_msgs.msg.String> chatter_pub =
                node.<std_msgs.msg.String>createPublisher(
                    std_msgs.msg.String.class,
                    "chatter");

        while(RCLJava.ok()) {
            msg.setData("Hello World: " + i);

            System.out.println("Publishing: \"" + msg.getData() + "\"");
            chatter_pub.publish(msg);

            // Sleep a little bit between each message
            Thread.sleep(500);
            i++;
        }

        chatter_pub.dispose();
        node.dispose();
        RCLJava.shutdown();
    }
}
