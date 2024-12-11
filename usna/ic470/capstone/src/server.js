// Constants pertinent to server functionality
// 'process.env.PORT' is only used for Docker container
const PORT = process.env.PORT || 9000;

// Import modules
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const path = require('path');
const { randomBytes } = require('crypto');
const gameState = require('./gameState');
const RedisInterface = require('./RedisInterface');

// Used for managing sessions
const randomID = () => randomBytes(16).toString('hex');
const sessionStore = new RedisInterface();

// Used for managing game lobbies
const queueManager = new RedisInterface();

// Create Express app and pass it to HTTP server
const app = express();
const server = createServer(app);

// Mount the socket.io server on the HTTP server
const io = new Server(server);

// Express app now serves static files from 'static' directory
app.use('/static', express.static(path.resolve('static')));





// ---------- DEFINING ENDPOINTS ----------

// Root endpoint
app.get('/', (req, res) => {
    res.sendFile(path.resolve('html/home.html'));
});

// Game endpoint
app.get('/game', (req, res) => {
    res.sendFile(path.resolve('html/game.html'));
});

// Error endpoint
app.get('/error', (req, res) => {
    res.sendFile(path.resolve('html/statusError.html'));
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

            // Update the socket variables
            socket.sessionID = sessionID;
            socket.isNewSession = false;
            socket.stat = session.stat;

            // Move on to actual connection handling
            return next();
        }
    }

    // If the socket didn't have a sessionID (or it wasn't found in sessionStore), create new random sessionID
    socket.sessionID = randomID();

    // Client is not already part of a session
    socket.isNewSession = true;
    socket.stat = 'new';

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

    // If the client didn't already have a session, save the new session and join them to 'new' room
    if (socket.isNewSession) {

        console.log("New session connected: " + socket.sessionID);

        // Save the session in sessionStore
        await sessionStore.set(socket.sessionID, {
            connected: true,
            stat: 'new'
        });

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
    socket.on('quick-play', async () => {

        // Check the client's status: if they are not sending this while in the 'base' status, then send a status error to client
        if (socket.stat !== 'base') {
            socket.emit('statusError');
            return;
        }

        // Set status to 'quick-play'
        socket.stat = 'quick-play';
        sessionStore.updateField(socket.sessionID, 'stat', socket.stat);

        // Attempt to retrieve a player from the quick-play queue
        const opponentSessionID = await queueManager.poll('quickPlayQueue');

        // If a player existed, join them in a game
        if (opponentSessionID) {

            // Create a new game, which will return a random gameID.
            const gameID = await gameState.createGame(opponentSessionID, socket.sessionID);
            await sessionStore.updateField(opponentSessionID, 'gameID', gameID);
            await sessionStore.updateField(socket.sessionID, 'gameID', gameID);

            // Send the 'game-start' message to both players
            io.to(opponentSessionID).emit('game-start');
            socket.emit('game-start');

        // Otherwise, add this player to the quick-play queue
        } else
            await queueManager.append('quickPlayQueue', socket.sessionID);
    });

    // Disconnect event
    // This event is fired as soon as the socket closes connection
    // Happens even with just a simple page load/reload, so we use sessions to differentiate between reload and actual disconnection
    socket.on('disconnect', async (reason) => {

        // Immediately mark session as disconnected, but don't delete
        await sessionStore.updateField(socket.sessionID, 'connected', false);

        // Set a timer to check disconnection after 5 seconds
        setTimeout(async () => {

            // Retrieve session
            const session = await sessionStore.get(socket.sessionID);

            // If the session still exists and it is still disconnected, then the client is actually disconnected
            if (session && !session.connected) {
                console.log("Deleting session with id: " + socket.sessionID);

                // Delete the session from the database
                await sessionStore.del(socket.sessionID);
            }
        }, 5000);
    });

    // -------------------------------------------
});



// Bind server to the specified port, and listen for incoming requests
server.listen(PORT, () => {
    console.log("Listening on port 9000");
});