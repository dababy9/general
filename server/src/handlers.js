// Quick-play handler
const handleQuickPlay = (socket, sessionStore, gameStore, quickPlayQueue, game, randomID, io) => {

    // Set status to 'quick-play'
    sessionStore.get(socket.sessionID).stat = socket.stat = 'quick-play';

    // Attempt to retrieve a player from the quick-play queue
    const opponentSessionID = quickPlayQueue.shift();

    // If a player is in the queue, join them in a game
    if (opponentSessionID) {

        // Retrieve opponent's session
        opponentSession = sessionStore.get(opponentSessionID);

        // If opponent session doesn't exist, just add the current player to the queue
        if (!opponentSession) {
            quickPlayQueue.push(socket.sessionID);
            return;
        }

        // Retrieve the current session
        currentSession = sessionStore.get(socket.sessionID);

        // Generate a random ID for the game
        const gameID = randomID;

        // Store the new game in gameStore
        gameStore.set(gameID, game.newGame(opponentSessionID, socket.sessionID));

        // Update both sessions with new gameID and status
        opponentSession.gameID = gameID;
        opponentSession.stat = 'game';
        currentSession.gameID = gameID;
        currentSession.stat = 'game';

        // Send the 'game-start' response to both players
        io.to(opponentSessionID).emit('game-start');
        socket.emit('game-start');

    // Otherwise, add this player to the quick-play queue
    } else
        quickPlayQueue.push(socket.sessionID);
}

// Message send update
const handleMessage = (message, socket, gameStore, io) => {

    // Attempt to retrieve the game
    const gameData = gameStore.get(socket.gameID);

    // If the game does not exist, send a status error to the client
    if (!gameData){
        socket.emit('status-error');
        return;
    }

    // Check if the message is invalid or inappropriate, and if so, send a bad message error to client
    if (0 == 1) {
        socket.emit('bad-message');
        return;
    }

    // Define message object to be added to message log
    const messageObject = {from: (socket.sessionID == gameData.blueSessionID) ? 'blue' : 'red', data: message};

    // Append the message to the log
    gameData.messages.push(messageObject);

    // Send the message to both clients
    io.to(socket.gameID).emit('new-message', messageObject);
}

// Disconnect handler
const handleDisconnect = (socket, sessionStore, gameStore, io) => {

    // Immediately mark session as disconnected, but don't delete
    sessionStore.get(socket.sessionID).connected = false;

    // Set a timer to check disconnection after 5 seconds
    setTimeout(() => {

        // Retrieve session
        const session = sessionStore.get(socket.sessionID);

        // If the session still exists and it is still disconnected, then the client is actually disconnected
        if (session && !session.connected) {
            console.log("Deleting session with id: " + socket.sessionID);

            // Check if the client is in 'game' status
            if (session.stat == 'game'){

                // Attempt to retrieve the game they are in
                const gameData = gameStore.get(session.gameID);
                
                // If the game exists, append disconnect message to the log and send to opponent
                if (gameData) {
                    const messageObject = {from: 'server', data: "Opponent has disconnected"};
                    gameData.messages.push(messageObject);
                    io.to(session.gameID).emit('new-message', messageObject);
                }
            }

            // Delete the session from sessionStore
            sessionStore.delete(socket.sessionID);
        }
    }, 5000);
}

// Set up export to be used in server.js
module.exports = {
    handleQuickPlay,
    handleMessage,
    handleDisconnect
};