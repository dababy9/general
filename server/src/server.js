// Load environment variables from .env file
require('dotenv').config();

// Constants pertinent to server functionality
const PORT = process.env.PORT || 9000;
const REDIS_HOST = process.env.REDIS_HOST || 'localhost';
const REDIS_PORT = process.env.REDIS_PORT || 6379;

// Import modules
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const path = require('path');
const { randomBytes } = require('crypto');
const gameState = require('./gameState');
const RedisInterface = require('./redisInterface');
const Handler = require('./handlers')

// Used for generating random sessionIDs
const randomID = () => randomBytes(16).toString('hex');

// Defining an initial session
const initialSession = {
    stat: 'base',
    connected: true,
    gameID: ''
}

// Initialize Redis interfaces using environment variables
const sessionStore = new RedisInterface(REDIS_HOST, REDIS_PORT);
const queueManager = new RedisInterface(REDIS_HOST, REDIS_PORT);

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
io.use(async (socket, next) => {

    // Try to get a sessionID from the socket
    const sessionID = socket.handshake.auth.sessionID;

    // If the client already has a sessionID
    if (sessionID) {

        // Find the session
        const session = await sessionStore.get(sessionID);

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
io.on('connection', async (socket) => {

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

        // Save new session in sessionStore
        await sessionStore.set(socket.sessionID, initialSession);

        // Copy properties from the new session to the socket
        Object.assign(socket, initialSession);

    // Otherwise, just update existing session to reflect (re)connection status
    } else {

        console.log("Existing session reconnected: " + socket.sessionID);

        await sessionStore.updateField(socket.sessionID, 'connected', true);
    }

    // ----------------------------------------------



    // ---------- SOCKET EVENT HANDLERS ----------

    // Quick-play event
    // This event is fired when the client attempts to join the quick-play queue
    // Will automatically start a game when two players are in the quick-play queue
    socket.on('quick-play', () => Handler.handleQuickPlay(socket, sessionStore, queueManager, gameState, io));

    // Fetch event
    // This event is fired when the client attempts to fetch an entire resource (game state, message log)
    // Mainly for reconnecting to a game, or a corrupted/malformed client-side game state
    socket.on('fetch', async (resource) => {
        switch (resource) {
            case 'messages':
                await Handler.handleMessagesFetch(socket, gameState);
                break;
            case 'game-state':
                await Handler.handleGameStateFetch(socket, gameState);
                break;
            default:
                socket.emit('bad-request');
        }
    });

    // Send event
    // This event is fired when the client attempts to send a message in game
    // Will update the message log and send the message to both players
    socket.on('send-message', (message) => Handler.handleMessage(message, socket, gameState, io));

    // Disconnect event
    // This event is fired as soon as the socket closes connection
    // Happens even with just a simple page load/reload, so we use sessions to differentiate between reload and actual disconnection
    socket.on('disconnect', (reason) => Handler.handleDisconnect(socket, sessionStore, reason));

    // Default event
    // This event is fired when the client sends an unrecognized message
    // Will respond to the client with an error
    //socket.onAny((event) => {
        //console.log("Unrecognized message: " + event);
        //socket.emit('unrecognized-request');
    //});

    // -------------------------------------------
});



// Bind server to the specified port, and listen for incoming requests
server.listen(PORT, () => {
    console.log("Listening on port 9000");
});



// ---------- SHUTDOWN CODE ----------

// Function to clean up before shutdown
const shutdown = async () => {
    console.log("\nShutting down...");

    // Attempt to close Redis clients
    try {
        sessionStore.close();
        queueManager.close();
        gameState.close();

        console.log("Redis clients closed successfully.");
    
    // If there was an error, print accordingly
    } catch (err) {
        console.error("Error while closing Redis clients:", err);
    
    // Exit the application
    } finally {
        process.exit(0);
    }
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