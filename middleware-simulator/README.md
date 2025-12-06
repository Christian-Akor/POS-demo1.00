# Middleware Simulator

A Node.js Express-based ISO8583 middleware routing simulator that routes transaction messages based on PAN prefix.

## Requirements

- Node.js 18+ 
- npm

## Installation

```bash
cd middleware-simulator
npm install
```

## Running the Server

```bash
npm start
```

The server will start on port 3000 (or the port specified in the `PORT` environment variable).

## API Endpoints

### POST /route

Routes ISO8583 messages based on PAN prefix.

**Request:**
- Content-Type: text/plain
- Body: ISO8583 message in format `MTI;field=value;field=value;...`

**Example Request:**
```
0200;2=4111111111111111;3=000000;4=000000010000
```

**Features:**
- Converts 0200 (Authorization Request) → 0210 (Authorization Response)
- Routes based on PAN (field 2) prefix:
  - 4xxx → VisaAcquirer
  - 5xxx → MastercardAcquirer
  - 6xxx → DiscoverAcquirer
  - 3xxx → AmexAcquirer
  - Others → DefaultAcquirer
- Appends response fields:
  - Field 39: Response code (00 = approved)
  - Field Routed: Acquirer name
- Simulates processing delay of 200-500ms

**Example Response:**
```
0210;2=4111111111111111;3=000000;4=000000010000;39=00;Routed=VisaAcquirer
```

### GET /health

Health check endpoint.

**Response:**
```json
{
  "status": "ok",
  "service": "middleware-simulator"
}
```

## Testing

You can test the middleware using curl:

```bash
# Test with Visa PAN (starts with 4)
curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=4111111111111111;3=000000;4=000000010000"

# Test with Mastercard PAN (starts with 5)
curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=5500000000000004;3=000000;4=000000020000"

# Health check
curl http://localhost:3000/health
```

## Message Format

The middleware uses a simplified ISO8583 format:
```
MTI;field=value;field=value;...
```

Common fields:
- MTI: Message Type Indicator (e.g., 0200, 0210)
- Field 2: Primary Account Number (PAN)
- Field 3: Processing Code
- Field 4: Transaction Amount
- Field 39: Response Code
- Field Routed: Acquirer routing information (added by middleware)

## License

MIT
