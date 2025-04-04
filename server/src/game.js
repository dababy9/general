// Import initial game state and game maps
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

        // Status and meta fields used to hold extra state
        this.status = 'default';
        this.meta = {};

        // Game message log
        this.messages = [{from: 'server', data: "Game Initialized!"}];

        // Actual game state
        this.gameState = createInitialState();
    }

    // Static method to conduct a roll for initiative and return the result
    static initiative () {

        // Rolls to be returned
        let bRoll, rRoll;

        // Roll until a tie is broken
        do {
            bRoll = d6();
            rRoll = d6();
        } while (bRoll === rRoll);

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

    // Method to return a list of pieces of a given type on a given node
    getPieces (type, node) {
        return this.gameState.nodes[node].filter(x => x.type === type);
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

    // Method to initiate close combat (add contested nodes to game.meta.combat)
    initiateCloseCombat () {

        // Set game status
        this.status = 'closeCombat';

        // Create list of contested nodes in meta variable
        this.meta.combat = Object.keys(this.gameState.nodes).filter(x => this.isContested(x));

        // Return whether close combat is required
        return this.meta.combat.length > 0;
    }

    // Method to perform close combat
    performCloseCombat () {

        // Helper function to count army casualties
        const armyCasualties = (rolls) => rolls.filter(x => x === 5 || x === 6).length;

        // Helper function to handle civilian casualties
        // Returns true if second player's roll was needed, false if not
        const handleCivilians = (rolls1, rolls2, color1, color2) => {

            // Calculate civilian casualties for turn player
            this.removePieces('civ', rolls1.filter(x => x === 6).length, node, color1);

            // If civilians remain, continue calculation for opposing player and return true
            if (this.getPieces('civ', node).length) {
                this.removePieces('civ', rolls2.filter(x => x === 6).length, node, color2);
                return true;

            // Otherwise, return false
            } else return false;
        }

        // Roll all dice
        const blueRolls = d6Array(this.meta.blueCC);
        const redRolls = d6Array(this.meta.redCC);
        const blueCivRolls = d6Array(this.meta.blueCC);
        const redCivRolls = d6Array(this.meta.redCC);

        // Retrieve close combat node
        const node = this.gameState.nodes[this.meta.combat[0]];

        // Remove armies based on rolls
        this.removePieces('red', armyCasualties(blueRolls), node, 'blue');
        this.removePieces('blue', armyCasualties(redRolls), node, 'red');

        // Variable to store result
        let result = { blueRolls, redRolls };

        // If it's the blue player's turn, they do casualties first
        if (this.gameState.turnPlayer === 'blue') {
            result.blueCivRolls = blueCivRolls;
            if (handleCivilians(blueRolls, redRolls, 'blue', 'red')) result.redCivRolls = redCivRolls;

        // Otherwise, red player does them first
        } else {
            result.redCivRolls = redCivRolls;
            if (handleCivilians(redRolls, blueRolls, 'red', 'blue')) result.blueCivRolls = blueCivRolls;
        }

        // Remove node from close combat list
        this.meta.combat.shift();

        // Return result
        return result;
    }

    // Method to handle piece removal (modifying game state and bookkeeping)
    removePieces (type, num, node, actingColor) {

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

        // If the pieces removed were armies, subtract from variable in game state
        const target = this.getPlayer(type);
        if (target) target.totalArmies -= removed;

        // Otherwise, add civilian casualties to acting player
        const acting = this.getPlayer(actingColor);
        if (acting) acting.casualties += removed;
    }

    // Method to reset values for new turn
    resetValues () {

        // Reset CP
        this.gameState.bluePlayer.cp = 6;
        this.gameState.redPlayer.cp = 6;

        // Reset piece movement
        for (const [_, node] of Object.entries(this.gameState.nodes))
            for (const piece of node)
                if (piece.hasMoved) piece.hasMoved = false;
    }

    // Method to add or subtract civilian population(s) after each turn
    // Returns 
    civilianPopulation () {

        // Helper function to return a random element from an array
        const randomElement = (list) => list[Math.floor(Math.random()*list.length)];

        // Choose two random nodes
        const nodes = this.gameState.nodes;
        const nodeNames = Object.keys(nodes);

        // Helper function to randomly change population on a node
        // Will return an object containing the node and the rolls
        const change = () => {

            // Select a random node and roll 3 dice
            const nodeName = randomElement(nodeNames);
            const node = nodes[nodeName];
            const rolls = d6Array(3);

            // Calculate total civilian population change
            const popChange = rolls.reduce((sum, x) => sum + ((x % 2) ? -1 : 1));

            // If positive, add civilian pieces to node
            if (popChange > 0)
                for (let i = 0; i < popChange; i++) node.push({ type: 'civ' });

            // Otherwise, remove civilian pieces from node
            else
                this.removePieces('civ', -1*popChange, node);
            return { nodeName, rolls };
        }

        // Return results of two civilian population changes
        return [change(), change()];
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

    // Method to validate and process the client selecting node(s) to move from
    moveSelectAction (nodes, color) {

        // Helper function to return number of moveable armies of a given color on a given node
        const getMoveableArmies = (color, node) => {
            return this.getPieces(color, node).filter(x => !x.hasMoved).length;
        }

        // Make sure turn player has enough CP
        if (this.getPlayer(color).cp < 1) return;

        // Input validation
        if (!moveInputValidation(nodes)) return;

        // Grab individual elements
        const [node1, node2] = nodes;

        // Ensure the turn player has correct amount of armies to move
        if (getMoveableArmies(color, node1) < (node2 ? (node1 === node2 ? 2 : 1) : 1) || (node2 && (node2 ? getMoveableArmies(color, node2) : 0) < 1))
            return { type: 'error', error: 'no-moveable-army' };
 
        // Set game status
        this.status = 'move';

        // Save chosen node(s) for move confirm
        this.meta.move = (node2 ? [node1, node2] : [node1]);

        // Return lists of valid nodes to move to
        return { type: 'move-lists', to: 'client', data: this.meta.move.map(x => this.getValidNodes(color, x))};
    };

    // Method to validate and process the client actually moving armies
    moveConfirmAction (nodes, color) {

        // Helper function to move a given color piece from one node to another
        const movePiece = (fromNode, toNode, color) => {
            const nodes = this.gameState.nodes;
            const index = nodes[fromNode].findIndex(x => x.type === color && !x.hasMoved);
            const [piece] = nodes[fromNode].splice(index, 1);
            piece.hasMoved = true;
            nodes[toNode].push(piece);
        }

        // Input validation
        if (!moveInputValidation(nodes)) return;

        // Grab individual elements
        const [node1, node2] = nodes;

        // Grab previous selections
        const [from1, from2] = this.meta.move;

        // Check if the selected nodes are actually connected
        if (!this.getValidNodes(color, from1).includes(node1) || (from2 && !this.getValidNodes(color, from2).includes(node2)))
            return { type: 'error', error: 'unconnected-node' };

        // Set game status
        this.status = 'default';

        // Subtract CP from turn player
        this.getPlayer(color).cp--;

        // Modify game state accordingly
        movePiece(from1, node1, color);
        if (from2) movePiece(from2, node2, color);

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
            return { reason: 'support', winner: color };
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
    if (!input || !Array.isArray(input) || !(input.length === 2 || input.length === 1)) return false;

    // Ensure that first element is a string and the name of a node
    if (typeof input[0] !== 'string' || !nodeMap.has(input[0])) return false;

    // If there is a second element, ensure that it is a strign and the name of a node
    if (input[1] && (typeof input[1] !== 'string' || !nodeMap.has(input[1]))) return false;

    // Return true to indicate successful validation
    return true;
};

// Set up export to be used in handlers.js
module.exports = {
    Game
};