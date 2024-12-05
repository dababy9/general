// Constants pertinent to server functionality
const port = 9000;

// Import necessary modules/methods
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const { Game } = require('./game.js');

// Create Express app and pass it to HTTP server
const app = express();
const server = createServer(app);

// Mount the socket.io server on the HTTP server
const io = new Server(server);

// Express app now serves static files from 'static' directory
app.use('/static', express.static('static'));

// ---------- DEFINING ENDPOINTS ----------
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/html/index.html');
});

// Bind server to the specified port, and listen for incoming requests
server.listen(port, () => {
    console.log('Listening on port 9000');
});

io.on('connection', (socket) => {
    console.log("connec");
})