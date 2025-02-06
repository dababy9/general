// Load environment variables from .env file
require('dotenv').config();

// Constants pertinent to server functionality
const PORT = process.env.PORT || 9000;

// Import modules
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const path = require('path');
const { randomBytes } = require('crypto');
const game = require('./game');
const handler = require('./handlers');
const { createInitialSession } = require('./objects.js');

// Used for generating random sessionIDs
const randomID = () => randomBytes(16).toString('hex');

// Initialize in-memory 'storages'
const sessionStore = new Map();
const gameStore = new Map();
const quickPlayQueue = new Array();

// Create Express app and pass it to HTTP server
const app = express();
const server = createServer(app);

// Mount the socket.io server on the HTTP server
const io = new Server(server);

// Express app now serves static files from 'static' directory
app.use(express.static(path.resolve('public/static')));



// ---------- DEFINING ENDPOINTS ----------

// Path to directory with html pages
const htmlPath = 'public/html/';

// Root endpoint
app.get('/', (req, res) => {
    res.sendFile(path.resolve(htmlPath + 'home.html'));
});

// Game endpoint
app.get('/game', (req, res) => {
    res.sendFile(path.resolve(htmlPath + 'game.html'));
});

// Status error endpoint
app.get('/status-error', (req, res) => {
    res.sendFile(path.resolve(htmlPath + 'status-error.html'));
});

// Message error endpoint
app.get('/message-error', (req, res) => {
    res.sendFile(path.resolve(htmlPath + 'message-error.html'));
});

// ----------------------------------------



// Middleware for handling sessions with socket.io connections
io.use((socket, next) => {

    // Try to get a sessionID from the socket
    const sessionID = socket.handshake.auth.sessionID;

    // If the client already has a sessionID
    if (sessionID) {

        // Find the session
        const session = sessionStore.get(sessionID);

        // Assuming the sessionID is a valid one
        if (session) {

            // Assign sessionID to socket
            socket.sessionID = sessionID;

            // Copy the rest of the properties from 'session' object to the socket
            Object.assign(socket, session);

            // Indicate that this socket belongs to an existing session
            socket.isNewSession = false;

            // Move on to actual connection handling
            return next();
        }
    }

    // If the socket didn't have a sessionID (or it wasn't found in sessionStore), create new random sessionID
    socket.sessionID = randomID();

    // Indicate that this socket belongs to a new session
    socket.isNewSession = true;

    // Move on to actual connection handling
    next();
});



// Main socket.io function
io.on('connection', (socket) => {

    // ---------- ONLY RUN ONCE ON CONNECT ----------

    // Send sessionID to client
    socket.emit('session', socket.sessionID);

    // Leave the default room (socket.id) and join room identified by the sessionID instead
    socket.leave(socket.id);
    socket.join(socket.sessionID);

    // If the client is in a game, join the room associated with the gameID
    if (socket.stat === 'game' && socket.gameID)
        socket.join(socket.gameID)

    // If the client didn't already have a session, save the new session and join them to 'new' room
    if (socket.isNewSession) {

        console.log("New session connected: " + socket.sessionID);

        // Create a new session
        const newSession = createInitialSession();

        // Save new session in sessionStore
        sessionStore.set(socket.sessionID, newSession);

        // Copy properties from the new session to the socket
        Object.assign(socket, newSession);

    // Otherwise, just update existing session to reflect (re)connection status
    } else {

        console.log("Existing session reconnected: " + socket.sessionID);

        // Set the existing session back to 'connected'
        sessionStore.get(socket.sessionID).connected = true;
    }

    // ----------------------------------------------



    // ---------- SOCKET EVENT HANDLERS ----------

    // Quick-play event
    // This event is fired when the client attempts to join the quick-play queue
    // Will automatically start a game when two players are in the quick-play queue
    socket.on('quick-play', () => {

        // Check the client's status: if they are not in the 'base' status, then send a status error to client
        if (socket.stat !== 'base') {
            socket.emit('status-error');
            return;
        }

        // Handle the event
        handler.handleQuickPlay(socket, sessionStore, gameStore, quickPlayQueue, game, randomID(), io);
    });

    // Fetch event
    // This event is fired when the client attempts to fetch an entire resource (game state, message log)
    // Mainly for reconnecting to a game, or a corrupted/malformed client-side game state
    socket.on('fetch', (resource) => {

        // Check the client's status: if they are not in the 'game' status, then send a status error to client
        if (socket.stat !== 'game') {
            socket.emit('status-error');
            return;
        }

        // Handle the event (or respond with an error)
        switch (resource) {
            case 'messages':
                socket.emit('message-log', gameStore.get(socket.gameID).messages);
                break;
            case 'game-state':
                socket.emit('message-log', gameStore.get(socket.gameID).gameState);
                break;
            default:
                socket.emit('bad-request');
        }
    });

    // Send message event
    // This event is fired when the client attempts to send a message in game
    // Will update the message log and send the message to both players
    socket.on('send-message', (message) => {

        // Check the client's status: if they are not in the 'game' status, then send a status error to client
        if (socket.stat !== 'game') {
            socket.emit('status-error');
            return;
        }

        // Handle the event
        handler.handleMessage(message, socket, gameStore, io);
    });

    // Action event
    // This event is fired when the client attempts to take an action in a game
    socket.on('action', (type) => {

        // Check the client's status: if they are not in the 'game' status, then send a status error to client
        if (socket.stat !== 'game') {
            socket.emit('status-error');
            return;
        }

        // Handle the event (or respond with an error)
        // TODO ----------------------------------------------------------------------------------------------------------------------------------------------------------
    });

    // End Turn event
    // This event is fired when the client ends their turn
    //socket.on('end-turn', () => Handler.handleEndTurn(socket, gameStore, io));

    // Disconnect event
    // This event is fired as soon as the socket closes connection
    // Happens even with just a simple page load/reload, so we use sessions to differentiate between reload and actual disconnection
    socket.on('disconnect', (reason) => handler.handleDisconnect(socket, sessionStore, gameStore, io));

    // -------------------------------------------
});



// Bind server to the specified port, and listen for incoming requests
server.listen(PORT, () => {
    console.log("Listening on port " + PORT);
});



// ---------- SHUTDOWN CODE ----------

// Function to exit gracefully before shutdown
const shutdown = async () => {
    console.log("\nShutting down...");
    process.exit(0);
}

// Handle process signals
process.on('SIGINT', shutdown);
process.on('SIGTERM', shutdown);

// Handle uncaught exceptions
process.on('uncaughtException', async (err) => {
    console.error("Uncaught exception:", err);
    await shutdown();
});

// Handle unhandled rejections
process.on('unhandledRejection', async (reason, promise) => {
    console.error("Unhandled promise rejection:", reason);
    await shutdown();
});

// -----------------------------------