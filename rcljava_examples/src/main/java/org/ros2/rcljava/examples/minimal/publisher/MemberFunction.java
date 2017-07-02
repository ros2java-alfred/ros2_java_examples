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
package org.ros2.rcljava.examples.minimal.publisher;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.topic.Publisher;
import org.ros2.rcljava.time.WallTimer;
import org.ros2.rcljava.time.WallTimerCallback;

public class MemberFunction {

    public static class MinimalPublisher extends NativeNode implements WallTimerCallback {
        int i = 0;
        private Publisher<std_msgs.msg.String> pub;
        private WallTimer timer;

        public MinimalPublisher() {
            super("minimal_subscriber");

            this.pub = this.<std_msgs.msg.String>createPublisher(
                            std_msgs.msg.String.class,
                            "topic");

            this.timer = this.createWallTimer(500, TimeUnit.MILLISECONDS, this);
            this.timer.getHandle(); // Disable Warning
        }

        @Override
        public void tick() {
            std_msgs.msg.String msg = new std_msgs.msg.String();
            msg.setData("Hello World: " + ++i);

            System.out.println("Publishing: \"" + msg.getData() + "\"");
            this.pub.publish(msg);
        }

        @Override
        public void dispose() {
            this.timer.dispose();
            this.pub.dispose();
            super.dispose();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit(args);

        RCLJava.spin(new MinimalPublisher());

        RCLJava.shutdown();
    }
}
