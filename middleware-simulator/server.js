const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

// Middleware to parse JSON and text
app.use(express.json());
app.use(express.text());

// Helper function to parse ISO8583 message
function parseISO8583(messageString) {
  const parts = messageString.split(';');
  const message = {
    mti: parts[0].trim(),
    fields: {}
  };
  
  for (let i = 1; i < parts.length; i++) {
    const part = parts[i].trim();
    if (part) {
      const [field, ...valueParts] = part.split('=');
      message.fields[field] = valueParts.join('=');
    }
  }
  
  return message;
}

// Helper function to serialize ISO8583 message
function serializeISO8583(message) {
  let result = message.mti;
  
  // Sort fields by key and append
  const sortedFields = Object.keys(message.fields).sort((a, b) => {
    const numA = parseInt(a);
    const numB = parseInt(b);
    return numA - numB;
  });
  
  for (const field of sortedFields) {
    result += `;${field}=${message.fields[field]}`;
  }
  
  return result;
}

// Routing configuration based on PAN prefix
const routingTable = {
  '4': 'VisaAcquirer',
  '5': 'MastercardAcquirer',
  '6': 'DiscoverAcquirer',
  '3': 'AmexAcquirer'
};

// Function to determine acquirer based on PAN prefix
function getAcquirer(pan) {
  if (!pan) return 'DefaultAcquirer';
  
  const firstDigit = pan.charAt(0);
  return routingTable[firstDigit] || 'DefaultAcquirer';
}

// Simulate processing delay (200-500ms)
function simulateDelay() {
  const minDelay = 200;
  const maxDelay = 500;
  const delay = Math.floor(Math.random() * (maxDelay - minDelay + 1)) + minDelay;
  return new Promise(resolve => setTimeout(resolve, delay));
}

// POST /route endpoint
app.post('/route', async (req, res) => {
  try {
    console.log('Received request:', req.body);
    
    // Parse the incoming message
    const messageString = typeof req.body === 'string' ? req.body : JSON.stringify(req.body);
    const message = parseISO8583(messageString);
    
    console.log('Parsed message:', message);
    
    // Check if it's a 0200 (Authorization Request)
    if (message.mti !== '0200') {
      return res.status(400).json({
        error: 'Only 0200 messages are supported',
        receivedMTI: message.mti
      });
    }
    
    // Get PAN from field 2
    const pan = message.fields['2'];
    
    // Determine acquirer
    const acquirer = getAcquirer(pan);
    
    console.log(`Routing to ${acquirer} based on PAN: ${pan}`);
    
    // Simulate processing delay
    await simulateDelay();
    
    // Convert 0200 -> 0210 (Authorization Response)
    message.mti = '0210';
    
    // Append response fields
    message.fields['39'] = '00'; // Response code (00 = approved)
    message.fields['Routed'] = acquirer;
    
    // Serialize the response
    const response = serializeISO8583(message);
    
    console.log('Sending response:', response);
    
    // Send response
    res.type('text/plain').send(response);
    
  } catch (error) {
    console.error('Error processing request:', error);
    res.status(500).json({
      error: 'Internal server error',
      message: error.message
    });
  }
});

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ status: 'ok', service: 'middleware-simulator' });
});

// Start server
app.listen(PORT, () => {
  console.log(`Middleware Simulator listening on port ${PORT}`);
  console.log(`POST /route - Route ISO8583 messages`);
  console.log(`GET /health - Health check`);
});

module.exports = app;
