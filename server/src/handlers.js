// Import modules
const { randomBytes } = require('crypto');
const sanitizeHtml = require('sanitize-html');
const { sessionStore, gameStore, quickPlayQueue, privateGameTable } = require('./objects.js');
const game = require('./game');

// Used for generating random gameIDs
const randomID = () => randomBytes(16).toString('hex');

// Function that takes necessary variables and starts a game
const startGame = (gameID, session, sessionID, opponentSession, opponentSessionID, io) => {

    // Create new game, choosing blue/red player based on coin flip
    const coin = Math.random() < 0.5;
    const gameData = game.newGame((coin ? sessionID : opponentSessionID), (!coin ? sessionID : opponentSessionID));

    // Set the color field for both sessions
    session.color = (coin ? 'blue' : 'red');
    opponentSession.color = (!coin ? 'blue' : 'red');

    // Store new game in gameStore
    gameStore.set(gameID, gameData);

    // Update both sessions with new gameID and status
    Object.assign(opponentSession, { gameID, stat: 'game' });
    Object.assign(session, { gameID, stat: 'game' });

    // Send the 'game-start' response to both clients
    io.to(opponentSessionID).emit('game-start');
    io.to(sessionID).emit('game-start');
};

// Quick-Play handler
const handleQuickPlay = (sessionID, session, io) => {

    // If the quick-play queue is empty
    if (quickPlayQueue.length === 0) {

        // Add the client to the queue
        quickPlayQueue.push(sessionID);

        // Set the client status accordingly
        session.stat = 'quick-play';

    // If the quick-play queue is not empty
    } else {

        // Retrieve opponent's sessionID and session
        const opponentSessionID = quickPlayQueue.shift();
        const opponentSession = sessionStore.get(opponentSessionID);

        // If opponent session doesn't exist, just re-call this handler function
        if (!opponentSession)
            handleQuickPlay(sessionID, session, io);
        
        // Otherwise, join the two clients in a game
        else {

            // Generate a random ID for the game
            const gameID = randomID();

            // Start the game
            startGame(gameID, session, sessionID, opponentSession, opponentSessionID, io);
        }
    }
};

// Create Game handler
const handleCreateGame = (sessionID, session, io) => {

    // Generate a random ID for the game
    const gameID = randomID();

    // Store the waiting private game in privateGameTable
    privateGameTable.set(gameID, sessionID);

    // Set status to 'private-play'
    session.stat = 'private-play';

    // Send the gameID to the client
    io.to(sessionID).emit('game-id', gameID);
};

// Join Game handler
const handleJoinGame = (gameID, sessionID, session, io) => {

    // Attempt to find an entry in privateGameTable
    const opponentSessionID = privateGameTable.get(gameID);

    // If no entry was found, send an invalid game error to client
    if (!opponentSessionID)
        io.to(sessionID).emit('invalid-game');
    
    // Otherwise, attempt to join the two clients in a game
    else {

        // Delete the entry from privateGameTable to prevent any other clients from joining game
        privateGameTable.delete(gameID);

        // Retrieve the opponent's session
        const opponentSession = sessionStore.get(opponentSessionID);

        // If opponent session doesn't exist, terminate function
        if (!opponentSession) return;

        // Start the game
        startGame(gameID, session, sessionID, opponentSession, opponentSessionID, io);
    }
};

// DEVELOPER TOOL
const handleDevPlay = (sessionID, session, io) => {
    startGame(randomID(), session, sessionID, session, sessionID, io);
}

// Send Message handler
const handleMessageSend = (message, gameData, session, io) => {

    // Make sure the message is a string under 500 characters
    if (typeof message !== 'string') return;
    if (message.length > 500) return;

    // Sanitize message to eliminate XSS
    message = sanitizeHtml(message, { allowedTags: [], allowedAttributes: {} });

    // Define message object to be added to message log
    const messageObject = {from: session.color, data: message};

    // Append the message to the log
    gameData.messages.push(messageObject);

    // Send the message to both clients
    io.to(session.gameID).emit('new-message', messageObject);
};

// Action handler
const handleAction = ({ type, action }, gameData, sessionID, session, io) => {

    // Make sure it's the client's turn
    if (gameData.gameState.turnPlayer !== session.color) return;

    // Check the type of action
    switch (type) {

        // Move action
        case 'move':
            game.moveAction(gameData, action);
            break;

        // CHMR action
        case 'chmr':
            game.CHMRAction(gameData, action);
            break;

        // Humanitarian Aid action
        case 'humanitarian-aid':
            game.humanitarianAidAction(gameData, action);
            break;

        // Surge action
        case 'surge':
            game.surgeAction(gameData, action);
            break;

        // Influence Operation action
        case 'influence-operation':
            game.influenceOperationAction(gameData, action);
            break;

        // Artillery Fire action
        case 'artillery-fire':
            game.artilleryFireAction(gameData, action);
            break;

        // Air Strike action
        case 'air-strike':
            game.airStrikeAction(gameData, action);
            break;
    }
};

// End Turn handler
const handleEndTurn = (socket, io) => {

};

// Disconnect handler
const handleDisconnect = (sessionID, session, io) => {

    // Immediately mark session as disconnected, but don't delete
    session.connected = false;

    // Set a timer to check disconnection after 5 seconds
    setTimeout(() => {

        // If the session is still disconnected, then the client is actually disconnected
        if (!session.connected) {

            // If the client is in a game
            if (session.stat === 'game') {

                // Attempt to retrieve the game
                const gameData = gameStore.get(session.gameID);

                // If the game exists, send disconnect message and mark the game as over
                if (gameData) {

                    // Define message object to be added to message log
                    const messageObject = {from: 'server', data: "Opponent has disconnected"};

                    // Append the message to the log
                    gameData.messages.push(messageObject);

                    // Send the message to both clients
                    io.to(session.gameID).emit('new-message', messageObject);

                    // Mark the game as over
                    gameData.inPlay = false;
                }
            }

            // Delete the session from sessionStore
            sessionStore.delete(sessionID);
        }
    }, 5000);
};

// Set up export to be used in server.js
module.exports = {
    handleQuickPlay,
    handleCreateGame,
    handleJoinGame,
    handleDevPlay,
    handleMessageSend,
    handleAction,
    handleEndTurn,
    handleDisconnect
};