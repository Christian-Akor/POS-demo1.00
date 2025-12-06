# Middleware Simulator Demo Log

## Starting the Server

```bash
$ npm start
Middleware Simulator listening on port 3000
POST /route - Route ISO8583 messages
GET /health - Health check
```

## Test 1: Visa Transaction (PAN starts with 4)

**Request:**
```bash
$ curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=4111111111111111;3=000000;4=000000010000"
```

**Server Log:**
```
Received request: 0200;2=4111111111111111;3=000000;4=000000010000
Parsed message: {
  mti: '0200',
  fields: { '2': '4111111111111111', '3': '000000', '4': '000000010000' }
}
Routing to VisaAcquirer based on PAN: 4111111111111111
Sending response: 0210;2=4111111111111111;3=000000;4=000000010000;39=00;Routed=VisaAcquirer
```

**Response:**
```
0210;2=4111111111111111;3=000000;4=000000010000;39=00;Routed=VisaAcquirer
```

**Observations:**
- MTI converted from 0200 → 0210 ✓
- Field 39 (response code) added with value "00" (approved) ✓
- Field "Routed" added with value "VisaAcquirer" ✓
- Processing delay: ~250-450ms (randomized between 200-500ms) ✓

---

## Test 2: Mastercard Transaction (PAN starts with 5)

**Request:**
```bash
$ curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=5500000000000004;3=000000;4=000000020000"
```

**Server Log:**
```
Received request: 0200;2=5500000000000004;3=000000;4=000000020000
Parsed message: {
  mti: '0200',
  fields: { '2': '5500000000000004', '3': '000000', '4': '000000020000' }
}
Routing to MastercardAcquirer based on PAN: 5500000000000004
Sending response: 0210;2=5500000000000004;3=000000;4=000000020000;39=00;Routed=MastercardAcquirer
```

**Response:**
```
0210;2=5500000000000004;3=000000;4=000000020000;39=00;Routed=MastercardAcquirer
```

**Observations:**
- Correctly routed to MastercardAcquirer based on PAN prefix "5" ✓
- All transformation rules applied correctly ✓

---

## Test 3: Discover Transaction (PAN starts with 6)

**Request:**
```bash
$ curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=6011111111111117;3=000000;4=000000030000"
```

**Response:**
```
0210;2=6011111111111117;3=000000;4=000000030000;39=00;Routed=DiscoverAcquirer
```

---

## Test 4: Amex Transaction (PAN starts with 3)

**Request:**
```bash
$ curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=378282246310005;3=000000;4=000000040000"
```

**Response:**
```
0210;2=378282246310005;3=000000;4=000000040000;39=00;Routed=AmexAcquirer
```

---

## Test 5: Unknown Card Type (Default routing)

**Request:**
```bash
$ curl -X POST http://localhost:3000/route \
  -H "Content-Type: text/plain" \
  -d "0200;2=9999999999999999;3=000000;4=000000050000"
```

**Response:**
```
0210;2=9999999999999999;3=000000;4=000000050000;39=00;Routed=DefaultAcquirer
```

---

## Test 6: Health Check

**Request:**
```bash
$ curl http://localhost:3000/health
```

**Response:**
```json
{"status":"ok","service":"middleware-simulator"}
```

---

## Summary

All features working as expected:
- ✓ POST /route endpoint accepts ISO8583 messages
- ✓ Routes based on PAN prefix (field 2)
- ✓ Converts 0200 → 0210
- ✓ Appends field 39=00 (approved)
- ✓ Appends Routed field with acquirer name
- ✓ Simulates 200-500ms delay
- ✓ Health check endpoint available
