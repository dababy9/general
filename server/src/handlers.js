// Import modules
const { sessionStore, gameStore, quickPlayQueue } = require('./objects.js');
const game = require('./game');

// Define a method to send a message to the current game
// Involves both updating the message log in the game state and sending a socket.io message to both players
const sendMessage = (io, gameID, from, data) => {

    // Attempt to retrieve the game
    const gameData = gameStore.get(gameID);

    // If the game does not exist, return false
    if (!gameData) return false;

    // Define message object to be added to message log
    const messageObject = {from: from, data: data};

    // Append the message to the log
    gameData.messages.push(messageObject);

    // Send the message to both clients
    io.to(gameID).emit('new-message', messageObject);

    // Return true to indicate a successful message send
    return true;
}

// Quick-play handler
const handleQuickPlay = (socket, randomID, io) => {

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
const handleMessage = (message, socket, io) => {

    // Check if the message is invalid or inappropriate, and if so, send a bad message error to client
    if (0 == 1) {
        socket.emit('bad-message');
        return;
    }

    // Attempt to send the message and give a status error if it failed
    if (!sendMessage(io, socket.gameID, socket.color, message))
        socket.emit('status-error');
}

// Disconnect handler
const handleDisconnect = (socket, io) => {

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

                // Send the disconnect message
                sendMessage(io, session.gameID, 'server', "Opponent has disconnected");
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