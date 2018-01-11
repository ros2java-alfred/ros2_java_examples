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

import org.ros2.rcljava.qos.QoSProfile;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.topic.SubscriptionCallback;
import org.ros2.rcljava.node.topic.Subscription;

public class ImuListener extends NativeNode {

    private Subscription<sensor_msgs.msg.Imu> sub;

    public ImuListener() {
        super("imu_listener");

        SubscriptionCallback<sensor_msgs.msg.Imu> imu_cb = new SubscriptionCallback<sensor_msgs.msg.Imu>() {
            // We define the callback inline, this works with Java 8's lambdas too, but we use
            // our own Consumer interface because Android supports lambdas via retrolambda, but not
            // the lambda API
            @Override
            public void dispatch(sensor_msgs.msg.Imu msg) {
                System.out.println(String.format(" accel: [%+6.3f %+6.3f %+6.3f]\n",
                        msg.getLinearAcceleration().getX(),
                        msg.getLinearAcceleration().getY(),
                        msg.getLinearAcceleration().getZ()));
            }
        };

        // Subscriptions are type safe, so we'll pass the message type.
        this.sub = this.<sensor_msgs.msg.Imu>createSubscription(
                sensor_msgs.msg.Imu.class,
                "imu",
                imu_cb,
                QoSProfile.SENSOR_DATA);
    }

    @Override
    public void dispose() {
        this.sub.dispose();
        super.dispose();
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a new Node
        Node node = new ImuListener();

        RCLJava.spin(node);

        node.dispose();
        RCLJava.shutdown();
    }

}
