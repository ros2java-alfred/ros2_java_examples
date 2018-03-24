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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.NativeNode;
import org.ros2.rcljava.node.service.Client;

import example_interfaces.srv.AddTwoInts;
import example_interfaces.srv.AddTwoInts_Request;
import example_interfaces.srv.AddTwoInts_Response;

public class AddTwoIntsClient {

    public static class AddTwoIntsClientNode extends NativeNode {

        private static final String NODE_NAME = AddTwoIntsClient.class.getSimpleName().toLowerCase();

        private final Client<AddTwoInts> client;

        public AddTwoIntsClientNode() {
            super(NODE_NAME);

            this.client = this.<AddTwoInts>createClient(AddTwoInts.class, "add_two_ints");
        }

        public void getSum() throws InterruptedException, ExecutionException {
            // Set request.
            final AddTwoInts_Request request = new AddTwoInts_Request();
            request.setA(2);
            request.setB(3);

            // Call service...
            final Future<AddTwoInts_Response> future = this.client.sendRequest(request);
            if (future != null) {
                System.out.println(String.format("Result of add_two_ints: %d", future.get().getSum()));
            } else {
                System.out.println("add_two_ints_client was interrupted. Exiting.");
            }
        }

        @Override
        public void dispose() {
            this.client.dispose();
            super.dispose();
        }
    }

    public static void main(String[] args) throws Exception {
        // Initialize RCL
        RCLJava.rclJavaInit();

        // Let's create a new Node
        AddTwoIntsClientNode node = new AddTwoIntsClientNode();
        node.getSum();

        // Release all.
        node.close();
        RCLJava.shutdown();
    }

}
