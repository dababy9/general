// Constants pertinent to server functionality
const PORT = process.argv[2] || 9000;

// Import modules
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const path = require('path');
const { randomBytes } = require('crypto');
const handler = require('./handlers');
const { sessionStore, gameStore, quickPlayQueue, privateGameTable, createInitialSession } = require('./objects.js');

// Used for generating random sessionIDs
const randomID = () => randomBytes(16).toString('hex');

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

// ----------------------------------------



// Middleware for handling sessions with socket.io connections
io.use((socket, next) => {

    // Attempt to retrieve a previous session from the socket's handshake object
    let sessionID = socket.handshake.auth.sessionID;
    let session = sessionStore.get(sessionID);

    // If the client already has a session
    if (session) {

        // Mark the session as 'connected'
        session.connected = true;

        // If the client is in a game, join the room associated with the gameID
        if (session.stat === 'game')
            socket.join(session.gameID)

    // If the client does not already have a session
    } else {

        // Generate new session info
        sessionID = randomID();
        session = createInitialSession();

        // Store the new session in sessionStore
        sessionStore.set(sessionID, session);
    }

    // Assign session info to the socket
    socket.data.sessionID = sessionID;
    socket.data.session = session;

    // Send the sessionID to the client
    socket.emit('session', sessionID);

    // Leave the default room (socket.id) and join room identified by the sessionID instead
    socket.leave(socket.id);
    socket.join(sessionID);

    // Move on to main socket.io function
    next();
});



// Main socket.io function with all socket.io handlers
io.on('connection', (socket) => {

    // Immediately grab the sessionID and session from the socket's data field
    const sessionID = socket.data.sessionID;
    const session = socket.data.session;

    // General request for client to enter a game
    socket.on('play', (type, gameID = null) => {

        // Make sure the client is in the 'base' status
        if (session.stat !== 'base') return;

        // Check the type of request
        switch (type) {

            // Client requests to join the quick-play queue
            case 'quick':
                handler.handleQuickPlay(sessionID, session, io);
                break;

            // Client requests to create a private game
            case 'create':
                handler.handleCreateGame(sessionID, session, io);
                break;

            // Client requests to join a private game
            case 'join':
                handler.handleJoinGame(gameID, sessionID, session, io);
                break;

            // DEVELOPER TOOL
            case 'dev':
                handler.handleDevPlay(sessionID, session, io);
                break;
        }
    });

    // General request for client to exit matchmaking
    socket.on('leave', (type) => {

        // Client attempts to leave the quick-play queue
        if (type === 'quick' && session.stat === 'quick-play')
            handler.handleLeaveQuickPlay(sessionID);

        // Client attempts to leave private game (before another client joins it)
        if (type === 'private' && session.stat === 'private-play' && session.gameID)
            privateGameTable.delete(session.gameID);
    });

    // General request for client to do something while in a game
    socket.on('game', (type, arg = null) => {

        // Make sure the client is in the 'game' status
        if (session.stat !== 'game') return;

        // Attempt to retrieve the game
        const gameData = gameStore.get(session.gameID);

        // Make sure the client is actually in a game
        if (!gameData) return;

        // Check the type of request
        switch (type) {

            // Client requests to fetch the message log
            case 'fetch-message-log':
                socket.emit('message-log', JSON.stringify(gameData.messages));
                break;

            // Client requests to fetch the game state
            case 'fetch-game-state':
                socket.emit('game-state', JSON.stringify(gameData.gameState));
                break;

            // Client requests to send a message in-game (in this case, 'arg' is the message)
            case 'send-message':
                handler.handleMessageSend(arg, gameData, session, io);
                break;

            // Client requests to roll for initiative
            case 'initiative':
                handler.handleInitiative(gameData, session, io);
                break;

            // Client requests to take an action in-game (in this case, 'arg' is an object with the type of action and the actual action)
            case 'action':
                handler.handleAction(arg, gameData, session, io);
                break;

            // Client requests to end their turn
            case 'end-turn':
                handler.handleEndTurn(gameData.gameState, session, io);
                break;
        }
    });

    // Disconnect event
    // This event is fired as soon as the socket closes connection
    // Happens even with a page load/reload, so we use sessions to differentiate between reload and actual disconnection
    socket.on('disconnect', () => handler.handleDisconnect(sessionID, session, io));
});



// Bind server to the specified port, and listen for incoming requests
server.listen(PORT, () => {
    console.log("Listening on port " + PORT);
});



// ---------- SHUTDOWN CODE ----------

// Function to exit gracefully before shutdown
const shutdown = async () => {
    console.log("\n\nShutting down...\n");
    process.exit(0);
};

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