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
package org.ros2.rcljava.demo.services;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.node.service.Service;
import org.ros2.rcljava.node.service.ServiceCallback;
import org.ros2.rcljava.node.service.RMWRequestId;

import example_interfaces.srv.AddTwoInts;
import example_interfaces.srv.AddTwoInts_Request;
import example_interfaces.srv.AddTwoInts_Response;

public class AddTwoIntsServer {

    public static class AddTwoIntsServerNode extends NativeNode {

        private static final String NODE_NAME = AddTwoIntsServer.class.getSimpleName().toLowerCase();

        private final Service<AddTwoInts> service;

        public AddTwoIntsServerNode() {
            super(NODE_NAME);

            // Create a service.
            this.service = this.<AddTwoInts>createService(
                    AddTwoInts.class,
                    "add_two_ints",
                    new ServiceCallback<AddTwoInts_Request, AddTwoInts_Response>() {

                        // We define the callback inline, this works with Java 8's
                        // lambdas
                        // too, but we use our own TriConsumer interface because
                        // Android
                        // supports lambdas via retrolambda, but not the lambda API
                        @Override
                        public void dispatch(
                                final RMWRequestId header,
                                final AddTwoInts_Request request,
                                final AddTwoInts_Response response) {
                            AddTwoIntsServerNode.handleAddTwoInts(request, response);
                        }
                    });

        }

        public static void handleAddTwoInts(
                final AddTwoInts_Request request,
                final AddTwoInts_Response response) {

            System.out.println("Incoming request");
            System.out.println(String.format("a: %d b: %d", request.getA(), request.getB()));
            response.setSum(request.getA() + request.getB());
        }

        @Override
        public void dispose() {
            this.service.dispose();
            super.dispose();
        }
    }

    public static void main(String[] args) {

        // Initialize RCL
        RCLJava.rclJavaInit(args);

        // Let's create a new Node
        Node node = new AddTwoIntsServerNode();

        // Wait...
        RCLJava.spin(node);

        // Release all.
        node.dispose();
        RCLJava.shutdown();
    }

}
