// Quick-play handler
const handleQuickPlay = async (socket, sessionStore, queueManager, gameState, io) => {

    // Set status to 'quick-play'
    socket.stat = 'quick-play';
    sessionStore.updateField(socket.sessionID, 'stat', socket.stat);

    // Attempt to retrieve a player from the quick-play queue
    const opponentSessionID = await queueManager.poll('quickPlayQueue');

    // If a player is in the queue, join them in a game
    if (opponentSessionID) {

        // Create a new game, which will return a random gameID.
        const gameID = await gameState.createGame(opponentSessionID, socket.sessionID);

        // Retrieve both players' session data
        const [opponentSession, currentSession] = await Promise.all([
            sessionStore.get(opponentSessionID),
            sessionStore.get(socket.sessionID)
        ]);

        // If opponent session doesn't exist, just add the current player to the queue
        if (!opponentSession) {
            await queueManager.append('quickPlayQueue', socket.sessionID);
            return;
        }

        // Update both sessions with new gameID and status
        opponentSession.gameID = gameID;
        opponentSession.stat = 'game';
        currentSession.gameID = gameID;
        currentSession.stat = 'game';

        // Save updated sessions back to the database
        await Promise.all([
            sessionStore.set(opponentSessionID, opponentSession),
            sessionStore.set(socket.sessionID, currentSession)
        ]);

        // Send the 'game-start' response to both players
        io.to(opponentSessionID).emit('game-start');
        socket.emit('game-start');

    // Otherwise, add this player to the quick-play queue
    } else
        await queueManager.append('quickPlayQueue', socket.sessionID);
}

// Fetch messages handler
const handleMessagesFetch = async (socket, gameState) => {

    // Get the game data from the database
    const messages = await gameState.getMessages(socket.gameID);

    // Send the responses to the client
    socket.emit('message-log', messages);
}

// Fetch game state handler
const handleGameStateFetch = async (socket, gameState) => {

    // Get the game data from the database
    const game = await gameState.getGame(socket.gameID);
    
    // Send the responses to the client
    socket.emit('game-state', game);
}

// Message send update
const handleMessage = async (message, socket, gameState, io) => {

    // Attempt to add message to game log
    if (gameState.newMessage(socket.gameID, socket.sessionID, message)) {

        // If successful, send response to both clients
        io.to(socket.gameID).emit('new-message', {from: socket.sessionID, data: message});

    // Otherwise, send a bad-message response to client
    } else
        socket.emit('bad-message');
}

// Move handler
const handleMove = async (socket, gameState) => {

}

// CHMR handler
const handleCHMR = async (socket, gameState) => {

}

// Humanitarian Aid handler
const handleHumanitarianAid = async (socket, gameState) => {

}

// Surge handler
const handleSurge = async (socket, gameState) => {

}

// Influence Operation handler
const handleInfluenceOperation = async (socket, gameState) => {

}

// Artillery Fires handler
const handleArtilleryFires = async (socket, gameState) => {

}

// Air Strike handler
const handleAirStrike = async (socket, gameState) => {

}

// End Turn handler
const handleEndTurn = async (socket, gameState, io) => {

}

// Disconnect handler
const handleDisconnect = async (socket, sessionStore, gameState, io) => {

    // Immediately mark session as disconnected, but don't delete
    await sessionStore.updateField(socket.sessionID, 'connected', false);

    // Set a timer to check disconnection after 5 seconds
    setTimeout(async () => {

        // Retrieve session
        const session = await sessionStore.get(socket.sessionID);

        // If the session still exists and it is still disconnected, then the client is actually disconnected
        if (session && !session.connected) {
            console.log("Deleting session with id: " + socket.sessionID);

            // If the client was in a game, send a disconnect message to their opponent
            if (session.stat == 'game' && gameState.newMessage(session.gameID, '', "Opponent has left the game"))

                // Important to use the 'session' object here rather than the 'socket' object
                // Sometimes the 'socket' object does not have a 'gameID' field after the setTimeout callback is executed
                io.to(session.gameID).emit('new-message', {from: '', data: "Opponent has left the game"});

            // Delete the session from the database
            await sessionStore.del(socket.sessionID);
        }
    }, 5000);
}

// Set up export to be used in server.js
module.exports = {
    handleQuickPlay,
    handleMessagesFetch,
    handleGameStateFetch,
    handleMessage,
    handleMove,
    handleCHMR,
    handleHumanitarianAid,
    handleSurge,
    handleInfluenceOperation,
    handleArtilleryFires,
    handleAirStrike,
    handleEndTurn,
    handleDisconnect
};