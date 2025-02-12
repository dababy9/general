// Import initial game state
const { createInitialState } = require('./objects');

// Returns a number 1-6, used as a digital 'dice roll'
const d6 = () => {
    return Math.floor(Math.random()*6)+1;
};

// Returns an array of a given number of d6 rolls
const d6Array = (rolls) => {
    return Array.from({ length: rolls }, d6())
};

// Returns a sum total of a given number of d6 rolls
const d6Sum = (rolls) => {
    return d6Array(rolls).reduce((sum, roll) => sum+roll, 0);
};

// Function that creates and returns a new game given two sessionIDs
const newGame = (blueID, redID) => {

    // Create gameData with both sessionIDs, the message log, and the actual game state
    const gameData = {
        blueSessionID: blueID,
        redSessionID: redID,
        inPlay: false,
        messages: [{from: 'server', data: "Game Initialized!"}],
        gameState: createInitialState()
    }

    // Return the game object
    return gameData;
};

// Function that processes/validates a 'Move' action
const moveAction = (gameData, action) => {

};

// Function that processes/validates a 'CHMR' action
const CHMRAction = (gameData, action) => {

};

// Function that processes/validates a 'Humanitarian Aid' action
const humanitarianAidAction = (gameData, action) => {

};

// Function that processes/validates a 'Surge' action
const surgeAction = (gameData, action) => {

};

// Function that processes/validates a 'Influence Operation' action
const influenceOperationAction = (gameData, action) => {

};

// Function that processes/validates a 'Artillery Fire' action
const artilleryFireAction = (gameData, action) => {

};

// Function that processes/validates a 'Air Strike' action
const airStrikeAction = (gameData, action) => {

};

// Set up export to be used in handlers.js
module.exports = {
    newGame,
    moveAction,
    CHMRAction,
    humanitarianAidAction,
    surgeAction,
    influenceOperationAction,
    artilleryFireAction,
    airStrikeAction
};