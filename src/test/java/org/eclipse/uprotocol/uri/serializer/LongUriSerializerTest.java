/*
 * Copyright (c) 2023 General Motors GTO LLC
 *
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
package org.eclipse.uprotocol.uri.serializer;

import org.eclipse.uprotocol.uri.builder.UResourceBuilder;
import org.eclipse.uprotocol.uri.validator.UriValidator;
import org.eclipse.uprotocol.v1.UEntity;
import org.eclipse.uprotocol.v1.UUri;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LongUriSerializerTest {


    @Test
    @DisplayName("Test using the serializers")
    public void test_using_the_serializers() {
        final UUri uri = UUri.newBuilder().setEntity(UEntity.newBuilder().setName("hartley")).setResource(UResourceBuilder.forRpcRequest("raise")).build();
        final String strUri = LongUriSerializer.instance().serialize(uri);
        assertEquals("/hartley//rpc.raise", strUri);
        final UUri uri2 = LongUriSerializer.instance().deserialize(strUri);
        assertEquals(uri, uri2);
    }

    @Test
    @DisplayName("Test parse uProtocol uri that is null")
    public void test_parse_protocol_uri_when_is_null() {
        UUri uri = LongUriSerializer.instance().deserialize(null);
        assertTrue(UriValidator.isEmpty(uri));
        assertFalse(UriValidator.isResolved(uri));
        assertFalse(UriValidator.isLongForm(uri));
    }

    @Test
    @DisplayName("Test parse uProtocol uri that is empty string")
    public void test_parse_protocol_uri_when_is_empty_string() {
        String uri = "";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(UriValidator.isEmpty(uuri));

        String uri2 = LongUriSerializer.instance().serialize(null);
        assertTrue(uri2.isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with schema and slash")
    public void test_parse_protocol_uri_with_schema_and_slash() {
        String uri = "/";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(uuri.hasAuthority());
        assertTrue(UriValidator.isEmpty(uuri));
        assertFalse(uuri.hasResource());
        assertFalse(uuri.hasEntity());

        String uri2 = LongUriSerializer.instance().serialize(UUri.newBuilder().build());
        assertTrue(uri2.isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with schema and double slash")
    public void test_parse_protocol_uri_with_schema_and_double_slash() {
        String uri = "//";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(uuri.hasAuthority());
        assertFalse(uuri.hasResource());
        assertFalse(uuri.hasEntity());
        assertTrue(UriValidator.isEmpty(uuri));
    }

    @Test
    @DisplayName("Test parse uProtocol uri with schema and 3 slash and something")
    public void test_parse_protocol_uri_with_schema_and_3_slash_and_something() {
        String uri = "///body.access";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(uuri.hasAuthority());
        assertFalse(uuri.hasResource());
        assertFalse(uuri.hasEntity());
        assertTrue(UriValidator.isEmpty(uuri));
        assertNotEquals("body.access", uuri.getEntity().getName());
        assertEquals(0, uuri.getEntity().getVersionMajor());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with schema and 4 slash and something")
    public void test_parse_protocol_uri_with_schema_and_4_slash_and_something() {
        String uri = "////body.access";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(uuri.hasAuthority());
        assertFalse(uuri.hasResource());
        assertFalse(uuri.hasEntity());
        assertTrue(uuri.getEntity().getName().isBlank());
        assertEquals(0, uuri.getEntity().getVersionMajor());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with schema and 5 slash and something")
    public void test_parse_protocol_uri_with_schema_and_5_slash_and_something() {
        String uri = "/////body.access";
        UUri uuri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(uuri.hasAuthority());
        assertFalse(uuri.hasResource());
        assertFalse(uuri.hasEntity());
        assertTrue(UriValidator.isEmpty(uuri));
    }
 /*
    @Test
    @DisplayName("Test parse uProtocol uri with schema and 6 slash and something")
    public void test_parse_protocol_uri_with_schema_and_6_slash_and_something() {
        String uri = "//////body.access";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(Uri.getAuthority().isLocal());
        assertTrue(!Uri.getAuthority().getLocal());
        assertTrue(Uri.isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service no version")
    public void test_parse_protocol_uri_with_local_service_no_version() {
        String uri = "/body.access";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service with version")
    public void test_parse_protocol_uri_with_local_service_with_version() {
        String uri = "/body.access/1";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertEquals(1, Uri.getEntity().version().orElse(10));
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service no version with resource name only")
    public void test_parse_protocol_uri_with_local_service_no_version_with_resource_name_only() {
        String uri = "/body.access//door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service with version with resource name only")
    public void test_parse_protocol_uri_with_local_service_with_version_with_resource_name_only() {
        String uri = "/body.access/1/door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertEquals(1, Uri.getEntity().version().orElse(10));
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service no version with resource and instance only")
    public void test_parse_protocol_uri_with_local_service_no_version_with_resource_with_instance() {
        String uri = "/body.access//door.front_left";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service with version with resource and instance only")
    public void test_parse_protocol_uri_with_local_service_with_version_with_resource_with_getMessage() {
        String uri = "/body.access/1/door.front_left";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service no version with resource with instance and message")
    public void test_parse_protocol_uri_with_local_service_no_version_with_resource_with_instance_and_getMessage() {
        String uri = "/body.access//door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with local service with version with resource with instance and message")
    public void test_parse_protocol_uri_with_local_service_with_version_with_resource_with_instance_and_getMessage() {
        String uri = "/body.access/1/door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol RPC uri with local service no version")
    public void test_parse_protocol_rpc_uri_with_local_service_no_version() {
        String uri = "/petapp//rpc.response";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("petapp", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("rpc", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("response", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol RPC uri with local service with version")
    public void test_parse_protocol_rpc_uri_with_local_service_with_version() {
        String uri = "/petapp/1/rpc.response";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("petapp", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("rpc", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("response", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service only device no domain")
    public void test_parse_protocol_uri_with_remote_service_only_device_no_domain() {
        String uri = "//VCU";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isEmpty());
        assertTrue(Uri.getEntity().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service only device and domain")
    public void test_parse_protocol_uri_with_remote_service_only_device_and_domain() {
        String uri = "//VCU.MY_CAR_VIN";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertTrue(Uri.getEntity().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service only device and cloud domain")
    public void test_parse_protocol_uri_with_remote_service_only_device_and_cloud_domain() {
        String uri = "//cloud.uprotocol.example.com";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertTrue(Uri.getEntity().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service no version")
    public void test_parse_protocol_uri_with_remote_service_no_version() {
        String uri = "//VCU.MY_CAR_VIN/body.access";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service no version")
    public void test_parse_protocol_uri_with_remote_cloud_service_no_version() {
        String uri = "//cloud.uprotocol.example.com/body.access";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service with version")
    public void test_parse_protocol_uri_with_remote_service_with_version() {
        String uri = "//VCU.MY_CAR_VIN/body.access/1";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service with version")
    public void test_parse_protocol_uri_with_remote_cloud_service_with_version() {
        String uri = "//cloud.uprotocol.example.com/body.access/1";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertTrue(Uri.getResource().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service no version with resource name only")
    public void test_parse_protocol_uri_with_remote_service_no_version_with_resource_name_only() {
        String uri = "//VCU.MY_CAR_VIN/body.access//door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service no version with resource name only")
    public void test_parse_protocol_uri_with_remote_cloud_service_no_version_with_resource_name_only() {
        String uri = "//cloud.uprotocol.example.com/body.access//door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service with version with resource name only")
    public void test_parse_protocol_uri_with_remote_service_with_version_with_resource_name_only() {
        String uri = "//VCU.MY_CAR_VIN/body.access/1/door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service with version with resource name only")
    public void test_parse_protocol_uri_with_remote_service_cloud_with_version_with_resource_name_only() {
        String uri = "//cloud.uprotocol.example.com/body.access/1/door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isEmpty());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service no version with resource and instance no message")
    public void test_parse_protocol_uri_with_remote_service_no_version_with_resource_and_instance_no_getMessage() {
        String uri = "//VCU.MY_CAR_VIN/body.access//door.front_left";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service with version with resource and instance no message")
    public void test_parse_protocol_uri_with_remote_service_with_version_with_resource_and_instance_no_getMessage() {
        String uri = "//VCU.MY_CAR_VIN/body.access/1/door.front_left";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service no version with resource and instance and message")
    public void test_parse_protocol_uri_with_remote_service_no_version_with_resource_and_instance_and_getMessage() {
        String uri = "//VCU.MY_CAR_VIN/body.access//door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service no version with resource and instance and message")
    public void test_parse_protocol_uri_with_remote_cloud_service_no_version_with_resource_and_instance_and_getMessage() {
        String uri = "//cloud.uprotocol.example.com/body.access//door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service with version with resource and instance and message")
    public void test_parse_protocol_uri_with_remote_service_with_version_with_resource_and_instance_and_getMessage() {
        String uri = "//VCU.MY_CAR_VIN/body.access/1/door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("my_car_vin", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote cloud service with version with resource and instance and message")
    public void test_parse_protocol_uri_with_remote_cloud_service_with_version_with_resource_and_instance_and_getMessage() {
        String uri = "//cloud.uprotocol.example.com/body.access/1/door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("cloud", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("uprotocol.example.com", Uri.getAuthority().domain().get());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse uProtocol uri with microRemote service with version with resource with message when there is only device, no domain")
    public void test_parse_protocol_uri_with_remote_service_with_version_with_resource_with_message_device_no_domain() {
        String uri = "//VCU/body.access/1/door.front_left";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("vcu", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isEmpty());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol RPC uri with microRemote service no version")
    public void test_parse_protocol_rpc_uri_with_remote_service_no_version() {
        String uri = "//bo.cloud/petapp//rpc.response";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("bo", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("cloud", Uri.getAuthority().domain().get());
        assertEquals("petapp", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("rpc", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("response", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test parse uProtocol RPC uri with microRemote service with version")
    public void test_parse_protocol_rpc_uri_with_remote_service_with_version() {
        String uri = "//bo.cloud/petapp/1/rpc.response";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isRemote());
        assertTrue(Uri.getAuthority().getName().isPresent());
        assertEquals("bo", Uri.getAuthority().getName().get());
        assertTrue(Uri.getAuthority().domain().isPresent());
        assertEquals("cloud", Uri.getAuthority().domain().get());
        assertEquals("petapp", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isPresent());
        assertEquals(1, Uri.getEntity().version().get());
        assertEquals("rpc", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("response", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isEmpty());
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from null")
    public void test_build_protocol_uri_from__uri_when__uri_isnull() {
        String uProtocolUri = LongUriSerializer.instance().serialize(null);
        assertEquals("", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an empty  URI Object")
    public void test_build_protocol_uri_from__uri_when__uri_isEmpty() {
        UUri Uri = UUri.empty();
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI object with an empty USE")
    public void test_build_protocol_uri_from__uri_when__uri_has_empty_use() {
        UEntity use = UEntity.empty();
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service no version")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_no_version() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.local(), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service and version")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_and_version() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.local(), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access/1", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service no version with resource")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_no_version_with_resource() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access//door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service and version with resource")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_and_version_with_resource() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access/1/door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service no version with resource with instance no message")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_no_version_with_resource_with_instance_no_getMessage() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door", "front_left", null));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access//door.front_left", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service and version with resource with instance no message")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_and_version_with_resource_with_instance_no_getMessage() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door", "front_left", null));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access/1/door.front_left", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service no version with resource with instance and message")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_no_version_with_resource_with_instance_with_getMessage() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door", "front_left", "Door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access//door.front_left#Door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a local authority with service and version with resource with instance and message")
    public void test_build_protocol_uri_from__uri_when__uri_has_local_authority_service_and_version_with_resource_with_instance_with_getMessage() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.local(), use, UResource.longFormat("door", "front_left", "Door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("/body.access/1/door.front_left#Door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service no version")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_no_version() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority no device with domain with service no version")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_no_device_with_domain_with_service_no_version() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.longRemote("", "MY_CAR_VIN"), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//my_car_vin/body.access", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service and version")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_and_version() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access/1", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote cloud authority with service and version")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_cloud_authority_service_and_version() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("cloud", "uprotocol.example.com"), use, UResource.empty());
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//cloud.uprotocol.example.com/body.access/1", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service and version with resource")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_and_version_with_resource() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access/1/door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service no version with resource")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_no_version_with_resource() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access//door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service and version with resource with instance no message")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_and_version_with_resource_with_instance_no_getMessage() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door", "front_left", null));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access/1/door.front_left", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote cloud authority with service and version with resource with instance no message")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_cloud_authority_service_and_version_with_resource_with_instance_no_getMessage() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("cloud", "uprotocol.example.com"), use, UResource.longFormat("door", "front_left", null));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//cloud.uprotocol.example.com/body.access/1/door.front_left", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service no version with resource with instance no message")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_no_version_with_resource_with_instance_no_getMessage() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door", "front_left", null));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access//door.front_left", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service and version with resource with instance and message")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_and_version_with_resource_with_instance_and_getMessage() {
        UEntity use = UEntity.longFormat("body.access", 1);
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door", "front_left", "Door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access/1/door.front_left#Door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from an  URI Object with a microRemote authority with service no version with resource with instance and message")
    public void test_build_protocol_uri_from__uri_when__uri_has_remote_authority_service_no_version_with_resource_with_instance_and_getMessage() {
        UEntity use = UEntity.longFormat("body.access");
        UUri Uri = new UUri(UAuthority.longRemote("VCU", "MY_CAR_VIN"), use, UResource.longFormat("door", "front_left", "Door"));
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("//vcu.my_car_vin/body.access//door.front_left#Door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI for the source part of an RPC request, where the source is local")
    public void test_build_protocol_uri_for_source_part_of_rpc_request_where_source_is_local() {
        UAuthority uAuthority = UAuthority.local();
        UEntity use = UEntity.longFormat("petapp", 1);
        String uProtocolUri = LongUriSerializer.instance().serialize(UUri.rpcResponse(uAuthority, use));
        assertEquals("/petapp/1/rpc.response", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI for the source part of an RPC request, where the source is microRemote")
    public void test_build_protocol_uri_for_source_part_of_rpc_request_where_source_is_remote() {
        UAuthority uAuthority = UAuthority.longRemote("cloud", "uprotocol.example.com");
        UEntity use = UEntity.longFormat("petapp");
        String uProtocolUri = LongUriSerializer.instance().serialize(UUri.rpcResponse(uAuthority, use));
        assertEquals("//cloud.uprotocol.example.com/petapp//rpc.response", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from parts that are null")
    public void test_build_protocol_uri_from_parts_when_they_are_null() {
        UAuthority uAuthority = null;
        UEntity uSoftwareEntity = null;
        UResource uResource = null;
        UUri Uri = new UUri(uAuthority, uSoftwareEntity, uResource);
        String uProtocolUri = LongUriSerializer.instance().serialize(Uri);
        assertEquals("", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a uProtocol URI from the parts of  URI Object with a microRemote authority with service and version with resource")
    public void test_build_protocol_uri_from__uri_parts_when__uri_has_remote_authority_service_and_version_with_resource() {
        UAuthority uAuthority = UAuthority.longRemote("VCU", "MY_CAR_VIN");
        UEntity use = UEntity.longFormat("body.access", 1);
        UResource uResource = UResource.longFormat("door");
        String uProtocolUri = LongUriSerializer.instance().serialize(new UUri(uAuthority, use, uResource));
        assertEquals("//vcu.my_car_vin/body.access/1/door", uProtocolUri);
    }

    @Test
    @DisplayName("Test Create a URI using no scheme")
    public void test_custom_scheme_no_scheme_empty() {
        UAuthority uAuthority = null;
        UEntity uSoftwareEntity = null;
        UResource uResource = null;
        String customUri = LongUriSerializer.instance().serialize(new UUri(uAuthority, uSoftwareEntity, uResource));
        assertTrue(customUri.isEmpty());
    }

    @Test
    @DisplayName("Test Create a custom URI using no scheme")
    public void test_custom_scheme_no_scheme() {
        UAuthority uAuthority = UAuthority.longRemote("VCU", "MY_CAR_VIN");
        UEntity use = UEntity.longFormat("body.access", 1);
        UResource uResource = UResource.longFormat("door");
        String ucustomUri = LongUriSerializer.instance().serialize(new UUri(uAuthority, use, uResource));
        assertEquals("//vcu.my_car_vin/body.access/1/door", ucustomUri);
    }

    @Test
    @DisplayName("Test parse local uProtocol uri with custom scheme")
    public void test_parse_local_protocol_uri_with_custom_scheme() {
        String uri = "custom:/body.access//door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertTrue(Uri.getAuthority().isLocal());
        assertFalse(!Uri.getAuthority().getLocal());
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertTrue(Uri.getResource().instance().isPresent());
        assertEquals("front_left", Uri.getResource().instance().get());
        assertTrue(Uri.getResource().getMessage().isPresent());
        assertEquals("Door", Uri.getResource().getMessage().get());
    }

    @Test
    @DisplayName("Test parse microRemote uProtocol uri with custom scheme")
    public void test_parse_remote_protocol_uri_with_custom_scheme() {
        String uri = "custom://vcu.vin/body.access//door.front_left#Door";
        String uri2 = "//vcu.vin/body.access//door.front_left#Door";
        UUri Uri = LongUriSerializer.instance().deserialize(uri);
        assertFalse(Uri.getAuthority().isLocal());
        assertTrue(!Uri.getAuthority().getLocal());
        assertEquals("vcu", Uri.getAuthority().getName().orElse(""));
        assertEquals("vin", Uri.getAuthority().domain().orElse(""));
        assertEquals("body.access", Uri.getEntity().getName());
        assertTrue(Uri.getEntity().version().isEmpty());
        assertEquals("door", Uri.getResource().getName());
        assertEquals("front_left", Uri.getResource().instance().orElse(""));
        assertEquals("Door", Uri.getResource().getMessage().orElse(""));
        assertEquals(uri2, LongUriSerializer.instance().serialize(Uri));
    }

    @Test
    @DisplayName("Test build resolved uri passing null")
    void test_deserialize_long_and_micro_passing_null() {
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved(null, null);
        assertTrue(uri.isPresent());
        assertTrue(uri.get().isEmpty());
    }

    @Test
    @DisplayName("Test build resolved uri passing null long uri empty byte array")
    void test_deserialize_long_and_micro_passing_null_long_uri_empty_byte_array() {
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved(null, new byte[0]);
        assertTrue(uri.isPresent());
        assertTrue(uri.get().isEmpty());
    }

    @Test
    @DisplayName("Test build resolved uri passing empty long uri null byte array")
    void test_deserialize_long_and_micro_passing_nullempty_long_uri_null_byte_array() {
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved("", null);
        assertTrue(uri.isPresent());
        assertTrue(uri.get().isEmpty());
    }

    @Test
    @DisplayName("Test build resolved uri passing empty long uri, empty byte[]")
    void test_deserialize_long_and_micro_passing_empty_long_uri_empty_byte_array() {
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved("", new byte[0]);
        assertTrue(uri.isPresent());
        assertTrue(uri.get().isEmpty());
    }

    @Test
    @DisplayName("Test deserializer a long and micro uri passing UAuthority of different types (local vs remote)")
    void test_deserialize_long_and_micro_passing_UAuthority_that_doesnt_match() {
        String longUUri = "//vcu.vin/body.access//door.front_left#Door";
        byte[] microUUri = new byte[] {0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved(longUUri, microUUri);
        assertTrue(uri.isEmpty());

        String longUUri1 = "/body.access//door.front_left#Door";
        byte[] microUUri1 = new byte[] {0x1, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        Optional<UUri> uri1 = LongUriSerializer.instance().buildResolved(longUUri1, microUUri1);
        assertTrue(uri1.isEmpty());
    }

    @Test
    @DisplayName("Test deserializer a long and micro uri passing invalid values")
    void test_deserialize_long_and_micro_passing_invalid_values() {
        String goodLongUUri = "/body.access//door.front_left#Door";
        byte[] goodMicroUUri = new byte[] {0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
        String badLongUUri = "///";
        byte[] badMicroUUri = new byte[] {0x0, 0x0, 0x0, 0x0, 0x0};
        Optional<UUri> uri = LongUriSerializer.instance().buildResolved(goodLongUUri, goodMicroUUri);
        assertFalse(uri.isEmpty());
        Optional<UUri> uri2 = LongUriSerializer.instance().buildResolved(goodLongUUri, badMicroUUri);
        assertTrue(uri2.isEmpty());
        Optional<UUri> uri3 = LongUriSerializer.instance().buildResolved(badLongUUri, goodMicroUUri);
        assertTrue(uri3.isEmpty());
        Optional<UUri> uri4 = LongUriSerializer.instance().buildResolved(badLongUUri, badMicroUUri);
        assertTrue(uri4.isEmpty());
        Optional<UUri> uri5 = LongUriSerializer.instance().buildResolved("", goodMicroUUri);
        assertTrue(uri5.isEmpty());
        Optional<UUri> uri6 = LongUriSerializer.instance().buildResolved("", badMicroUUri);
        assertTrue(uri6.isEmpty());
        Optional<UUri> uri7 = LongUriSerializer.instance().buildResolved(null, goodMicroUUri);
        assertTrue(uri7.isEmpty());
        Optional<UUri> uri8 = LongUriSerializer.instance().buildResolved(null, badMicroUUri);
        assertTrue(uri8.isEmpty());


        Optional<UUri> uri9 = LongUriSerializer.instance().buildResolved(goodLongUUri, null);
        assertTrue(uri9.isEmpty());
        Optional<UUri> uri10 = LongUriSerializer.instance().buildResolved(goodLongUUri, new byte[0]);
        assertTrue(uri10.isEmpty());

    }

    @Test
    @DisplayName("Test deserializer a long and micro uri passing valid values")
    void test_deserialize_long_and_micro_passing_valid_values() throws UnknownHostException {
        UUri uri = new UUri(UAuthority.resolvedRemote("vcu", "vin", InetAddress.getByName("192.168.1.100")),
            UEntity.resolvedFormat("hartley", 1, (short)5),
            UResource.resolvedFormat("raise", "salary", "Pay", (short)2));
        String longUUri = LongUriSerializer.instance().serialize(uri);
        byte[] microUUri = MicroUriSerializer.instance().serialize(uri);
        
        Optional<UUri> uri2 = LongUriSerializer.instance().buildResolved(longUUri, microUUri);
        assertEquals(uri, uri2.orElse(UUri.empty()));
    }
     */
}
