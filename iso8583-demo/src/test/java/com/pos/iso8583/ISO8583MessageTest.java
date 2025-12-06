package com.pos.iso8583;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ISO8583MessageTest {

    @Test
    void testDefaultConstructor() {
        ISO8583Message message = new ISO8583Message();
        assertNull(message.getMti());
        assertNotNull(message.getDataElements());
        assertTrue(message.getDataElements().isEmpty());
    }

    @Test
    void testConstructorWithMti() {
        ISO8583Message message = new ISO8583Message("0200");
        assertEquals("0200", message.getMti());
        assertTrue(message.getDataElements().isEmpty());
    }

    @Test
    void testSetAndGetMti() {
        ISO8583Message message = new ISO8583Message();
        message.setMti("0200");
        assertEquals("0200", message.getMti());
    }

    @Test
    void testSetAndGetField() {
        ISO8583Message message = new ISO8583Message("0200");
        message.setField(2, "1234567890123456");
        assertEquals("1234567890123456", message.getField(2));
    }

    @Test
    void testHasField() {
        ISO8583Message message = new ISO8583Message("0200");
        assertFalse(message.hasField(2));
        message.setField(2, "1234567890123456");
        assertTrue(message.hasField(2));
    }

    @Test
    void testMultipleFields() {
        ISO8583Message message = new ISO8583Message("0200");
        message.setField(2, "1234567890123456");
        message.setField(3, "000000");
        message.setField(4, "000000010000");
        
        assertEquals("1234567890123456", message.getField(2));
        assertEquals("000000", message.getField(3));
        assertEquals("000000010000", message.getField(4));
        assertEquals(3, message.getDataElements().size());
    }

    @Test
    void testGetDataElementsReturnsNewMap() {
        ISO8583Message message = new ISO8583Message("0200");
        message.setField(2, "test");
        
        var elements = message.getDataElements();
        elements.put(3, "modified");
        
        // Original message should not be modified
        assertFalse(message.hasField(3));
    }

    @Test
    void testToString() {
        ISO8583Message message = new ISO8583Message("0200");
        message.setField(2, "1234567890123456");
        message.setField(3, "000000");
        
        String result = message.toString();
        assertTrue(result.contains("0200"));
        assertTrue(result.contains("Field 2=1234567890123456"));
        assertTrue(result.contains("Field 3=000000"));
    }
}
