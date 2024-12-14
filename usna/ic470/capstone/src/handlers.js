// Quick-play handler
const handleQuickPlay = async (socket, sessionStore, queueManager, gameState, io) => {

    // Check the client's status: if they are not in the 'base' status, then send a status error to client
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

        // Retrieve both players' session data
        const [opponentSession, currentSession] = await Promise.all([
            sessionStore.get(opponentSessionID),
            sessionStore.get(socket.sessionID)
        ]);

        // If either session doesn't exist in database, just return
        if (!opponentSession || !currentSession)
            return;

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

        // Send the 'game-start' message to both players
        io.to(opponentSessionID).emit('game-start');
        socket.emit('game-start');

    // Otherwise, add this player to the quick-play queue
    } else
        await queueManager.append('quickPlayQueue', socket.sessionID);
}

// Fetch messages handler
const handleMessagesFetch = async (socket, gameState) => {

    // Check the client's status: if they are not in the 'game' status, then send a status error to client
    if (socket.stat !== 'game') {
        socket.emit('statusError');
        return;
    }

    // Get the game data from the database
    const game = await gameState.getGame(socket.gameID);

    // Send the messages to the client
    socket.emit('message-log', game.messages);
}

// Fetch game state handler
const handleGameStateFetch = async (socket, gameState) => {

    // Check the client's status: if they are not in the 'game' status, then send a status error to client
    if (socket.stat !== 'game') {
        socket.emit('statusError');
        return;
    }

    // Get the game data from the database
    const game = await gameState.getGame(socket.gameID);

    // Delete the message field from the game data (client requests messages through a different handler)
    delete game.messages;
    
    // Send the messages to the client
    socket.emit('game-state', game.messages);
}

// Disconnect handler
const handleDisconnect = async (socket, sessionStore) => {

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
}

module.exports = {
    handleQuickPlay,
    handleMessagesFetch,
    handleGameStateFetch,
    handleDisconnect
};