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

 package org.eclipse.uprotocol.utransport.datamodel;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * The UPayload contains the clean Payload information along with its raw serialized structure of a byte[].
 */
public class UPayload {

    private static final UPayload EMPTY = new UPayload(new byte[0], null);

    private final byte[] data;

    private final Integer size; // The size of the payload in bytes

    private final USerializationHint hint;  // Hint regarding the bytes contained within the UPayload


    /**
     * Create a UPayload.
     * @param data A byte array of the actual data.
     */
    public UPayload(byte[] data, USerializationHint hint) {
        this(data, data == null ? 0 : data.length, hint);
    }

    /**
     * Create a UPayload passing a fixed size
     * @param data A byte array of the actual data.
     */
    public UPayload(byte[] data, Integer size, USerializationHint hint) {
        this.hint = hint;
        this.data = data;
        this.size = size;
    }


    /**
     * The actual serialized or raw data, which can be deserialized or simply used as is.
     * @return Returns the actual serialized or raw data, which can be deserialized or simply used as is.
     */
    public byte[] data() {
        return this.data == null ? EMPTY.data() : this.data;
    }

    /**
     * The hint regarding the bytes contained within the UPayload.
     * @return Returns the hint regarding the bytes contained within the UPayload.
     */
    public Optional<USerializationHint> hint() {
        return (hint == null) ? Optional.empty() : Optional.of(hint);
    }
    
    /**
     * @return Returns an empty representation of UPayload.
     */
    public static UPayload empty() {
        return EMPTY;
    }

    /**
     * Static factory method for creating the payload from a simple String.
     * @param payload String payload.
     * @param hint Optional hint regarding the bytes contained within the UPayload.
     * @return Returns a UPayload from the string argument.
     */
    public static UPayload fromString(String payload, USerializationHint hint) {
        return new UPayload(payload.getBytes(), hint);
    }

    /**
     * @return Returns true if the data in the UPayload is empty.
     */
    public boolean isEmpty() {
        return this.data == null || this.data.length == 0;
    }

    /**
     * The size of the payload in bytes
     * @return Returns the size of the payload in bytes
     */
    public Integer size() {
        return size;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UPayload uPayload = (UPayload) o;
        return Arrays.equals(data, uPayload.data) && this.hint == uPayload.hint
                && Objects.equals(this.size, uPayload.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(data),hint, size);
    }

    @Override
    public String toString() {
        return "UPayload{" +
                "data=" + Arrays.toString(data()) + " size=" + size + 
                ", hint=" + hint +'}';
    }
}
