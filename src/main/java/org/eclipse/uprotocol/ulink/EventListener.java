/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.eclipse.uprotocol.ulink;

import org.eclipse.uprotocol.cloudevent.datamodel.UCloudEventAttributes;
import org.eclipse.uprotocol.uri.datamodel.UUri;

import com.google.protobuf.Any;

/**
 *  uP-L2 Event Listener Interace for the uBus
 */
public interface EventListener {

	/**
     *  Receive Events from the bus
	 * When messages are received, this function is called.
	 * @param ce CloudEvent received
	 @return void
     */
	void onEvent(UUri topic, Any data, UCloudEventAttributes attributes);
}
