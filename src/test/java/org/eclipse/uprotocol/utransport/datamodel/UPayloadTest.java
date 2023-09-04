package org.eclipse.uprotocol.utransport.datamodel;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UPayloadTest {

    @Test
    @DisplayName("Make sure the equals and hash code works")
    public void testHashCodeEquals() {
        EqualsVerifier.forClass(UPayload.class).usingGetClass().verify();
    }

    @Test
    @DisplayName("Make sure the toString works on empty")
    public void testToString_with_empty() {
        UPayload uPayload = UPayload.empty();
        assertEquals("UPayload{data=[] size=0}", uPayload.toString());
    }

    @Test
    @DisplayName("Make sure the toString works on null")
    public void testToString_with_null() {
        UPayload uPayload = new UPayload(null);
        assertEquals("UPayload{data=[] size=0}", uPayload.toString());
    }

    @Test
    @DisplayName("Make sure the toString works")
    public void testToString() {
        UPayload uPayload = UPayload.fromString("hello");
        assertEquals("UPayload{data=[104, 101, 108, 108, 111] size=5}", uPayload.toString());
    }

    @Test
    @DisplayName("Create an empty UPyload")
    public void create_an_empty_upayload() {
        UPayload uPayload = UPayload.empty();
        assertEquals(0, uPayload.data().length);
        assertTrue(uPayload.isEmpty());
    }

    @Test
    @DisplayName("Create a UPyload with null")
    public void create_upayload_with_null() {
        UPayload uPayload = new UPayload(null);
        assertEquals(0, uPayload.data().length);
        assertTrue(uPayload.isEmpty());
    }

    @Test
    @DisplayName("Create a UPayload from some bytes")
    public void create_upayload_from_bytes() {
        String stringData = "hello";
        UPayload uPayload = new UPayload(stringData.getBytes(StandardCharsets.UTF_8));
        assertEquals(stringData.length(), uPayload.data().length);
        assertFalse(uPayload.isEmpty());
        assertEquals(stringData, new String(uPayload.data()));
    }

    @Test
    @DisplayName("Create a UPayload from a string")
    public void create_upayload_from_a_string() {
        String stringData = "hello world";
        UPayload uPayload = UPayload.fromString(stringData);
        assertEquals(stringData.length(), uPayload.data().length);
        assertFalse(uPayload.isEmpty());
        assertEquals(stringData, new String(uPayload.data()));
    }

}