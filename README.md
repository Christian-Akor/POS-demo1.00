# POS-demo1.00

ISO8583 Payment Processing Demo - Java module and Node.js middleware simulator.

## Projects

### 1. iso8583-demo (Java)

A Maven-based Java library for parsing and serializing ISO8583 messages.

**Requirements:** Java 11+, Maven 3.6+

**Quick Start:**
```bash
cd iso8583-demo
mvn clean test
```

[Read more →](iso8583-demo/README.md)

### 2. middleware-simulator (Node.js)

An Express-based middleware that routes ISO8583 messages based on PAN prefix.

**Requirements:** Node.js 18+

**Quick Start:**
```bash
cd middleware-simulator
npm install
npm start
```

[Read more →](middleware-simulator/README.md)

## Features

### ISO8583 Demo (Java)
- ✓ ISO8583Message class for message representation
- ✓ ISO8583Parser for parsing and serializing messages
- ✓ Comprehensive unit tests (21 tests, all passing)
- ✓ Simple key-value message format

### Middleware Simulator (Node.js)
- ✓ POST /route endpoint for message routing
- ✓ PAN prefix-based routing (Visa, Mastercard, Discover, Amex)
- ✓ 0200 → 0210 MTI conversion
- ✓ Automatic response field appending (39=00, Routed=AcquirerName)
- ✓ Simulated processing delay (200-500ms)
- ✓ Health check endpoint

## Demo Log

See [middleware-simulator/DEMO_LOG.md](middleware-simulator/DEMO_LOG.md) for example usage and test results.

## License

MIT - See [LICENSE](LICENSE) file for details.
