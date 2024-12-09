// Constants pertinent to server functionality
const PORT = process.env.PORT || 9000;

// Import modules
const express = require('express');
const { createServer } = require('http');
const { Server } = require('socket.io');
const path = require('path');
const { randomBytes } = require('crypto');
const gameState = require('./gameState');
const RedisInterface = require('./RedisInterface')

// Used for managing sessions
const randomID = () => randomBytes(16).toString("hex");
const sessionStore = new RedisInterface();

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
    res.sendFile(path.resolve('html/index.html'));
});

app.get('/test', (req, res) => {
    res.sendFile(path.resolve('html/template.html'));
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

            // Update the socket to have the correct sessionID and userID
            socket.sessionID = sessionID;
            socket.userID = session.userID;

            // Client is already part of a session
            socket.isNewSession = false;
            socket.stat = session.stat;

            // Move on to actual connection handling
            return next();
        }
    }

    // If the socket didn't have a sessionID (or it wasn't found in sessionStore), create new random sessionID and userID
    socket.sessionID = randomID();
    socket.userID = randomID();

    // Client is not already part of a session
    socket.isNewSession = true;
    socket.stat = 'new';

    // Move on to actual connection handling
    next();
});



// Main socket.io function
io.on('connection', async (socket) => {

    // ---------- ONLY RUN ONCE ON CONNECT ----------

    // Send sessionID and userID to client
    socket.emit('session', {
        sessionID: socket.sessionID,
        userID: socket.userID
    });

    // If the client didn't already have a session, save the new session and join them to 'new' room
    if (socket.isNewSession) {

        console.log("New session connected: " + socket.sessionID);

        // Save the session in sessionStore
        await sessionStore.set(socket.sessionID, {
            userID: socket.userID,
            connected: true,
            stat: 'new'
        })

        // Put new client in 'new' room
        socket.join("new");

    // Otherwise, just update existing session to reflect (re)connection status
    } else {

        console.log("Existing session reconnected: " + socket.sessionID);

        await sessionStore.updateField(socket.sessionID, 'connected', true)
    }

    // ----------------------------------------------



    // ---------- SOCKET EVENT HANDLERS ----------

    socket.on('trial', async () => {
        socket.emit('statusError');
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