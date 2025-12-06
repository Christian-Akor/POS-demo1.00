package com.pos.iso8583;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an ISO8583 message with MTI and data elements.
 */
public class ISO8583Message {
    private String mti;
    private Map<Integer, String> dataElements;

    public ISO8583Message() {
        this.dataElements = new HashMap<>();
    }

    public ISO8583Message(String mti) {
        this.mti = mti;
        this.dataElements = new HashMap<>();
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }

    public void setField(int fieldNumber, String value) {
        dataElements.put(fieldNumber, value);
    }

    public String getField(int fieldNumber) {
        return dataElements.get(fieldNumber);
    }

    public Map<Integer, String> getDataElements() {
        return new HashMap<>(dataElements);
    }

    public boolean hasField(int fieldNumber) {
        return dataElements.containsKey(fieldNumber);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ISO8583Message{\n");
        sb.append("  MTI=").append(mti).append("\n");
        for (Map.Entry<Integer, String> entry : dataElements.entrySet()) {
            sb.append("  Field ").append(entry.getKey())
              .append("=").append(entry.getValue()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
