// Import modules
const { randomBytes } = require('crypto');
const sanitizeHtml = require('sanitize-html');
const { sessionStore, gameStore, quickPlayQueue, privateGameTable } = require('./objects.js');
const game = require('./game');

// Used for generating random gameIDs
const randomID = () => randomBytes(3).toString('hex');

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

        // Set client status to 'quick-play'
        session.stat = 'quick-play';

    // If the quick-play queue is not empty
    } else {

        // Retrieve opponent's sessionID and session
        const opponentSessionID = quickPlayQueue.shift();
        const opponentSession = sessionStore.get(opponentSessionID);

        // If opponent session doesn't exist, just re-call this handler function
        if (!opponentSession) {
            handleQuickPlay(sessionID, session, io);
            return;
        }

        // Generate a random ID for the game
        const gameID = randomID();

        // Start the game
        startGame(gameID, session, sessionID, opponentSession, opponentSessionID, io);
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
    if (!opponentSessionID) {
        io.to(sessionID).emit('error', 'invalid-game');
        return;
    }

    // Delete the entry from privateGameTable to prevent any other clients from joining game
    privateGameTable.delete(gameID);

    // Retrieve the opponent's session
    const opponentSession = sessionStore.get(opponentSessionID);

    // If opponent session doesn't exist, send an invalid game error to client
    if (!opponentSession) {
        io.to(sessionID).emit('error', 'invalid-game');
        return;
    }

    // Start the game
    startGame(gameID, session, sessionID, opponentSession, opponentSessionID, io);
};

// DEVELOPER TOOL
const handleDevPlay = (sessionID, session, io) => {
    startGame(randomID(), session, sessionID, session, sessionID, io);
};

// Send Message handler
const handleMessageSend = (message, gameData, sessionID, session, io) => {

    // If the message isn't a string, send a message type error to client
    if (typeof message !== 'string'){
        io.to(sessionID).emit('message-type-error');
        return;
    }

    // If the message is longer than 500 characters, send a message length error to client
    if (message.length > 500){
        io.to(sessionID).emit('message-length-error');
        return;
    }

    // Sanitize message to eliminate XSS
    message = sanitizeHtml(message, { allowedTags: [], allowedAttributes: {} });

    // Define message object to be added to message log
    const messageObject = {from: session.color, data: message};

    // Append the message to the log
    gameData.messages.push(messageObject);

    // Send the message to both clients
    io.to(session.gameID).emit('new-message', JSON.stringify(messageObject));
};

// Initiative handler
const handleInitiative = (gameData, session, io) => {

    // Make sure that there isn't a turn player
    if (gameData.gameState.turnPlayer) return;

    // Update the initiative flag corresponding to the client's color
    if (session.color === 'blue')
        gameData.blueInitiative = true;
    else
        gameData.redInitiative = true;

    // If both flags are true, then conduct a roll for initiative
    if (gameData.blueInitiative && gameData.redInitiative) {

        // Reset flags
        gameData.blueInitiative = gameData.redInitiative = false;

        // Get roll results: { winner, blueRoll, redRoll }
        let result = game.initiativeRoll();

        // Set the turn player based on results
        gameData.gameState.turnPlayer = result.winner;

        // Send results to clients
        io.to(session.gameID).emit('initiative-result', JSON.stringify(result));
    }
}

// Action handler
const handleAction = ({ type, data }, gameData, sessionID, session, io) => {

    // Make sure it's the client's turn
    if (gameData.gameState.turnPlayer !== session.color) return;

    // Retrieve game status
    const status = gameData.status;

    // Used to store result of action processing
    let result = null;

    // Check the type of action
    switch (type) {

        // Move selection action
        case 'move-select':
            if (status === 'default') result = game.moveSelectAction(gameData, data, session.color);
            break;

        // Move confirmation action
        case 'move-confirm':
            if (status === 'move') result = game.moveConfirmAction(gameData, data, session.color);
            break;
            
        // CHMR action
        case 'chmr':
            result = game.CHMRAction(gameData, action);
            break;

        // Humanitarian Aid action
        case 'humanitarian-aid':
            if (status === 'default') result = game.humanitarianAidAction(gameData, session.color);
            break;

        // Surge action
        case 'surge':
            if (status === 'default') result = game.surgeAction(gameData, session.color);
            break;

        // Influence Operation action
        case 'influence-operation':
            if (status === 'default') result = game.influenceOperationAction(gameData, session.color);
            break;

        // Artillery Fire action
        case 'artillery-fire':
            result = game.artilleryFireAction(gameData, action);
            break;

        // Air Strike action
        case 'air-strike':
            result = game.airStrikeAction(gameData, action);
            break;
    }

    // If the result is undefined, just return and do nothing
    if (!result) return;

    // If the result was an error, send it to the client
    if (result.type === 'error')
        io.to(sessionID).emit('error', result.error);

    // Otherwise, send the result to one or both clients (depending on the 'to' field)
    else {
        if (result.to === 'client') io.to(sessionID).emit(result.type, JSON.stringify(result.data));
        else io.to(session.gameID).emit(result.type, JSON.stringify(result.data));
    }
};

// End Turn handler
const handleEndTurn = (gameState, session, io) => {

    // Make sure it's the client's turn
    if (gameState.turnPlayer !== session.color) return;

    // Increment turn counter
    gameState.turnCounter++;

    // If turn counter is even, players must roll for initiative
    if (gameState.turnCounter % 2 === 0) {

        // Set the turn player to empty
        gameState.turnPlayer = '';

        // Indicate to both clients that they need to roll for initiative
        io.to(session.gameID).emit('initiative-ready');

    // Otherwise, simply switch the turn player to the opposite color and notify both clients
    } else
        gameState.turnPlayer = (gameState.turnPlayer === 'blue' ? 'red' : 'blue');
        io.to(session.gameID).emit('new-turn', gameState.turnPlayer);
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
    handleInitiative,
    handleAction,
    handleEndTurn,
    handleDisconnect
};