// Import initial game state
const { createInitialState } = require('./objects');

// Returns a number 1-6, used as a digital 'dice roll'
const d6 = () => {
    return Math.floor(Math.random()*6)+1;
}

// Returns an array of a given number of d6 rolls
const d6Array = (rolls) => {
    return Array.from({ length: rolls }, d6())
}

// Returns a sum total of a given number of d6 rolls
const d6Sum = (rolls) => {
    return d6Array(rolls).reduce((sum, roll) => sum+roll, 0);
}

// Function that creates and returns a new game given two sessionIDs
const newGame = (sessionID1, sessionID2) => {

    // Create gameData with both sessionIDs, the message log, and the actual game state
    const gameData = {
        blueSessionID: sessionID1,
        redSessionID: sessionID2,
        messages: [{from: 'server', data: "Game Initialized!"}],
        gameState: createInitialState()
    }

    // Return the game object
    return gameData;
}

// Set up export to be used in server.js
module.exports = {
    newGame
};