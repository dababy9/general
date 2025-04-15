// Import modules
const { randomBytes } = require('crypto');
const sanitizeHtml = require('sanitize-html');
const { SESSION_TIMEOUT, MAX_MESSAGE_LENGTH, sessionStore, gameStore, quickPlayQueue, privateGameTable } = require('./objects.js');
const { Game } = require('./game');

// Used for generating random gameIDs
const randomID = () => randomBytes(3).toString('hex');

// Function that takes necessary variables and starts a game
const startGame = (gameID, session, sessionID, opponentSession, opponentSessionID, io) => {

    // Create new game, choosing blue/red player based on coin flip
    const coin = Math.random() < 0.5;
    const game = new Game((coin ? sessionID : opponentSessionID), (!coin ? sessionID : opponentSessionID));

    // Set the color field for both sessions
    session.color = (coin ? 'blue' : 'red');
    opponentSession.color = (!coin ? 'blue' : 'red');

    // Store new game in gameStore
    gameStore.set(gameID, game);

    // Update both sessions with new gameID and status
    opponentSession.gameID = session.gameID = gameID;
    opponentSession.stat = session.stat = 'game';

    // Send the 'game-start' response to both clients
    io.to(opponentSessionID).emit('game-start');
    io.to(sessionID).emit('game-start');
};

// Function that ends a game
const endGame = (gameID, reason, winner, io) => {

    // Send game over message to both clients
    io.to(gameID).emit('game-over', { reason, winner });

    // Erase gameID from both sessions
    const game = gameStore.get(gameID);
    sessionStore.get(game.blueSessionID).gameID = '';
    sessionStore.get(game.redSessionID).gameID = '';

    // Delete game from memory
    gameStore.delete(gameID);
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
        if (!opponentSession) return handleQuickPlay(sessionID, session, io);

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
    if (!opponentSessionID) return io.to(sessionID).emit('error', 'invalid-game');

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
const handleMessageSend = (message, game, sessionID, session, io) => {

    // If the message isn't a string, send a message type error to client
    if (typeof message !== 'string'){
        io.to(sessionID).emit('message-type-error');
        return;
    }

    // If the message is longer than a given number of characters, send a message length error to client
    if (message.length > MAX_MESSAGE_LENGTH){
        io.to(sessionID).emit('message-length-error');
        return;
    }

    // Sanitize message to eliminate XSS
    message = sanitizeHtml(message, { allowedTags: [], allowedAttributes: {} });

    // Define message object to be added to message log
    const messageObject = {from: session.color, data: message};

    // Append the message to the log
    game.messages.push(messageObject);

    // Send the message to both clients
    io.to(session.gameID).emit('new-message', JSON.stringify(messageObject));
};

// Initiative handler
const handleInitiative = (game, session, io) => {

    // Make sure that there isn't a turn player
    if (game.gameState.turnPlayer) return;

    // Update the flag corresponding to the client's color
    if (session.color === 'blue')
        game.blueFlag = true;
    else
        game.redFlag = true;

    // If both flags are true, then conduct a roll for initiative
    if (game.blueFlag && game.redFlag) {

        // Reset flags
        game.blueFlag = game.redFlag = false;

        // Get roll results: { winner, blueRoll, redRoll }
        let result = Game.initiative();

        // Set the turn player based on results
        game.gameState.turnPlayer = result.winner;

        // Set game status
        game.status = 'default';

        // Send results to clients
        io.to(session.gameID).emit('initiative-result', JSON.stringify(result));
    }
};

// Action handler
const handleAction = ({ type, data }, game, sessionID, session, io) => {

    // Make sure it's the client's turn
    if (game.gameState.turnPlayer !== session.color) return;

    // Retrieve game status
    const status = game.status;

    // Used to store result of action processing
    let result = null;

    // Check the type of action
    switch (type) {

        // Move selection action
        case 'move-select':
            if (status === 'default') result = game.moveSelectAction(data, session.color);
            break;

        // Move confirmation action
        case 'move-confirm':
            if (status === 'move') result = game.moveConfirmAction(data, session.color);
            break;
            
        // CHMR action
        case 'chmr':
            result = null; // TODO ------------------------------------------
            break;

        // Humanitarian Aid action
        case 'humanitarian-aid':
            if (status === 'default') result = game.humanitarianAidAction(session.color);
            break;

        // Surge action
        case 'surge':
            if (status === 'default') result = game.surgeAction(session.color);
            break;

        // Influence Operation action
        case 'influence-operation':
            if (status === 'default') result = game.influenceOperationAction(session.color);
            break;

        // Artillery Fire selection action
        case 'artillery-select':
            if (status === 'default') result = game.artillerySelectAction(data, session.color);
            break;

        // Artillery Fire confirmation action
        case 'artillery-confirm':
            if (status === 'artillery') result = game.artilleryConfirmAction(data, session.color);
            break;

        // Air Strike action
        case 'air-strike':
            if (status === 'default') result = game.airStrikeAction(data, session.color);
            break;
    }

    // If the result is undefined, just return and do nothing
    if (!result) return;

    // If the result was an error, send it to the client
    if (result.type === 'error') return io.to(sessionID).emit('error', result.error);

    // If the result type is empty, end game
    if (!result.type) return endGame(session.gameID, result.reason, result.winner, io);

    // Otherwise, send the result to one or both clients (depending on the 'to' field)
    if (result.to === 'client') io.to(sessionID).emit(result.type, JSON.stringify(result.data));
    else io.to(session.gameID).emit(result.type, JSON.stringify(result.data));
};

// End Turn handler
const handleEndTurn = (game, session, io) => {

    // Make sure it's the client's turn
    if (game.gameState.turnPlayer !== session.color) return;

    // Make sure the game is in default status
    if (game.status !== 'default') return;

    // Reset all values for new turn
    game.resetValues();

    // Variable to store close combat data sent to both clients
    let data = {};

    // Initiate close combat, and if it's required, add a 'next' node to data
    if (game.initiateCloseCombat()) data = { next: game.meta.combat[0] };
    
    // Send close combat message
    io.to(session.gameID).emit('close-combat', JSON.stringify(data));
};

// Close Combat handler
const handleCloseCombat = (numDice, game, session, io) => {

    // Make sure the game is in close combat status
    if (game.status !== 'closeCombat') return;

    // Get node for close combat
    const node = game.meta.combat[0];

    // Make sure the client send a valid number of dice to roll
    if (!Number.isInteger(numDice) || numDice < 0 || numDice > game.getPieces(session.color, node).length) {

        // If not, send back another close combat response
        io.to(session.gameID).emit('close-combat', JSON.stringify({ next: node }));
        return;
    }

    // Update the flag corresponding to the client's color
    if (session.color === 'blue'){
        game.blueFlag = true;
        game.meta.blueCC = numDice;
    } else {
        game.redFlag = true;
        game.meta.redCC = numDice;
    }

    // If both flags are true, then conduct close combat
    if (game.blueFlag && game.redFlag) {

        // Conduct close combat fully, and get dice roll results
        let result = game.performCloseCombat();

        // If a player ran out of armies, end the game
        if (result.winner)
            endGame(session.gameID, result.reason, result.winner, io);

        // Otherwise, send clients the next close combat response
        else
            io.to(session.gameID).emit('close-combat', JSON.stringify({ rolls: result, gameState: game.gameState, next: game.meta.combat[0] }));
    }
};

// Civilian Move handler
const handleCivMove = (game, session, io) => {

    // Make sure it's the client's turn
    if (game.gameState.turnPlayer !== session.color) return;
    
    // Make sure close combat has just ended
    if (game.status !== 'closeCombat' || game.meta.combat[0]) return;

    // Set game status for civilian return
    game.status = 'civReturn';

    // Perform civilian repopulation and save result
    const result = game.civilianPopulation();

    // Send clients the result of civilian movement
    io.to(session.gameID).emit('civ-move', JSON.stringify({ result, gameState: game.gameState }));
};

// Civilian Return from Haven handler
const handleCivReturn = (choice, game, session, io) => {

    // Make sure it's the client's turn
    if (game.gameState.turnPlayer !== session.color) return;

    // Make sure the game is in civilian return status
    if (game.status !== 'civReturn') return;

    // If choice is undefined, civilian movement has just finished
    if (!choice) {

        // If there was no CHMR performed, simply end the turn
        // TODO (add if statement) --------------------------------------------
        endTurn(game, session, io);
    
    // Otherwise, send the next haven that CHMR was performed on
    } else {

    }
};

// Helper function to actually end the turn
const endTurn = (game, session, io) => {

    // Update game state with new turn, and use result to determine initiative
    // True means initiative required, false means new turn player is determined automatically
    if (game.switchTurn()) io.to(session.gameID).emit('initiative-ready');
    else io.to(session.gameID).emit('new-turn', game.gameState.turnPlayer);
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
                const game = gameStore.get(session.gameID);

                // If the game exists, end the game
                if (game) {

                    // Set status of both players' sessions back to base
                    sessionStore.get(game.blueSessionID).stat = 'base';
                    sessionStore.get(game.redSessionID).stat = 'base';

                    // End game
                    endGame(session.gameID, 'disconnection', (session.color === 'blue' ? 'red' : 'blue'), io);
                }
            }

            // Delete the session from sessionStore
            sessionStore.delete(sessionID);
        }
    }, SESSION_TIMEOUT);
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
    handleCloseCombat,
    handleCivMove,
    handleCivReturn,
    handleDisconnect
};