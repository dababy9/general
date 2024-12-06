// Constants pertinent to server functionality
const port = 9000;

// Import necessary modules/methods
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const { Game } = require('./game.js');
const { randomBytes } = require('crypto');
const { SessionStore } = require('./session.js');

// Used for managing sessions
const randomID = () => randomBytes(16).toString("hex");
const sessionStore = new SessionStore();

// Create Express app and pass it to HTTP server
const app = express();
const server = createServer(app);

// Mount the socket.io server on the HTTP server
const io = new Server(server);

// Express app now serves static files from 'static' directory
app.use('/static', express.static('static'));





// ---------- DEFINING ENDPOINTS ----------

// Root endpoint
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/html/index.html');
});

app.get('/test', (req, res) => {
    res.sendFile(__dirname + '/html/template.html')
});

// ----------------------------------------





// Middleware for handling sessions with socket.io connections
io.use((socket, next) => {

    // Try to get a sessionID from the socket
    const sessionID = socket.handshake.auth.sessionID;

    // If the client already has a sessionID
    if (sessionID) {

        // Find the session
        const session = sessionStore.findSession(sessionID);

        // Assuming the sessionID is a valid one
        if (session) {

            // Update the socket to have the correct sessionID and userID
            socket.sessionID = sessionID;
            socket.userID = session.userID;

            // Move on to actual connection handling
            return next();
        }
    }

    // If the socket didn't have a sessionID (or it wasn't found in sessionStore), create new random sessionID and userID
    socket.sessionID = randomID();
    socket.userID = randomID();

    // Move on to actual connection handling
    next();
});



// Main socket.io function
io.on('connection', (socket) => {

    // Save the session in sessionStore
    sessionStore.saveSession(socket.sessionID, {
        userID: socket.userID,
        connected: true
    })

    // Send sessionID and userID to client
    socket.emit('session', {
        sessionID: socket.sessionID,
        userID: socket.userID
    });

    socket.join("waiting");



    socket.on('disconnect', (reason) => {

        sessionStore.saveSession(socket.sessionID, {
            userID: socket.userID,
            connected: false
        });

        console.log("Session disconnect: " + socket.sessionID);
    });
})



// Bind server to the specified port, and listen for incoming requests
server.listen(port, () => {
    console.log("Listening on port 9000");
});