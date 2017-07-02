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
package org.ros2.rcljava.demo.timers;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.time.WallTimer;
import org.ros2.rcljava.time.WallTimerCallback;

public class ReuseTimer {

    public static class OneOffTimerNode extends NativeNode {
        private int count = 0;
        private WallTimer periodicTimer;
        private WallTimer oneOffTimer;

        public OneOffTimerNode() {
            super("reuse_timer");

            this.oneOffTimer = this.createWallTimer(2, TimeUnit.SECONDS, new WallTimerCallback() {

                @Override
                public void tick() {
                    System.out.println("in one_off_timer callback");
                    OneOffTimerNode.this.oneOffTimer.cancel();
                }


            });
            // cancel immediately to prevent it running the first time.
            this.oneOffTimer.cancel();

            this.periodicTimer = this.createWallTimer(2, TimeUnit.SECONDS, new WallTimerCallback() {
                @Override
                public void tick() {
                    System.out.println("in periodic_timer callback");

                    if (OneOffTimerNode.this.count++ % 3 == 0) {
                        System.out.println("  resetting one off timer");
                        OneOffTimerNode.this.oneOffTimer.reset();
                    } else {
                        System.out.println("  not resetting one off timer");
                    }
                }
            });
        }

        @Override
        public void dispose() {
            this.periodicTimer.dispose();
            this.oneOffTimer.dispose();
            super.dispose();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit(args);

        RCLJava.spin(new OneOffTimerNode());

        RCLJava.shutdown();
    }
}
