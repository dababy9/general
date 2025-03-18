// Import initial game state
const { createInitialState, nodeMap, fullMap } = require('./objects');

// Define Game class
class Game {

    // Constructor takes both client sessionIDs
    constructor (blueID, redID) {

        // Player session IDs
        this.blueSessionID = blueID;
        this.redSessionID = redID;

        // Initiative trackers
        this.blueInitiative = false;
        this.redInitiative = false;

        // Status and info fields used to hold extra state
        this.status = 'default';
        this.info = [];

        // Game message log
        this.messages = [{from: 'server', data: "Game Initialized!"}];

        // Actual game state
        this.gameState = createInitialState();
    }

    // Static method to return a number 1-6, simulated die roll
    static d6 () { return Math.floor(Math.random()*6)+1; }

    // Static method to return an array of a given number of d6 rolls
    static d6Array (rolls) { return Array.from({ length: rolls }, Game.d6()); }

    // Static method to conduct a roll for initiative and return the result
    static initiative () {

        // Rolls to be returned
        let bRoll = 0, rRoll = 0;

        // Roll until a tie is broken
        while (bRoll === rRoll){
            bRoll = Game.d6();
            rRoll = Game.d6();
        }

        // Return result
        return { winner: bRoll > rRoll ? 'blue' : 'red', blueRoll: bRoll, redRoll: rRoll };
    }

    // Method to retrieve a player object given a corresponding color
    getPlayer (color) {

        // Return corresponding player given the string 'blue' or 'red'
        switch (color) {
            case 'blue': return this.gameState.bluePlayer;
            case 'red': return this.gameState.redPlayer;
        }

        // Return null if given anything else
        return null;
    }

    // Method to return a list of armies of a given color on a given node
    getArmies (color, node) {
        return this.gameState.nodes[node].filter(x => x.type === color);
    }

    // Method to return number of moveable armies of a given color are on a given node
    getMoveableArmies (color, node) {
        return this.getArmies(color, node).filter(x => !x.hasMoved).length;
    }

    // Method to validate and process a 'Humanitarian Aid' action
    humanitarianAidAction (color) {

        // Retrieve player object
        const player = this.getPlayer(color);

        // Make sure turn player has enough CP
        if (player.cp < 2) return;

        // Subtract CP from turn player
        player.cp -= 2;

        // Perform dice roll
        const roll = d6();

        // Adjust turn player support tracker accordingly
        if (roll === 6 && player.support < 6) player.support++;

        // Return dice roll result and new game state
        return { type: 'humanitarianAid', to: 'both', data: { result: roll, gameState: this.gameState }};
    }

    // Method to validate and process a 'Surge' action
    surgeAction (color) {

        // Retrieve player object
        const player = this.getPlayer(color);

        // Make sure turn player has enough CP and available units
        if (player.cp < 3 || player.surgeArmies < 4) return;

        // Subtract CP and surgeArmies from turn player
        player.cp -= 3;
        player.surgeArmies -= 4;
        
        // Add units to the corresponding base
        this.gameState.nodes[color + 'Base'].push(...Array.from({ length: 4 }, () => ({ type: color, hasMoved: false })))

        // Return new game state
        return { type: 'surge', to: 'both', data: this.gameState };
    }

    // Method to validate and process an 'Influence Operation' action
    influenceOperationAction (color) {

        // Retrieve player object
        const player = this.getPlayer(color)

        // Make sure turn player has enough CP
        if (player.cp < 3) return;

        // Subtract CP from turn player
        player.cp -= 3;

        // Retrieve opponent player object
        const opponent = getPlayer((color === 'blue' ? 'red' : 'blue'));

        // TODO -------------------------------
    }
}

// Used for validating an input of two nodes
const moveInputValidation = (input) => {

    // Ensure that the input exists, is an array, and is either one or two elements
    if (!input || !Array.isArray(input) || !(input.length === 2 || input.length === 1)) return;

    // Ensure that first element is a string and the name of a node
    if (typeof input[0] !== 'string' || !nodeMap.has(input[0])) return;

    // If there is a second element, ensure that it is a strign and the name of a node
    if (input[1] && (typeof input[1] !== 'string' || !nodeMap.has(input[1]))) return;

    // Return true to indicate successful validation
    return true;
};

// Function that processes/validates the client selecting two nodes to move from
const moveSelectAction = (gameData, nodes, color) => {

    // Make sure turn player has enough CP
    if (getPlayer(gameData, color).cp < 1) return;

    // Input validation
    if (!moveInputValidation(nodes)) return;

    // Grab individual elements
    const [node1, node2] = nodes;

    // If client only selected one node, make sure it has a moveable army
    if (!node2 && moveableArmies(gameData, color, node1) < 1)
        return { type: 'error', error: 'no-moveable-army' };

    // If the client selected two identical nodes, make sure the node has at least two moveable armies
    if (node2 && node1 === node2 && moveableArmies(gameData, color, node1) < 2)
        return { type: 'error', error: 'no-moveable-army' };

    // If the client selected two different nodes, make sure they each have a moveable army
    if (node2 && node1 !== node2 && (!moveableArmies(gameData, color, node1) || !moveableArmies(gameData, color, node2)))
        return { type: 'error', error: 'no-moveable-army' };

    // Set game status
    gameData.status = 'move';

    // Save chosen node(s) for move confirm
    gameData.info = [node1];
    if (node2) gameData.info.push(node2);

    // TODO ------------------------- MAYBE ONLY ADD ONE NODE TO LISTS IF THEY ONLY SELECTED ONE????

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
    if (!moveInputValidation(nodes)) return;

    // Grab individual elements
    const [node1, node2] = nodes;

    // Grab previous selections
    const [from1, from2] = gameData.info;

    // Check if the selected nodes are actually connected
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

// Function that processes/validates a 'Artillery Fire' action
const artilleryFireAction = (gameData, action) => {

};

// Function that processes/validates a 'Air Strike' action
const airStrikeAction = (gameData, action) => {

};

// Set up export to be used in handlers.js
module.exports = {
    Game
};