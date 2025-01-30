// Load environment variables from .env file
require('dotenv').config();

// Import Redis Interface and create a client
const RedisInterface = require('./redisInterface');
const gameStore = new RedisInterface(
    process.env.REDIS_HOST,
    process.env.REDIS_PORT
);

// Import randomBytes from crypto
const { randomBytes } = require('crypto');

// Import initial game state
const { initialState } = require('./objects');

// Function that creates a new game given two sessionIDs, stores the game in the database, and returns the gameID
async function createGame(sessionID1, sessionID2) {

    // Create random gameID
    const gameID = randomBytes(12).toString('hex');

    // Create gameData with both sessionIDs, the message log, and the actual game state to be stored in Redis
    const gameData = {
        blueSessionID: sessionID1,
        redSessionID: sessionID2,
        messages: [{from: "server", message: "Game Initialized!"}],
        gameState: initialState
    }

    // Store the gameData in the database
    await gameStore.set(gameID, gameData);

    // Return the gameID of the game
    return gameID;
}

// Function that retrieves a game from the database
async function getGame(id) {

    // Attempts to retrieve the entry from the database
    const game = await gameStore.get(id);

    // If the entry exists, return the gameState field of the game, which is the relevant game information
    // Otherwise, return null
    return game ? game.gameState : null;
}

// Function that retrieves the message log of a game from the database
async function getMessages(id) {

    // Attempts to retrieve the entry from the database
    const game = await gameStore.get(id);

    // If the entry exists, return the message log
    // Otherwise, return null
    return game ? game.messages : null;
}

// Function that adds a new message to the message log
async function newMessage(gameID, sessionID, message) {

    // Attempts to retrieve the entry from the database
    const game = await gameStore.get(gameID);

    // If the game is empty, return false
    if (!game)
        return false;

    // Determine the color of the player sending the message
    color = (sessionID == game.gameState.blueSessionID) ? 'blue' : 'red';

    // Pppend message to message log
    game.messages.push({from: color, message: message});

    // Update entry in database
    gameStore.set(gameID, game);

    // Return true to indicate successful message
    return true;
}

// Function to close gameStore Redis client
function close() {
    
    // Attempt to close
    try {
        gameStore.close();

    // Pass error up down the call stack if it was thrown
    } catch (err) {
        throw err;
    }
}

// Set up export to be used in server.js
module.exports = {
    createGame,
    getGame,
    getMessages,
    newMessage,
    close
};