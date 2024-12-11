// Import Redis Interface and create a client
const RedisInterface = require('./RedisInterface');
const gameStore = new RedisInterface();

// Import randomBytes from crypto
const { randomBytes } = require('crypto');

// Define initial game state
const initialState = {
    vp1: 0,
    vp2: 0
}

// Function that creates a new game given two sessionIDs, stores the game in a database, and returns the gameID
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

// Set up export to be used in server.js
module.exports = {
    createGame
};