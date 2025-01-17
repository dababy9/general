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

// Define initial game state
const initialState = {
    messages: [{from: "server", message: "Game Initialized!"}],
    vp1: 0,
    vp2: 0
}

// Function that creates a new game given two sessionIDs, stores the game in the database, and returns the gameID
async function createGame(sessionID1, sessionID2) {

    // Create random gameID
    const gameID = randomBytes(12).toString('hex');

    // Create gameState with both sessionIDs and actual game data to be stored in Redis
    const gameState = {
        sessionID1,
        sessionID2,
        gameData: initialState
    }

    // Store the gameState in the database
    await gameStore.set(gameID, gameState);

    // Return the gameID of the game
    return gameID;
}

// Function that retrieves a game from the database
async function getGame(id) {

    // Attempts to retrieve the entry from the database
    const game = await gameStore.get(id);

    // Return null if the entry is empty
    if (!game)
        return null;

    // Otherwise, return the gameData field of the game, which is the relevant game information
    return game.gameData;
}

// Function that adds a new message to the message log
async function newMessage(gameID, sessionID, message) {

    // Attempts to retrieve the entry from the database
    const game = await gameStore.get(gameID);

    // If the game is empty, return false
    if (!game)
        return false;

    // Otherwise, append message to message log
    game.gameData.messages.push({from: sessionID, message: message});

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
    newMessage,
    close
};