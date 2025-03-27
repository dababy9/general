// Import initial game state
const { createInitialState, nodeMap, fullMap } = require('./objects');

// Define Game class
class Game {

    // Constructor takes both client sessionIDs
    constructor (blueID, redID) {

        // Player session IDs
        this.blueSessionID = blueID;
        this.redSessionID = redID;

        // Flags for actions requiring both players
        this.blueFlag = false;
        this.redFlag = false;

        // Status and info fields used to hold extra state
        this.status = 'default';
        this.info = {};

        // Game message log
        this.messages = [{from: 'server', data: "Game Initialized!"}];

        // Actual game state
        this.gameState = createInitialState();
    }

    // Static method to conduct a roll for initiative and return the result
    static initiative () {

        // Rolls to be returned
        let bRoll = 0, rRoll = 0;

        // Roll until a tie is broken
        while (bRoll === rRoll){
            bRoll = d6();
            rRoll = d6();
        }

        // Return result
        return { winner: bRoll > rRoll ? 'blue' : 'red', blueRoll: bRoll, redRoll: rRoll };
    }

    // Method to retrieve a player object given a corresponding color
    getPlayer (color) {
        switch (color) {
            case 'blue': return this.gameState.bluePlayer;
            case 'red': return this.gameState.redPlayer;
            default: return null;
        }
    }

    // Method to return a list of armies of a given color on a given node
    getArmies (color, node) {
        return this.gameState.nodes[node].filter(x => x.type === color);
    }

    // Method to return number of moveable armies of a given color are on a given node
    getMoveableArmies (color, node) {
        return this.getArmies(color, node).filter(x => !x.hasMoved).length;
    }

    // Method to return nodes that a piece of a given color can move to from a given node
    getValidNodes (color, node) {
        const nodes = nodeMap.get(node);
        if (!this.isContested(node)) return nodes;
        else return nodes.filter(x => this.gameState.nodes[x].some(y => y.type === color));
    }

    // Method to return whether a node is contested or not
    isContested (node) {
        const n = this.gameState.nodes[node];
        return n.some(x => x.type === 'blue') && n.some(x => x.type === 'red');
    }

    // Method to initiate close combat (add contested nodes to game.info.combat)
    initiateCloseCombat () {

        // Set game status
        this.status = 'closeCombat';

        // Reset game info to store contested nodes
        this.info.combat = [];

        // Loop over each node
        for (const node in this.gameState.nodes)

            // If the node is contested, add it to the list
            if (this.isContested(node)) this.info.combat.push(node);

        // Return whether close combat is required
        return this.info.combat.length !== 0;
    }

    // Method to perform close combat
    performCloseCombat () {

        // Roll all dice
        const result = {
            blueRolls: d6Array(this.info.blueCC),           
            redRolls: d6Array(this.info.redCC),
            blueCivRolls: d6Array(this.info.blueCC),
            redCivRolls: d6Array(this.info.redCC)
        };

        // Retrieve close combat node
        const node = this.gameState.nodes[this.info.combat[0]];

        // Remove armies based on rolls
        this.removePieces('blue', result.redRolls.filter(x => x === 5 || x === 6).length, node);
        this.removePieces('red', result.blueRolls.filter(x => x === 5 || x === 6).length, node);

        // Remove node from close combat list
        this.info.combat.shift();

        // Return rolls
        return result;
    }

    // Method to remove a given number of a given type of piece from a given node
    removePieces (type, num, node) {

        // Counter to keep track of how many pieces have been removed
        let removed = 0;

        // Loop through pieces of the node and remove them by type
        for (let i = 0; i < node.length; i++) {
            if (removed >= num) break;
            if (node[i].type === type) {
                node.splice(i, 1);
                removed++;
                i--;
            }
        }
    }

    // Method to reset piece movement data
    resetMovement () {
        for (const [_, node] of Object.entries(this.gameState.nodes)) {
            for (const piece of node)
                if (piece.hasMoved) piece.hasMoved = false;
        }
    }

    // Method to switch turn player, returning true or false depending on whether initiative is required
    switchTurn () {

        // Increment turn counter
        this.gameState.turnCounter++;

        // Reset game status
        this.status = 'default';

        // If turn counter is even, set turn player to empty and return true
        if (this.gameState.turnCounter % 2 === 0) {
            this.gameState.turnPlayer = '';
            return true;

        // Otherwise, simply switch the turn player to the opposite color and return false
        } else {
            this.gameState.turnPlayer = (this.gameState.turnPlayer === 'blue' ? 'red' : 'blue');
            return false;
        }
    }

    // Method to move a given color piece from one node to another
    movePiece (fromNode, toNode, color) {
        const nodes = this.gameState.nodes;
        nodes[fromNode].splice(nodes[fromNode].indexOf({ type: color, hasMoved: false }));
        nodes[toNode].push({ type: color, hasMoved: true });
    }

    // Method to validate and process the client selecting node(s) to move from
    moveSelectAction (nodes, color) {

        // Make sure turn player has enough CP
        if (this.getPlayer(color).cp < 1) return;

        // Input validation
        if (!moveInputValidation(nodes)) return;

        // Grab individual elements
        const [node1, node2] = nodes;

        // If client only selected one node, make sure it has a moveable army
        if (!node2 && this.getMoveableArmies(color, node1) < 1)
            return { type: 'error', error: 'no-moveable-army' };

        // If the client selected two identical nodes, make sure the node has at least two moveable armies
        if (node2 && node1 === node2 && this.getMoveableArmies(color, node1) < 2)
            return { type: 'error', error: 'no-moveable-army' };

        // If the client selected two different nodes, make sure they each have a moveable army
        if (node2 && node1 !== node2 && (!this.getMoveableArmies(color, node1) || !this.getMoveableArmies(color, node2)))
            return { type: 'error', error: 'no-moveable-army' };

        // Set game status
        this.status = 'move';

        // Save chosen node(s) for move confirm, and create list(s) of valid nodes to move to
        this.info.move = [node1];
        const response = [this.getValidNodes(color, node1)];
        if (node2) {
            this.info.move.push(node2);
            response.push(this.getValidNodes(color, node2));
        }

        // Return lists of valid nodes to move to
        return { type: 'move-lists', to: 'client', data: response};
    };

    // Method to validate and process the client actually moving armies
    moveConfirmAction (nodes, color) {

        // Input validation
        if (!moveInputValidation(nodes)) return;

        // Grab individual elements
        const [node1, node2] = nodes;

        // Grab previous selections
        const [from1, from2] = this.info.move;

        // Check if the selected nodes are actually connected
        if (!this.getValidNodes(color, from1).includes(node1) || (from2 && !this.getValidNodes(color, from2).includes(node2)))
            return { type: 'error', error: 'unconnected-node' };

        // Set game status
        this.status = 'default';

        // Subtract CP from turn player
        this.getPlayer(color).cp--;

        // Modify game state accordingly
        this.movePiece(from1, node1, color);
        if (from2) this.movePiece(from2, node2, color);

        // Return new game state
        return { type: 'move', to: 'both', data: this.gameState };
    };

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

        // Return die roll result and new game state
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
        const opponent = this.getPlayer((color === 'blue' ? 'red' : 'blue'));

        // Perform dice rolls
        const rolls = d6Array(Math.floor(opponent.casualties/2));

        // Decrease opponent support tracker by one for each 6 rolled
        opponent.support -= rolls.filter(x => x === 6).length;

        // End the game if opponent support is at or below zero
        if (opponent.support <= 0) {
            opponent.support = 0;
            // TODO (end game) ----------------------------------------------------
        }

        // Return dice roll results and new game state
        return { type: 'influenceOperation', to: 'both', data: { result: rolls, gameState: this.gameState }};
    }

    // Method to validate and process the client selecting node to fire from
    artillerySelectAction (color) {

        // Set game status
        this.status = 'artillery';
    }

    // Method to validate and process the client selecting node to fire on
    artilleryConfirmAction (color) {

        // Set game status
        this.status = 'default';
    }

    // Method to validate and process a 'Air Strike' action
    airStrikeAction (color) {

    }
}

// Return a number 1-6, simulated die roll
const d6 = () => { return Math.floor(Math.random()*6)+1; }

// Return an array of a given number of d6 rolls
const d6Array = (rolls) => { return Array.from({ length: rolls }, () => d6()); }

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

// Set up export to be used in handlers.js
module.exports = {
    Game
};