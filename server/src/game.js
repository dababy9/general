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
        this.meta = { chmrList: [] };

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

    // Method to return the opposite color given
    otherColor(color) {
        return (color === 'blue' ? 'red' : 'blue');
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
    performCloseCombat (keepNode) {

        // Helper function to count army casualties
        const armyCasualties = (rolls) => rolls.filter(x => x === 5 || x === 6).length;

        // Retrieve close combat node
        const node = this.gameState.nodes[this.meta.combat[0]];

        // Roll all dice
        const blueRolls = d6Array(this.meta.blueCC);
        const redRolls = d6Array(this.meta.redCC);
        const blueCivRolls = d6Array(this.meta.blueCC);
        const redCivRolls = d6Array(this.meta.redCC);

        // Variable to store result
        const result = { blueRolls, redRolls };
        
        // Remove armies based on rolls
        const blueWin = this.removeArmies('red', armyCasualties(blueRolls), node);
        const redWin = this.removeArmies('blue', armyCasualties(redRolls), node);
        
        // End the game if necessary
        if (blueWin || redWin) {
            if (blueWin && redWin) return { winner: 'tie', reason: 'army' };
            else return { winner: (blueWin ? 'blue' : 'red'), reason: 'army' };
        }

        // If it's the blue player's turn, they do casualties first
        if (this.gameState.turnPlayer === 'blue') {

            // Assign blue roll to result
            result.blueCivRolls = blueCivRolls;

            // Remove civilians and end game if necessary
            if (this.removeCivilians('blue', blueCivRolls.filter(x => x === 6).length, node)) return { winner: 'red', reason: 'support' };

            // If there are civilians remaining, have red player conduct civilian casualties
            if (this.getPieces('civ', this.meta.combat[0]).length) {

                // Remove civilians and end game if necessary
                if (this.removeCivilians('red', redCivRolls.filter(x => x === 6).length, node)) return { winner: 'blue', reason: 'support' };

                // Assign red roll to result
                result.redCivRolls = redCivRolls;
            }

        // Otherwise, red player does casualties first
        } else {
            
            // Assign red roll to result
            result.redCivRolls = redCivRolls;

            // Remove civilians and end game if necessary
            if (this.removeCivilians('red', redCivRolls.filter(x => x === 6).length, node)) return { winner: 'blue', reason: 'support' };

            // If there are civilians remaining, have blue player conduct civilian casualties
            if (this.getPieces('civ', this.meta.combat[0]).length) {

                // Remove civilians and end game if necessary
                if (this.removeCivilians('blue', blueCivRolls.filter(x => x === 6).length, node)) return { winner: 'red', reason: 'support' };

                // Assign blue roll to result
                result.blueCivRolls = blueCivRolls;
            }
        }

        // Remove the current node from contested list based on keepNode
        if ((keepNode && !this.isContested(this.meta.combat[0])) || !keepNode)
            this.meta.combat.shift();

        // Reset flags
        this.blueFlag = false;
        this.redFlag = false;

        // Return result
        return result;
    }

    // Method to handle army removal
    removeArmies (color, num, node) {

        // Remove the armies and get the number removed
        const removed = this.removePieces(color, num, node);

        // Just return if no armies were removed
        if (!removed) return;

        // Retrieve target player object
        const player = this.getPlayer(color);

        // Remove armies from game state
        player.totalArmies -= removed;

        // If the target player is out of armies, indicate game over
        return player.totalArmies <= 0;
    }

    // Method to handle civilian removal
    removeCivilians (color, num, node) {

        // Remove the civilians and get the number removed
        const removed = this.removePieces('civ', num, node);

        // Just return if no civilians were removed or if civilians were removed naturally
        if (!removed || !color) return;

        // Retrieve acting player object
        const player = this.getPlayer(color);

        // Adjust player support based on civilians removed
        if (removed % 2 === 0) player.support -= removed/2;
        else player.support -= (removed-1)/2 + player.casualties % 2;

        // Add civilian casualties to acting player
        player.casualties += removed;

        // If support is at or below zero, set it to zero and indicate game over
        if (player.support <= 0) {
            player.support = 0;
            return true;

        // Otherwise, just return false
        } else return false;
    }

    // Method to remove pieces and return the actual number removed
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

        // Return number of pieces removed
        return removed;
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
        const nodeNames = Array.from(nodeMap.keys());

        // Helper function to randomly change population on a node
        // Will return an object containing the node and the rolls
        const change = () => {

            // Select a random node and roll 3 dice
            const nodeName = randomElement(nodeNames);
            const node = nodes[nodeName];
            const rolls = d6Array(3);

            // Calculate total civilian population change
            const popChange = rolls.reduce((sum, x) => sum + ((x % 2) ? -1 : 1), 0);

            // If positive, add civilian pieces to node
            if (popChange > 0)
                for (let i = 0; i < popChange; i++) node.push({ type: 'civ' });

            // Otherwise, remove civilian pieces from node
            else
                this.removeCivilians(false, -1*popChange, node);
            return { nodeName, rolls };
        }

        // Return results of two civilian population changes
        return [change(), change()];
    }

    // Method to switch turn player, returning true or false depending on whether initiative is required
    switchTurn () {

        // Increment turn counter
        this.gameState.turnCounter++;

        // Reset meta CHMR variable
        this.meta.chmrList = [];

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

    // Method to tally up VP and determine a winner
    winnerByVP () {

        // Initiate VP variables
        let blueVP = 0, redVP = 0;

        // Method to calculate VP from support
        const supportVP = (support) => {
            switch (support) {
                case 1: case 2: return -2;
                case 3: case 4: return 2;
                default: return 6;
            }
        }

        // Method to adjust VP on a given node
        const nodeVP = (node) => {
            let vp;
            switch (node[0]) {
                case "c": vp = 4; break;
                case "v": vp = 2; break;
                default: return;
            }
            if (this.getPieces('blue', node).length > 0) blueVP += vp;
            if (this.getPieces('red', node).length > 0) redVP += vp;
        }

        // Calculate support VP
        blueVP += supportVP(this.gameState.bluePlayer.support);
        redVP += supportVP(this.gameState.redPlayer.support);

        // Calculate VP from nodes
        Object.keys(this.gameState.nodes).forEach(nodeVP);

        // Return winner
        if (blueVP === redVP) return 'tie';
        return (blueVP > redVP ? 'blue' : 'red');
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
        return { type: 'move-lists', to: 'client', data: this.meta.move.map(x => this.getValidNodes(color, x)) };
    }

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
    }

    // Method to validate and process the client choosing a node and a number of armies to perform CHMR
    chmrSelectAction (action, color) {

        // Input validation for the node
        if (typeof action.node !== 'string' || !nodeMap.has(action.node)) return;

        // Input validation for the number of armies
        if (typeof action.armies !== 'number' || action.armies < 1 || action.armies > this.getPieces(color, action.node).length) return;

        // Make sure turn player has enough CP
        if (this.getPlayer(color).cp < 2) return;

        // Set game status
        this.status = 'chmrSelect';

        // Save chosen node for CHMR haven selection
        this.meta.chmr = action;
        
        // Return list of adjacent havens
        return { type: 'haven-list', to: 'client', data: fullMap.get(action.node)};
    }

    // Method to validate and process the client choosing a haven to move the armies to
    chmrHavenAction (haven, color) {

        // Input validation
        if (typeof haven !== 'string' || nodeMap.has(haven) || !fullMap.has(haven)) return;

        // Grab previous selection
        const { node, armies } = this.meta.chmr;

        // Make sure the selected haven is adjacent to the previously selected node
        if (!fullMap.get(node).includes(haven)) return;

        // Move the armies to the selected haven
        const nodes = this.gameState.nodes;
        for (let i = 0; i < armies; i++) {
            const index = nodes[node].findIndex(x => x.type === color);
            const [piece] = nodes[node].splice(index, 1);
            nodes[haven].push(piece);
        }

        // Save the haven for both civilian pull and return
        this.meta.chmrHaven = haven;
        this.meta.chmrList.push(haven);

        // Set game status
        this.status = 'chmrHaven';

        // Return list of adjacent nodes and new game state
        return { type: 'haven-move', to: 'both', data: { nodes: fullMap.get(haven), gameState: this.gameState } };
    }

    // Method to validate and process the client selecting a node to pull civilians from
    chmrPullAction (node, color) {

        // Input validation
        if (typeof node !== 'string' || !nodeMap.has(node)) return;

        // Grab previous selections
        const { armies } = this.meta.chmr;
        const haven = this.meta.chmrHaven;

        // Make sure the selected node is adjacent to the previously selected haven
        if (!fullMap.get(haven).includes(node)) return;

        // Perform dice rolls
        const rolls = d6Array(armies);

        // Calculate number of civilians to be moved
        const count = rolls.reduce((sum, x) => sum + (x > 5 ? 2 : (x > 2 ? 1 : 0)), 0);

        // Move civilians to the selected haven
        const nodes = this.gameState.nodes;
        let removed = 0;
        for (let i = 0; i < count; i++) {
            if (this.getPieces('civ', node).length === 0) break;
            const index = nodes[node].findIndex(x => x.type === 'civ');
            const [piece] = nodes[node].splice(index, 1);
            nodes[haven].push(piece);
            removed++;
        }

        // Subtract CP from turn player
        this.getPlayer(color).cp -= 2;

        // Set game status
        this.status = 'default';

        // Return result of civilian pull and new game state
        return { type: 'pull-result', to: 'both', data: { rolls, removed, gameState: this.gameState } };
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

        // Return die roll result and new game state
        return { type: 'humanitarianAid', to: 'both', data: { result: roll, gameState: this.gameState } };
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
        const opponent = this.getPlayer(this.otherColor(color));

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
        return { type: 'influenceOperation', to: 'both', data: { result: rolls, gameState: this.gameState } };
    }

    // Method to validate and process the client selecting node to fire from
    artillerySelectAction (node, color) {

        // Input validation
        if (typeof node !== 'string' || !nodeMap.has(node)) return;

        // Retrieve player object
        const player = this.getPlayer(color);

        // Make sure turn player has enough CP
        if (player.cp < 1) return;
        
        // Make sure node has a player's army on it
        if (this.getPieces(color, node).length === 0) return;

        // Generate list of valid adjacent nodes
        const adjacentNodes = nodeMap.get(node).filter(x => this.getPieces(this.otherColor(color), x).length > 0);

        // Make sure there is at least one node that the player can fire on
        if (adjacentNodes.length <= 0) return;

        // Set game status
        this.status = 'artillery';

        // Save chosen node for artillery confirm
        this.meta.fire = node;

        // Send list of adjacent nodes with opposing armies to client
        return { type: 'fire-list', to: 'client', data: adjacentNodes };
    }

    // Method to validate and process the client selecting node to fire on
    artilleryConfirmAction (node, color) {

        // Input validation
        if (typeof node !== 'string' || !nodeMap.has(node)) return;

        // Retrieve previously selected node
        const from = this.meta.fire;

        // Make sure nodes are connected
        if (!nodeMap.get(from).includes(node)) return;

        // Subtract CP from turn player
        this.getPlayer(color).cp--;

        // Set game status
        this.status = 'default';

        // Perform and return long range combat
        return this.longRange('fire', node, color);
    }

    // Method to validate and process a 'Air Strike' action
    airStrikeAction (node, color) {

        // Input validation
        if (typeof node !== 'string' || !nodeMap.has(node)) return;

        // Retrieve player object
        const player = this.getPlayer(color);

        // Make sure turn player has enough CP
        if (player.cp < 2) return;

        // Subtract CP from turn player
        player.cp -= 2;

        // Perform and return long range combat
        return this.longRange('strike', node, color);
    }

    // Handle rolls and piece removal for long range combat
    longRange (type, node, color) {

        // Perform rolls
        const combatRoll = d6Array((type === 'fire' ? 3 : 2));
        const civRoll = d6Array((type === 'fire' ? 3 : 2));

        // Retrieve the node
        const n = this.gameState.nodes[node];

        // Calculate combat casualties and end the game if necessary
        if (this.removeArmies(this.otherColor(color), combatRoll.filter(x => x > 3 && x < 7).length, n))
            return { reason: 'army', winner: color };

        // Calculate civilian casualties and end the game if necessary
        if (this.removeCivilians(color, civRoll.filter(x => x > (type === 'fire' ? 2 : 4) && x < 6).length, n))
            return { reason: 'support', winner: this.otherColor(color) };

        // Return rolls and game state
        return { type, to: 'both', data: { rolls: { combatRoll, civRoll }, gameState: this.gameState } };
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