# ISO8583 Demo

A simple Java library for parsing and serializing ISO8583 messages.

## Requirements

- Java 11 or higher
- Maven 3.6+

## Project Structure

```
iso8583-demo/
├── src/
│   ├── main/java/com/pos/iso8583/
│   │   ├── ISO8583Message.java    # Message representation
│   │   └── ISO8583Parser.java     # Parser/serializer
│   └── test/java/com/pos/iso8583/
│       ├── ISO8583MessageTest.java
│       └── ISO8583ParserTest.java
└── pom.xml
```

## Building

```bash
cd iso8583-demo
mvn clean install
```

## Running Tests

```bash
mvn test
```

## Usage

### Parsing a Message

```java
import com.pos.iso8583.ISO8583Message;
import com.pos.iso8583.ISO8583Parser;

// Parse an ISO8583 message string
String messageString = "0200;2=1234567890123456;3=000000;4=000000010000";
ISO8583Message message = ISO8583Parser.parse(messageString);

// Access message fields
System.out.println("MTI: " + message.getMti());
System.out.println("PAN: " + message.getField(2));
System.out.println("Processing Code: " + message.getField(3));
System.out.println("Amount: " + message.getField(4));
```

### Creating a Message

```java
import com.pos.iso8583.ISO8583Message;
import com.pos.iso8583.ISO8583Parser;

// Create a new message
ISO8583Message message = new ISO8583Message("0200");
message.setField(2, "1234567890123456");
message.setField(3, "000000");
message.setField(4, "000000010000");

// Serialize to string
String serialized = ISO8583Parser.serialize(message);
System.out.println(serialized);
```

## Message Format

Messages use a simple key-value format:
```
MTI;field=value;field=value;...
```

Example:
```
0200;2=1234567890123456;3=000000;4=000000010000
```

Where:
- `0200` is the Message Type Indicator (MTI)
- Fields are numbered (e.g., field 2 is PAN, field 3 is Processing Code)
- Values are separated by semicolons

## License

MIT
