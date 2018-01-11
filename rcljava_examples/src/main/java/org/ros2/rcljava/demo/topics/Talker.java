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
package org.ros2.rcljava.demo.topics;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.Publisher;
import org.ros2.rcljava.qos.QoSProfile;
import org.ros2.rcljava.time.WallTimer;
import org.ros2.rcljava.time.WallTimerCallback;

public class Talker extends NativeNode {

    private static final String NODE_NAME = Talker.class.getSimpleName().toLowerCase();

    private int i = 1;
    private std_msgs.msg.String msg;
    private Publisher<std_msgs.msg.String> pub;
    private WallTimer timer;

    public Talker(String topic) {
        super(NODE_NAME);

        this.msg = new std_msgs.msg.String();

        WallTimerCallback publish_message = new WallTimerCallback() {

            @Override
            public void tick() {
                msg.setData("Hello World: " + i++);

                System.out.println("Publishing: \"" + msg.getData() + "\"");
                pub.publish(msg);
            }
        };

        QoSProfile qos = QoSProfile.DEFAULT;
        //TODO qos.setDepth(7);

        this.pub = this.<std_msgs.msg.String>createPublisher(
                    std_msgs.msg.String.class,
                    topic,
                    qos);

        this.timer = this.createWallTimer(1, TimeUnit.SECONDS, publish_message);
    }

    @Override
    public void dispose() {
        this.timer.dispose();
        this.pub.dispose();
        super.dispose();
    }

    public static void main(String[] args) throws InterruptedException {

        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a Node
        Node node = new Talker("chatter");

        RCLJava.spin(node);

        node.dispose();
        RCLJava.shutdown();
    }
}
