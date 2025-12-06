package com.pos.iso8583;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses ISO8583 messages in a simple key-value format.
 * Format: MTI;field=value;field=value;...
 * Example: 0200;2=1234567890123456;3=000000;4=000000010000
 */
public class ISO8583Parser {

    /**
     * Parses an ISO8583 message string into an ISO8583Message object.
     * @param messageString The message string to parse
     * @return An ISO8583Message object
     * @throws IllegalArgumentException if the message format is invalid
     */
    public static ISO8583Message parse(String messageString) {
        if (messageString == null || messageString.trim().isEmpty()) {
            throw new IllegalArgumentException("Message string cannot be null or empty");
        }

        String[] parts = messageString.split(";");
        if (parts.length == 0) {
            throw new IllegalArgumentException("Invalid message format");
        }

        ISO8583Message message = new ISO8583Message();
        
        // First part is the MTI
        String mti = parts[0].trim();
        if (!isValidMti(mti)) {
            throw new IllegalArgumentException("Invalid MTI format: " + mti);
        }
        message.setMti(mti);

        // Parse remaining parts as field=value pairs
        Pattern fieldPattern = Pattern.compile("^(\\d+)=(.*)$");
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty()) {
                continue;
            }
            
            Matcher matcher = fieldPattern.matcher(part);
            if (matcher.matches()) {
                int fieldNumber = Integer.parseInt(matcher.group(1));
                String value = matcher.group(2);
                message.setField(fieldNumber, value);
            } else {
                throw new IllegalArgumentException("Invalid field format: " + part);
            }
        }

        return message;
    }

    /**
     * Serializes an ISO8583Message object into a string format.
     * @param message The message to serialize
     * @return A string representation of the message
     */
    public static String serialize(ISO8583Message message) {
        if (message == null || message.getMti() == null) {
            throw new IllegalArgumentException("Message and MTI cannot be null");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(message.getMti());

        // Add fields in sorted order
        message.getDataElements().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                sb.append(";").append(entry.getKey())
                  .append("=").append(entry.getValue());
            });

        return sb.toString();
    }

    /**
     * Validates if a string is a valid MTI (4 digits).
     * @param mti The MTI to validate
     * @return true if valid, false otherwise
     */
    private static boolean isValidMti(String mti) {
        return mti != null && mti.matches("\\d{4}");
    }
}
