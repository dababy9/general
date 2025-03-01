// Import initial game state
const { createInitialState, nodeMap, fullMap } = require('./objects');

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

// Function that retrieves the correct player object from a gameState given the color
const getPlayer = (gameData, color) => {
    return (color === 'blue' ? gameData.gameState.bluePlayer : gameData.gameState.redPlayer);
};

// Function that creates and returns a new game given two sessionIDs
const newGame = (blueID, redID) => {

    // Create gameData with both sessionIDs, the message log, and the actual game state
    const gameData = {
        blueSessionID: blueID,
        redSessionID: redID,
        blueInitiative: false,
        redInitiative: false,
        status: 'default',
        info: [],
        messages: [{from: 'server', data: "Game Initialized!"}],
        gameState: createInitialState()
    }

    // Return the game object
    return gameData;
};

// Conduct a roll for initiative and return results
// Automatically re-rolls if a tie occurs
const initiativeRoll = () => {

    // Rolls to be returned
    let blueRoll = 0, redRoll = 0;

    // Roll until a tie is broken
    while (blueRoll === redRoll){
        blueRoll = d6();
        redRoll = d6();
    }

    // Return result
    return { 'winner': blueRoll > redRoll ? 'blue' : 'red', 'blueRoll': blueRoll, 'redRoll': redRoll };
};

// Used for validating an input of two nodes
const twoNodeInputValidation = (input) => {

    // Ensure that the input exists, is an array, and is exactly two elements
    if (!input || !Array.isArray(input) || input.length != 2) return;

    // Ensure that both elements are strings
    if (typeof input[0] !== 'string' || typeof input[1] !== 'string') return;

    // Ensure that both elements are valid node names
    if (!nodeMap.has(input[0]) || !nodeMap.has(input[1])) return;

    // Return true to indicate successful validation
    return true;
};

// Function that processes/validates the client selecting two nodes to move from
const moveSelectAction = (gameData, nodes, color) => {

    // Input validation
    if (!twoNodeInputValidation(nodes)) return;

    // Grab individual elements
    const [node1, node2] = nodes;

    // Check if user selected nodes with moveable armies
    if (!gameData.gameState.nodes[node1].some(x => x.type === color && !x.hasMoved) || !gameData.gameState.nodes[node2].some(x => x.type === color && !x.hasMoved))
        return { type: 'error', error: 'no-moveable-army'};

    // Set game status and info
    gameData.status = 'move';
    gameData.info = [node1, node2];

    // Return lists of valid nodes to move to
    return { type: 'move-lists', to: 'client', data: [nodeMap.get(node1), nodeMap.get(node2)]};
};

// Function that moves a given color piece from one node to another
const movePiece = (fromNode, toNode, nodes, color) => {
    nodes[fromNode].splice(nodes[fromNode].indexOf({ type: color, hasMoved: false }));
    nodes[toNode].push({ type: color, hasMoved: true });
};

// Function that processes/validates the client actually moving armies
const moveConfirmAction = (gameData, nodes, color) => {

    // Input validation
    if (!twoNodeInputValidation(nodes)) return;

    // Grab individual elements
    const [node1, node2] = nodes;

    // Grab previous selections
    const [from1, from2] = gameData.info;

    if (!nodeMap.get(from1).includes(node1) || !nodeMap.get(from2).includes(node2))
        return { type: 'error', error: 'unconnected-node' };

    // Set game status
    gameData.status = 'default';

    // Subtract CP from turn player
    getPlayer(gameData, color).cp--;

    // Modify game state accordingly
    movePiece(from1, node1, gameData.gameState.nodes, color);
    movePiece(from2, node2, gameData.gameState.nodes, color);

    // Return new game state
    return { type: 'move', to: 'both', data: gameData.gameState };
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
    initiativeRoll,
    moveSelectAction,
    moveConfirmAction,
    CHMRAction,
    humanitarianAidAction,
    surgeAction,
    influenceOperationAction,
    artilleryFireAction,
    airStrikeAction
};