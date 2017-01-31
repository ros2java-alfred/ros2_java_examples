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

import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Publisher;
import org.ros2.rcljava.qos.QoSProfile;
import org.ros2.rcljava.RCLJava;


public class TalkerBestEffort {
  public static void main(String[] args) throws InterruptedException {
    // Initialize RCL
    RCLJava.rclJavaInit();

    // Let's create a Node
    Node node = RCLJava.createNode("talker");

    // Publishers are type safe, make sure to pass the message type
    Publisher<std_msgs.msg.String> chatterPublisher =
        node.<std_msgs.msg.String>createPublisher(
        std_msgs.msg.String.class, "chatter",
        QoSProfile.SENSOR_DATA);

    std_msgs.msg.String msg = new std_msgs.msg.String();

    int i = 1;

    while (RCLJava.ok()) {
      msg.setData("Hello World: " + i);
      i++;
      System.out.println("Publishing: \"" + msg.getData() + "\"");
      chatterPublisher.publish(msg);

      // Sleep a little bit between each message
      Thread.sleep(1000);
    }
  }
}
