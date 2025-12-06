package com.pos.iso8583;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ISO8583ParserTest {

    @Test
    void testParseSimpleMessage() {
        String messageString = "0200;2=1234567890123456;3=000000;4=000000010000";
        ISO8583Message message = ISO8583Parser.parse(messageString);
        
        assertEquals("0200", message.getMti());
        assertEquals("1234567890123456", message.getField(2));
        assertEquals("000000", message.getField(3));
        assertEquals("000000010000", message.getField(4));
    }

    @Test
    void testParseMessageWithOnlyMti() {
        String messageString = "0200";
        ISO8583Message message = ISO8583Parser.parse(messageString);
        
        assertEquals("0200", message.getMti());
        assertTrue(message.getDataElements().isEmpty());
    }

    @Test
    void testParseMessageWithWhitespace() {
        String messageString = "0200 ; 2=1234567890123456 ; 3=000000 ";
        ISO8583Message message = ISO8583Parser.parse(messageString);
        
        assertEquals("0200", message.getMti());
        assertEquals("1234567890123456", message.getField(2));
        assertEquals("000000", message.getField(3));
    }

    @Test
    void testParseMessageWithEmptyFields() {
        String messageString = "0200;;2=1234567890123456;;3=000000";
        ISO8583Message message = ISO8583Parser.parse(messageString);
        
        assertEquals("0200", message.getMti());
        assertEquals("1234567890123456", message.getField(2));
        assertEquals("000000", message.getField(3));
    }

    @Test
    void testParseNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.parse(null);
        });
    }

    @Test
    void testParseEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.parse("");
        });
    }

    @Test
    void testParseInvalidMti() {
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.parse("ABC;2=test");
        });
    }

    @Test
    void testParseInvalidFieldFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.parse("0200;invalidfield");
        });
    }

    @Test
    void testSerializeMessage() {
        ISO8583Message message = new ISO8583Message("0200");
        message.setField(2, "1234567890123456");
        message.setField(3, "000000");
        message.setField(4, "000000010000");
        
        String serialized = ISO8583Parser.serialize(message);
        
        assertTrue(serialized.startsWith("0200"));
        assertTrue(serialized.contains("2=1234567890123456"));
        assertTrue(serialized.contains("3=000000"));
        assertTrue(serialized.contains("4=000000010000"));
    }

    @Test
    void testSerializeAndParseRoundTrip() {
        ISO8583Message original = new ISO8583Message("0200");
        original.setField(2, "1234567890123456");
        original.setField(3, "000000");
        original.setField(4, "000000010000");
        
        String serialized = ISO8583Parser.serialize(original);
        ISO8583Message parsed = ISO8583Parser.parse(serialized);
        
        assertEquals(original.getMti(), parsed.getMti());
        assertEquals(original.getField(2), parsed.getField(2));
        assertEquals(original.getField(3), parsed.getField(3));
        assertEquals(original.getField(4), parsed.getField(4));
    }

    @Test
    void testSerializeNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.serialize(null);
        });
    }

    @Test
    void testSerializeMessageWithNullMti() {
        ISO8583Message message = new ISO8583Message();
        message.setField(2, "test");
        
        assertThrows(IllegalArgumentException.class, () -> {
            ISO8583Parser.serialize(message);
        });
    }

    @Test
    void testParseComplexFieldValues() {
        String messageString = "0210;2=1234567890123456;39=00;48=Routed to AcquirerA";
        ISO8583Message message = ISO8583Parser.parse(messageString);
        
        assertEquals("0210", message.getMti());
        assertEquals("1234567890123456", message.getField(2));
        assertEquals("00", message.getField(39));
        assertEquals("Routed to AcquirerA", message.getField(48));
        assertNull(message.getField(999)); // Non-existent field
    }
}
