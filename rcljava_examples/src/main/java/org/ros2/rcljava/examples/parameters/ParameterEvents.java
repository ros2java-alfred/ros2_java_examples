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
package org.ros2.rcljava.examples.parameters;

import java.util.Arrays;

import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.topic.Subscription;
import org.ros2.rcljava.node.parameter.ParameterEventCallback;
import org.ros2.rcljava.node.parameter.ParameterVariant;
import org.ros2.rcljava.node.parameter.SyncParametersClient;

import rcl_interfaces.msg.ParameterEvent;

public class ParameterEvents {
    private static final String NODE_NAME = ParameterEvents.class.getSimpleName().toLowerCase();

    private static void onParameterEvent(final rcl_interfaces.msg.ParameterEvent event)
    {
        System.out.println("Parameter event:\n new parameters:");
        for (rcl_interfaces.msg.Parameter new_parameter : event.getNewParameters()) {
            System.out.println(String.format("  %s", new_parameter.getName()));
        }
        System.out.println(" changed parameters:");
        for (rcl_interfaces.msg.Parameter changed_parameter : event.getChangedParameters()) {
            System.out.println(String.format("  %s", changed_parameter.getName()));
        }
        System.out.println(" deleted parameters:");
        for (rcl_interfaces.msg.Parameter deleted_parameter : event.getDeletedParameters()) {
            System.out.println(String.format("  %s", deleted_parameter.getName()));
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a new Node
        Node node = RCLJava.createNode(NODE_NAME);

        SyncParametersClient parameters_client = new SyncParametersClient(node);

        // Setup callback for changes to parameters.
        Subscription<?> sub = parameters_client.onParameterEvent(new ParameterEventCallback() {
            @Override
            public void onEvent(ParameterEvent event) {
                ParameterEvents.onParameterEvent(event);
            }
        });

        // Set several different types of parameters.
//        ArrayList<rcl_interfaces.msg.SetParametersResult> set_parameters_results =
            parameters_client.setParameters(Arrays.<ParameterVariant<?>>asList(
                new ParameterVariant<Long>("foo", 2L),
                new ParameterVariant<String>("bar", "hello"),
                new ParameterVariant<Double>("baz", 1.45),
                new ParameterVariant<Boolean>("foobar", true)
        ));

        // Change the value of some of them.
//        set_parameters_results =
            parameters_client.setParameters(Arrays.<ParameterVariant<?>>asList(
                new ParameterVariant<Long>("foo", 3L),
                new ParameterVariant<String>("bar", "world"),
                new ParameterVariant<String>("foobar", null)
        ));

        for (int i = 0; i < 6; i++) {
            RCLJava.spinOnce(node);
        }


        sub.dispose();
        node.dispose();
        RCLJava.shutdown();
    }

}
