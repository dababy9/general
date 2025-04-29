import * as Board from './gameBoard.js';
import * as Objects from './objects.js';

// Global variables
let playerColor;
let gameState;
let highlightedNodes;

// ---------- Helper functions ----------

// Function to retrieve the current player object from the game state
function currentPlayer () {
    return (playerColor === 'blue' ? gameState.bluePlayer : gameState.redPlayer);
}

// Function to take in three key style components and return a PIXI TextStyle
function makeTextStyle (fontSize, fill, align) {
    return new PIXI.TextStyle({ fontSize, fill, align });
}

// Function to specifically 'memoize' white, centered text of a given font size
const memo = {};
function text (size) {
    const key = String(size);
    if (!memo[key]) memo[key] = makeTextStyle(size, 0xFFFFFF, 'center');
    return memo[key];
}

// ---------- Initialization ----------

// Start PIXI application
const app = new PIXI.Application();
await app.init({ width: 1000, height: 650, background: '#DFE8F3' });

// Load all images to be used in sprite creation
await Board.loadAssets();

// Requests player color
socket.emit('game', 'fetch-color');

// Add static sprites to canvas
app.stage.addChild(
    Object.assign(PIXI.Sprite.from('/content/backgroundVV.png'), { width: 1000, height: 650, zIndex: 0 }), // Background
    Object.assign(PIXI.Sprite.from('/content/Board_Circles.png'), { x: 110, y: 40, width: 750, height: 456, zIndex: 10 }), // Game board
    Board.makeBoardPiece(128, 450, 100, 175, 10, '/content/blueGrave.png'), // Blue gravestone
    Board.makeBoardPiece(848, 450, 100, 175, 10, '/content/redGrave.png'), // Red gravestone
    Board.makeBoardPiece(30, 350, 193, 89, 10, '/content/blueCPTracker.png'), // Blue CP tracker
    Board.makeBoardPiece(750, 350, 193, 89, 10, '/content/redCPTracker.png'), // Red CP tracker
    Board.makeBoardPiece(20, 450, 95, 170, 10, '/content/blueSurgeNew.png'), // Blue surge icon
    Board.makeBoardPiece(740, 450, 95, 170, 10, '/content/redSurgeNew.png'), // Red surge icon
    Board.makeBoardPiece(230, 505, 500, 115, 10, '/content/support_tracker.png'), // Support tracker
);

// Create text styles
const boardTextStyle = makeTextStyle(30, 0x520E05, 'center');
const pieceTextStyle = Object.assign(text(32), { fontWeight: 'bold' });

// Create all dynamic sprites for blue stats
const blueSprites = {
    support: Board.makeCircle(695, 552, 18, 20, 0x0000FF, 2, 0x0F0FFF, 0.75),
    casualties: Board.makeBoardText(168, 492, 20, "", boardTextStyle),
    surge: Board.makeBoardText(58, 492, 20, "", boardTextStyle),
    cp: Board.makeBoardText(113, 385, 20, "", boardTextStyle)
};

// Create all dynamic sprites for red stats
const redSprites = {
    support: Board.makeCircle(695, 591, 18, 20, 0xFF0000, 2, 0xFF0FF0, 0.75),
    casualties: Board.makeBoardText(888, 492, 20, "", boardTextStyle),
    surge: Board.makeBoardText(778, 492, 20, "", boardTextStyle),
    cp: Board.makeBoardText(833, 385, 20, "", boardTextStyle)
};

// Create round number text and background
const roundText = Board.makeBoardText(871, 210, 20, "Round", makeTextStyle(17, 0x000000, 'center'));
const roundNumber = Board.makeBoardText(889, 235, 20, "", makeTextStyle(30, 0x000000, 'center'));
const roundBackground = new PIXI.Graphics()
    .roundRect(850, 200, 100, 80, 10)
    .fill({ color: 0x000000, alpha: 0 })
    .stroke(10, 0x000000);

// Add dynamic sprites to canvas
app.stage.addChild(
    ...Object.values(blueSprites),
    ...Object.values(redSprites),
    roundText,
    roundNumber,
    roundBackground
);

// Create map of node names to corresponding sprites
const nodeSprites = {};

// Loop through all nodes and their positions
for (const [node, p] of Object.entries(Objects.locations)) {

    // Create texts for pieces
    const blueText = Board.makeBoardText(p.x + 62, p.y - 10, 30, '0', pieceTextStyle, false);
    const redText = Board.makeBoardText(p.x + 10, p.y - 10, 30, '0', pieceTextStyle, false);
    const civText = Board.makeBoardText(p.x + 35, p.y + 40, 30, '0', pieceTextStyle, false);

    // Create icons for pieces
    const blueIcon = Object.assign(Board.makeBoardPiece(p.x + 25, p.y - 45, 90, 90, 20, '/content/IFV_Blue.png'), { visible: false });
    const redIcon = Object.assign(Board.makeBoardPiece(p.x - 25, p.y - 35, 90, 90, 20, '/content/IFV_Red.png'), { visible: false });
    const civIcon = Object.assign(Board.makeBoardPiece(p.x + 20, p.y + 35, 50, 50, 20, '/content/civilians2.png'), { visible: false });

    // Add sprites to canvas
    app.stage.addChild(blueText, redText, civText, blueIcon, redIcon, civIcon);

    // Add sprites to map
    nodeSprites[node] = { blueText, redText, civText, blueIcon, redIcon, civIcon };
}

// Function to redraw the board using global game state 
function updateBoard() {

    // Helper function to update given sprites with given player stats
    const updateStats = (player, sprites) => {
        sprites.support.x = Objects.supportCoords[player.support];
        sprites.casualties.text = player.casualties;
        sprites.surge.text = player.surgeArmies;
        sprites.cp.text = player.cp;
    }

    // Update both players' displayed stats
    updateStats(gameState.bluePlayer, blueSprites);
    updateStats(gameState.redPlayer, redSprites);

    // Update player turn banner
    updateTurnBanner();

    // Update round
    roundNumber.text = Math.floor(gameState.turnCounter / 2) + 1;

    // Show initiative button if turn player is empty
    if (!gameState.turnPlayer) initiativeButton.visible = initiativeText.visible = true;

    // Update all piece sprites
    for (const [node, pieces] of Object.entries(getCount(gameState.nodes))) {

        // Retrieve sprites of node
        const sprites = nodeSprites[node];

        // If it is not the player's turn and armies have changed, draw arrow on the node
        if (gameState.turnPlayer !== playerColor && (sprites.blueText.text != pieces.blueArmies || sprites.redText.text != pieces.redArmies))
            doArrow(node);

        // Update piece texts
        sprites.blueText.text = pieces.blueArmies;
        sprites.redText.text = pieces.redArmies;
        sprites.civText.text = pieces.civilians;

        // Set visibility of pieces based on number
        sprites.blueText.visible = sprites.blueIcon.visible = sprites.blueText.text !== '0';
        sprites.redText.visible = sprites.redIcon.visible = sprites.redText.text !== '0';
        sprites.civText.visible = sprites.civIcon.visible = sprites.civText.text !== '0';
    }
}



// ---------- Socket Event Handlers ----------

// If Socket.IO server can't be reached, print connection error
socket.on('connect_error', (error) => {
    console.log('CONNECTION ERROR: ', error);
});

// Assign global game state accordingly, and update board
socket.on('game-state', (data) => {
    gameState = JSON.parse(data);
    updateBoard();
    //gameOverReceived({vp: {blue: 10, red: 2}, reason: 'vp', winner: 'blue', gameState})
    // closeCombatReceived();
    // civMoveReceived();
});

// Assign global player color accordingly
socket.on('color', (color) => {
    playerColor = color;
});

// Handle initiative result message
socket.on('initiative-result', (data) => {

    // Call corresponding function with parsed data
    initiativeReceived(JSON.parse(data));
});

// Handle move list message
socket.on('move-lists', (data) => {

    // Call corresponding function with parsed data
    moveReceived(JSON.parse(data));
});

// Handle move message (completed move)
socket.on('move', (data) => {

    // Parse data and update game state
    gameState = JSON.parse(data);

    // Update board
    updateBoard();

    // Either open the CP menu or change action banner, depending on player's color
    if (gameState.turnPlayer === playerColor) Board.openCPQuery();
    else updateActionBanner("Opponent completed a Move!\nWaiting on them to spend command points...");
});

// Handle haven list message
socket.on('haven-list', (data) => {

    // Call corresponding function with parsed data
    chmrReceived(JSON.parse(data));
});

// Handle haven move message
socket.on('haven-move', (data) => {

    // Parse data
    const result = JSON.parse(data);

    // Update game state and board
    gameState = result.gameState;
    updateBoard();
   
    // If it's the player's turn, call corresponding function with parsed nodes
    if (gameState.turnPlayer === playerColor) chmr2Recived(result.nodes);
});

// Handle pull result message
socket.on('pull-result', (data) => {

    // Call corresponding function with parsed data
    pullResultReceived(JSON.parse(data));
});

// Handle humanitarian aid message
socket.on('humanitarianAid', (data) => {
    
    // Call corresponding function with parsed data
    humanitarianAidReceived(JSON.parse(data));
});

// Handle surge message
socket.on('surge', (data) => {

    // Parse data and update game state
    gameState = JSON.parse(data);

    // Update board
    updateBoard();

    // Either open the CP menu or change action banner, depending on player's color
    if (gameState.turnPlayer === playerColor) Board.openCPQuery();
    else updateActionBanner("Opponent completed a Surge!\n Waiting on them to spend command points...");
});

// Handle influence operation message
socket.on('influenceOperation', (data) => {

    // Call corresponding function with parsed data
    influenceOperationReceived(JSON.parse(data));
});

// Handle fire list message
socket.on('fire-list', (data) => {

    // Call corresponding function with parsed data
    highlight(JSON.parse(data), fires2, "Pick the node where you are firing to:");
});

// Handle fire message
socket.on('fire', (data) => {
    
    // Call corresponding function with parsed data
    fireReceived(JSON.parse(data));
});

// Handle strike message
socket.on('strike', (data) => {

    // Call corresponding function with parsed data
    strikeReceived(JSON.parse(data));
});

// Handle close combat message
socket.on('close-combat', (data) => {

    // Call corresponding function with parsed data
    closeCombatReceived(JSON.parse(data));
});

// Handle civ move message
socket.on('civ-move', (data) => {

    // Call corresponding function with parsed data
    civMoveReceived(JSON.parse(data));
});

// Handle civ return message
socket.on('civ-return', (data) => {

    // Call corresponding function with parsed data
    selectCHMRNodes(JSON.parse(data));
});

// Handle initiative ready message
socket.on('initiative-ready', (data) => {

    // Parse data and update game state
    gameState = JSON.parse(data);

    // Update board
    updateBoard();

    // Update initiative button text
    initiativeText.text = "Click to roll for Initiative";

    // Show initiative button
    initiativeButton.visible = initiativeText.visible = true;
});

// Handle new turn message
socket.on('new-turn', (data) => {

    // Parse data and update game state
    gameState = JSON.parse(data);

    // Update board
    updateBoard();

    // If this is the new turn player, open CP menu and clear action banner
    if (gameState.turnPlayer === playerColor){
        Board.openCPQuery();
        updateActionBanner();
    }

    // Update turn banner
    updateTurnBanner();
});

// Handle game over message
socket.on('game-over', (data) => {
    
    // Call corresponding function with parsed data
    gameOverReceived(JSON.parse(data));
});



// ---------- Helper Functions for Socket Handlers ----------

// Function to create display for initiative message
function initiativeReceived (result) {

    // Update game state
    gameState.turnPlayer = result.winner;

    // Draw dice and background
    const dice = [Board.drawDice(415, 370, result.blueRoll, 'blue'), Board.drawDice(510, 370, result.redRoll, 'red')];
    const diceBackground = Board.makeRoundRect(385, 340, 230, 135, 60, 10, 0x808080, 4, 0xffffff);
    app.stage.addChild(...dice, diceBackground);

    // Update initiative result text
    initiativeResultText.text = Objects.upperCaseColor[result.winner] + " wins! Click again to continue";

    // Hide initiative button and show initiative result button
    initiativeButton.visible = initiativeText.visible = false;
    initiativeResultButton.visible = initiativeResultText.visible = true;

    // Give initiative result button a new callback
    initiativeResultButton
        .removeAllListeners()
        .on('pointerdown', () => {

        // Remove dice from screen
        app.stage.removeChild(...dice, diceBackground);

        // Hide the initiative result button
        initiativeResultButton.visible = initiativeResultText.visible = false;
        
        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Waiting on " + gameState.turnPlayer + " to spend Command points");

        // Update the turn banner
        updateTurnBanner();
    });

    // Update turn banner
    updateTurnBanner();
}

// Function to create display for pull result message
function pullResultReceived (result) {

    // Update game state and board
    gameState = result.gameState;
    updateBoard();
    
    // Change action banner if it isn't the player's turn
    if (gameState.turnPlayer !== playerColor) updateActionBanner("Opponent is rolling for CHMR...");

    // Determine y offset
    const yOffset = Math.floor((result.rolls.length + 2) / 3) * 85;

    // Create result box and text
    const chmrBox = Board.makeRoundRect(367.5, 100, 265, 85 + yOffset, 60, 10, 0x808080, 4, 0x000000);
    chmrBox.eventMode = 'static';
    const chmrTextTop = Board.makeBoardText(409, 110, 70, result.removed + " civilian(s) moved", text(22));
    const chmrTextBottom = Board.makeBoardText(417, 145 + yOffset, 70, "Click to Continue", text(22));

    // Create dice
    const dice = Array.from(result.rolls, (x, i) => Board.drawDice(377.5 + (i % 3) * 85, 145 + Math.floor(i / 3) * 85, x, 'black'));

    // Create example box and text
    const exampleBox = Board.makeRoundRect(236, 100, 114, 178, 60, 10, 0x808080, 2, 0x000000);
    const exampleText = Board.makeBoardText(246, 110, 70, "Roll 6:\nMove 2 CIV\n\nRoll 3-5:\nMove 1 CIV\n\nRoll 1-2:\nMove 0 CIV", text(18));

    // Add CHMR elements to canvas
    app.stage.addChild(chmrBox, chmrTextTop, chmrTextBottom, exampleBox, exampleText, ...dice);

    // Add callback to CHMR box click
    chmrBox.on('pointerdown', () => {

        // Remove all CHMR elements
        app.stage.removeChild(chmrBox, chmrTextTop, chmrTextBottom, exampleBox, exampleText, ...dice);

        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Opponent completed CHMR!\nWaiting on them to spend command points...");
    });
}

// Function to create display for humanitarian aid message
function humanitarianAidReceived (result) {

    // Update game state and board
    gameState = result.gameState;
    updateBoard();

    // Change action banner if it isn't the player's turn
    if (gameState.turnPlayer !== playerColor) updateActionBanner("Opponent is rolling for Humanitarian Aid...");

    // Create result box and text
    const aidBox = Board.makeRoundRect(325, 150, 350, 200, 60, 10, 0x808080, 4, 0x000000);
    aidBox.eventMode = 'static';
    const aidText = Board.makeBoardText(372, 160, 70, ((gameState.turnPlayer === playerColor) ? 'You' : 'Opponent') + " rolled a " + result.result + ".\n+" + (result.result > 4 ? '1' : '0') + " Support Tracker Points\n\n\n\n\nClick to Continue", text(22));

    // Create die
    const die = Board.drawDice(462.5, 220, result.result, 'black');

    // Add humanitarian aid elements to canvas
    app.stage.addChild(aidBox, aidText, die);

    // Add callback to humanitarian aid box click
    aidBox.on('pointerdown', () => {

        // Remove all humanitarian aid elements
        app.stage.removeChild(aidBox, aidText, die);

        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Opponent completed Humanitarian Aid!\nWaiting on them to spend command points...");
    });
}

// Function to create display for influence operation message
function influenceOperationReceived (result) {

    // Update game state and board
    gameState = result.gameState;
    updateBoard(gameState);

    // Change action banner if it isn't the player's turn
    if (gameState.turnPlayer !== playerColor) updateActionBanner("Opponent is rolling for Influence Operations...");

    // Retrieve rolls from result
    const rolls = result.result;

    // Determine y offset
    const yOffset = Math.floor((rolls.length + 2) / 3) * 85;

    // Create result box and text
    const ioBox = Board.makeRoundRect(351, 100, 298, 85 + yOffset, 60, 10, 0x808080, 4, 0x000000);
    ioBox.eventMode = 'static';
    const ioTextTop = Board.makeBoardText(366, 110, 70, "- " + rolls.filter(x => x === 6).length + " opponent support points", text(22));
    const ioTextBottom = Board.makeBoardText(417, 145 + yOffset, 70, "Click to Continue", text(22));

    // Create dice
    const dice = Array.from(rolls, (x, i) => Board.drawDice(377.5 + (i % 3) * 85, 145 + Math.floor(i / 3) * 85, x, 'black'));

    // Add influence operations elements to canvas
    app.stage.addChild(ioBox, ioTextTop, ioTextBottom, ...dice);

    // Add callback to influence operation box click
    ioBox.on('pointerdown', () => {

        // Remove all influence operation elements
        app.stage.removeChild(ioBox, ioTextTop, ioTextBottom, ...dice);

        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Opponent completed Influence Operation!\nWaiting on them to spend command points...");
    });
}

// Function to create display for fire message
function fireReceived (result) {

    // Update game state and board
    gameState = result.gameState;
    updateBoard();

    // Change action banner if it isn't the player's turn
    if (gameState.turnPlayer !== playerColor) updateActionBanner("Opponent rolled for artillery fires");

    // Create result box and text
    const fireBox = Board.makeRoundRect(335, 150, 330, 290, 60, 10, 0x808080, 4, 0x000000);
    fireBox.eventMode = 'static';
    const fireText = Board.makeBoardText(413, 160, 70, result.casualties.combatCasualties + " Armies Killed\n\n\n\n\n" + result.casualties.civCasualties + " Civilian(s) Killed\n\n\n\n\nClick to Continue", text(22));

    // Create dice
    const combatDice = Array.from(result.rolls.combatRoll, (x, i) => Board.drawDice(377.5 + i * 85, 190, x, gameState.turnPlayer));
    const civDice = Array.from(result.rolls.civRoll, (x, i) => Board.drawDice(377.5 + i * 85, 310, x, 'black'));

    // Create example box and text
    const exampleBox = Board.makeRoundRect(150, 150, 172, 160, 60, 10, 0x808080, 2, 0x000000);
    const exampleText = Board.makeBoardText(160, 160, 70, "Combat Casualties\noccur on rolls\nof 4-6.\n\nCivilian Casualties\noccur on rolls\nof 3-6.", text(18)); 

    // Add fire elements to canvas
    app.stage.addChild(fireBox, fireText, exampleBox, exampleText, ...combatDice, ...civDice);

    // Add callback to fire box click
    fireBox.on('pointerdown', () => {

        // Remove all fire elements
        app.stage.removeChild(fireBox, fireText, exampleBox, exampleText, ...combatDice, ...civDice);

        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Opponent completed Artillery Fires!\nWaiting on them to spend command points...");
    });
}

// Function to create display for strike message
function strikeReceived (result) {

    // Update game state and board
    gameState = result.gameState;
    updateBoard();

    // Change action banner if it isn't the player's turn
    if (gameState.turnPlayer !== playerColor) updateActionBanner("Opponent rolled for air strike");

    // Create result box and text
    const strikeBox = Board.makeRoundRect(390, 150, 220, 290, 60, 10, 0x808080, 4, 0x000000);
    strikeBox.eventMode = 'static';
    const strikeText = Board.makeBoardText(413, 160, 70, result.casualties.combatCasualties + " Armies Killed\n\n\n\n\n" + result.casualties.civCasualties + " Civilian(s) Killed\n\n\n\n\nClick to Continue", text(22));

    // Create dice
    const combatDice = Array.from(result.rolls.combatRoll, (x, i) => Board.drawDice(420 + i * 85, 190, x, gameState.turnPlayer));
    const civDice = Array.from(result.rolls.civRoll, (x, i) => Board.drawDice(420 + i * 85, 310, x, 'black'));

    // Create example box and text
    const exampleBox = Board.makeRoundRect(205, 150, 172, 160, 60, 10, 0x808080, 2, 0x000000);
    const exampleText = Board.makeBoardText(215, 160, 70, "Combat Casualties\noccur on rolls\nof 4-6.\n\nCivilian Casualties\noccur on rolls\nof 5-6.", text(18)); 

    // Add fire elements to canvas
    app.stage.addChild(strikeBox, strikeText, exampleBox, exampleText, ...combatDice, ...civDice);

    // Add callback to strike box click
    strikeBox.on('pointerdown', () => {

        // Remove all strike elements
        app.stage.removeChild(strikeBox, strikeText, exampleBox, exampleText, ...combatDice, ...civDice);

        // Either open the CP menu or change action banner, depending on player's color
        if (gameState.turnPlayer === playerColor) Board.openCPQuery();
        else updateActionBanner("Opponent completed Air Strike!\nWaiting on them to spend command points...");
    });
}

// Function to delegate appropriately based on close combat message
function closeCombatReceived (result) {

    // Destructure result
    const { rolls, next } = result;

    // If there are rolls from a previous close combat
    if (result.rolls) {

        // Display the rolls
        displayRolls(rolls, next, result.minValue);

        // Update game state and board
        gameState = result.gameState;
        updateBoard();

    // If this is the first close combat message
    } else if (next) {
        combatSelect(next, result.minValue);
        doCC(next);

    // If there is no close combat
    } else socket.emit('game', 'civ-move');
}

// Function to animate for civ move message
function civMoveReceived (result) {

    // Retrieve node names
    const node1 = result.result[0].nodeName;
    const node2 = result.result[1].nodeName;

    // Place an arrow over nodes where population change occured
    doArrow(node1);
    if (node1 !== node2) doArrow(node2);

    // Update game state and board
    gameState = result.gameState;
    updateBoard();

    // Change action banner
    updateActionBanner("Civilian Populations Updated!");

    // Display continuation banner to the turn player
    if(gameState.turnPlayer === playerColor) startCHMRReset();
}

// Function to create display for game over message
function gameOverReceived ({ vp, winner, reason, gameState }) {

    // Remove all (potential) pop ups, highlights, menu buttons, etc.
    app.stage.removeChild(...interactiveElements);
    removeHighlight();

    // Create background
    const endBackground = Board.makeRoundRect(150, 50, 700, 500, 100, 10, Objects.backgroundColors[winner], 4, 0x000000);

    // Create winner text and center it
    const winnerText = Board.makeBoardText(0, 80, 110, Objects.winnerTexts[winner], text(45));
    winnerText.x = 498 - (winnerText.width - 5) / 2;

    // Create reason text and center it
    const reasonText = Board.makeBoardText(0, 150, 110, "Reason: " + Objects.reasonTexts[reason], text(30), winner !== 'tie');
    reasonText.x = 500 - (reasonText.width - 1.5) / 2;

    // Helper function to create stats for each player
    const createStats = (player, align, color) => {

        // Define text to be displayed
        const statText = Objects.upperCaseColor[color] + " Player Stats:\nVP: " + vp[color] + "\nCivilian Casualties: " + player.casualties + "\nArmy Casualties: " + (24 - player.totalArmies);

        // Create stats and place according to align
        const stats = Board.makeBoardText(0, 230, 110, statText, makeTextStyle(30, (color === 'blue' ? 0x0D0E47 : 0x6D0000), align));
        stats.x = (align === 'left' ? 530 : 468.5 - stats.width);

        // Return stats
        return stats;
    }

    // Create stats
    const blueStats = createStats(gameState.bluePlayer, 'right', 'blue');
    const redStats = createStats(gameState.redPlayer, 'left', 'red');

    // Create buttons to view game board or go home
    const viewButton = Object.assign(Board.makeRoundRect(180, 440, 290, 80, 110, 30, 0x73DCA1, 2, 0x000000), { eventMode: 'static', cursor: 'pointer'});
    const homeButton = Object.assign(Board.makeRoundRect(530, 440, 290, 80, 110, 30, 0x73DCA1, 2, 0x000000), { eventMode: 'static', cursor: 'pointer'});

    // Create text for buttons
    const viewText = Board.makeBoardText(221, 462, 120, "VIEW BOARD", pieceTextStyle);
    const homeText = Board.makeBoardText(628, 462, 120, "HOME", pieceTextStyle);

    // Compile all display elements into array
    const gameOverDisplay = [endBackground, winnerText, reasonText, blueStats, redStats, viewButton, homeButton, viewText, homeText];

    // Create button to open the game over display
    const backButton = Object.assign(Board.makeRoundRect(20, 20, 136, 80, 110, 30, 0x73DCA1, 2, 0x000000, false), { eventMode: 'static', cursor: 'pointer'});
    const backText = Board.makeBoardText(40, 42, 120, "BACK", pieceTextStyle, false);

    // Add all game over elements to canvas
    app.stage.addChild(...gameOverDisplay, backButton, backText);

    // Add callback to view button to hide game over display and show back button
    viewButton.on('pointerdown', () => {
        gameOverDisplay.forEach(x => x.visible = false);
        backButton.visible = backText.visible = true;
    });

    // Add callback to home button to redirect user home
    homeButton.on('pointerdown', () => {
        window.location.replace('/');
    });

    // Add callback to back button to show game over display and hide back button
    backButton.on('pointerdown', () => {
        gameOverDisplay.forEach(x => x.visible = true);
        backButton.visible = backText.visible = false;
    });
}



// ---------- Button/Banner Declarations ----------

// Create menu button and instructions
const menuButton = Object.assign(Board.makeBoardPiece(15, 15, 25, 25, 10, '/content/button.png'), { eventMode: 'static', cursor: 'pointer' });
const menuInstructions = Object.assign(Board.makeBoardPiece(40, 40, 950, 534, 90, '/content/VVInstructions.jpg'), { visible: false });

// Add event listener to open/close instructions
menuButton.on('pointerdown', () => { menuInstructions.visible = !menuInstructions.visible; });

// Create initiative button and text
const initiativeButton = Object.assign(Board.makeRoundRect(300, 230, 400, 70, 40, 30, 0x77a1b5, 3, 0x426779), { eventMode: 'static', cursor: 'pointer' });
const initiativeText = Board.makeBoardText(318, 247, 50, "Click to roll for Initiative", pieceTextStyle);

// Add event listener to send initiative message and change text
initiativeButton.on('pointerdown', () => {
    initiativeText.text = " Waiting on Opponent...";
    socket.emit('game', 'initiative');
});

// Create initiative result button and text
const initiativeResultButton = Object.assign(Board.makeRoundRect(224, 230, 552, 70, 40, 30, 0x77a1b5, 3, 0x426779, false), { eventMode: 'static', cursor: 'pointer' });
const initiativeResultText = Board.makeBoardText(244, 247, 50, "", pieceTextStyle, false);

// Create action banner and background
const actionBanner = Board.makeBoardText(100, 20, 50, "", text(23));
const actionBackground = Object.assign(new PIXI.Graphics(), { zIndex: 40, alpha: 0.75 });

// Create turn banner and background
const turnBanner = Board.makeBoardText(780, 290, 50, "", text(20));
const turnBackground = Object.assign(new PIXI.Graphics(), { x: 0, y: 0, zIndex: 40 });

// Function to create a menu for actions with one step (humanitarian aid, surge, influence operation)
function createMenu (buttonText, cancelText, bannerText, socketMessage, minCP) {

    // Create button text and center it
    const bText = Board.makeBoardText(0, 260, 80, buttonText, text(35), false);
    const textWidth = (bText.width - 1);
    bText.x = 499 - textWidth / 2;

    // Create background and button
    const background = Board.makeRoundRect(bText.x - 40, 230, textWidth + 80, 160, 60, 10, 0xd0d0d0, 2, 0x000000, false);
    const button = Object.assign(Board.makeRoundRect(bText.x - 20, 250, textWidth + 40, 60, 70, 10, 0x5b3775, 2, 0x000000, false), { eventMode: 'static', cursor: 'pointer' });

    // Create cancel text and center it
    const cText = Board.makeBoardText(0, 340, 80, cancelText, text(20), false);
    const cancelWidth = (cText.width - 0.7);
    cText.x = 500 - cancelWidth / 2;

    // Create cancel button
    const cancel = Object.assign(Board.makeRoundRect(cText.x - 10, 330, cancelWidth + 20, 40, 70, 10, 0xFF0000, 2, 0x000000, false), { eventMode: 'static', cursor: 'pointer' });

    // Compile all menu elements into array
    const elements = [bText, background, button, cText, cancel]

    // Callback to hide menu
    const hideMenu = (send) => {

        // Hide menu and reset action banner
        elements.forEach(x => x.visible = false);
        updateActionBanner();

        // Send socket message or open CP menu depending on which button was clicked
        if (send) socket.emit('game', 'action', { type: socketMessage });
        else Board.openCPQuery();
    };

    // Add event listeners
    button.on('pointerdown', () => { hideMenu(true); });
    cancel.on('pointerdown', () => { hideMenu(false); });

    // Calback to open menu
    const openMenu = () => {

        // Notify player if they don't have enough CP
        if (currentPlayer().cp < minCP) {
            updateActionBanner("Not enough Command Points. Open CP Menu to select a different action.");
            Board.openCPQuery();

        // Otherwise, open menu
        } else {
            elements.forEach(x => x.visible = true);
            updateActionBanner(bannerText);
        }
    };

    // Return list of menu elements and open menu callback
    return { elements, openMenu };
}

// Create humanitarian aid menu
const { elements: humAidMenu, openMenu: openHumAid } = createMenu("Roll for Humanitarian Aid", "Cancel Aid", "Confirm Humanitarian Aid Selection", 'humanitarian-aid', 2)

// Create surge menu
const {elements: surgeMenu, openMenu: openSurge } = createMenu("Confirm Surge", "Cancel Surge", "Confirm Surge Selection", 'surge', 3);

// Create influence operation menu
const {elements: influenceOpMenu, openMenu: openInfluenceOp } = createMenu("Roll for Influence Operation", "Cancel IO", "Confirm Influence Operation\n(Rolls one dice per two civilian casualties)", 'influence-operation', 3);

// Compile all buttons and banners into array
const interactiveElements = [
    menuButton,
    menuInstructions,
    initiativeButton,
    initiativeText,
    initiativeResultButton,
    initiativeResultText,
    actionBanner,
    actionBackground,
    turnBanner,
    turnBackground,
    ...humAidMenu,
    ...surgeMenu,
    ...influenceOpMenu
];

// Add elements to canvas
app.stage.addChild(...interactiveElements);

// Create CP menu (pass array of callbacks to link to button event handlers)
Board.makeCPMenu([moveClicked, chmrClicked, openHumAid, openSurge, openInfluenceOp, firesClicked, strikeClicked], app);



// ---------- Sprite Manipulation Functions ----------

// Function to update the action banner
function updateActionBanner (text) {
    
    // If no text is provided, hide the banner
    if (!text) actionBanner.visible = actionBackground.visible = false;

    // Otherwise, update banner text and redraw banner background
    else {
        actionBanner.text = text;
        actionBanner.visible = actionBackground.visible = true;
        actionBackground
        .clear()
        .roundRect(90, 10, actionBanner.width + 20, actionBanner.height + 20, 10)
        .fill(0x3d52a0);
    }
}

// Updates the player turn banner
function updateTurnBanner () {

    // Update banner text
    turnBanner.text = "You are " + playerColor + " player.\nIt is " + (gameState.turnPlayer === playerColor ? "" : "not ") + "your turn!";

    // Redraw banner background
    turnBackground
        .clear()
        .roundRect(775, 285, turnBanner.width + 10, turnBanner.height + 10, 10)
        .fill(Objects.colors[playerColor]);
}

// Function to highlight nodes and attach callback to highlight sprites
function highlight (nodes, callback, message) {

    // Remove any previous highlights
    removeHighlight();

    // Create array of highlights from array of nodes
    highlightedNodes = Array.from(nodes, x =>
        Object.assign(new PIXI.Graphics(), { zIndex: 15, alpha: 0.5, eventMode: 'static', cursor: 'pointer' })
            .circle(Objects.locations[x].x + 46, Objects.locations[x].y + 32, 57)
            .fill(0xFFDE21)
            .stroke({ width: 3, color: 0xF4BC1C })
            .on('pointerdown', () => callback(x))
    );
    
    // Add highlights to canvas
    app.stage.addChild(...highlightedNodes);

    // Update action banner with given message
    updateActionBanner(message);
}

// Function to remove highlights
function removeHighlight () {

    // If there are no highlighted nodes, just return
    if (!highlightedNodes) return;

    // Otherwise, loop through each highlight and remove it
    highlightedNodes.forEach(x => { app.stage.removeChild(x); });
    
    // Empty the array of highlighted nodes
    highlightedNodes = [];
}
















// Add PixiJS canvas to DOM
document.getElementById("pixi-container").appendChild(app.canvas);

// TRANSPARENT canvas to put on top of pixi canvas.
const fxCanvas = document.createElement("canvas");
fxCanvas.width = app.canvas.width;
fxCanvas.height = app.canvas.height;
fxCanvas.style.position = "absolute";
fxCanvas.style.top = "0";
fxCanvas.style.left = "0";
fxCanvas.style.pointerEvents = "none";
fxCanvas.style.background = "transparent";

// Append it aboe pixi canvas because webgl does not support my draw methods.
document.getElementById("pixi-container").appendChild(fxCanvas);

//asks for the game state after the values in update board have been created
socket.emit('game', 'fetch-game-state');

//locations where the highlight appear on node.
const highLoc = {
    'blueBase': { x: 168, y: 254 },
    'redBase': { x: 802, y: 171 },
    'city4': { x: 592, y: 98 },
    'city6': { x: 302, y: 331 },
    'city9': { x: 421, y: 439 },
    'city10': { x: 636, y: 403 },
    'village2': { x: 306, y: 148 },
    'village3': { x: 453, y: 155 },
    'village7': { x: 487, y: 303 },
    'village8': { x: 645, y: 245 }
}

//returns the gamestate in a numbered format instead of the one with objects
function getCount(initialNodes) {

    const newNodes = {};

    for (const [key, units] of Object.entries(initialNodes)) {

        let redCount = 0;
        let civCount = 0;
        let blueCount = 0;
        let blueMoved = 0;
        let redMoved = 0;

        for (const unit of units) {
            if (unit.type === 'blue') {
                blueCount++;
            } else if (unit.type === 'red') {
                redCount++;
            } else if (unit.type === 'civ') {
                civCount++;
            }
            if (unit.type === 'blue' && unit.hasMoved) {
                blueMoved++;
            }
            if (unit.type === 'red' && unit.hasMoved) {
                redMoved++;
            }
        }
        newNodes[key] = {
            civilians: civCount,
            blueArmies: blueCount,
            redArmies: redCount,
            redMoved: redMoved,
            blueMoved: blueMoved
        };

    }

    return newNodes;
}


/////////////MOVE START
/////////////MOVE START
/////////////MOVE START

//selected nodes for the move function
let selections = [];
let find = [];

function moveClicked() {
    //find the clickable nodes based on player
    //call the highlight function with the nodes

    if(gameState['bluePlayer'].cp < 1 || gameState['redPlayer'].cp < 1){
    // if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 1) {
        updateActionBanner("Not enough Command Points. Open CP Menu to select a different action.");
        Board.openCPQuery();
        return;
    }
    find = Board.findNodes(playerColor, getCount(gameState.nodes));
    cancelMove();
    highlight(find, move1, "Pick the node where your first army will move from");
}

//lets the user select one or two nodes to move from and sends them to the server
function move1(name) {
    selections[selections.length] = name;
    hideCancelMove();
    if (selections.length === 2) {
        socket.emit('game', 'action', { type: 'move-select', data: selections });
        removeHighlight();
        updateActionBanner();
        hideDone();
    }

    let color = {
        'blue':'blueArmies',
        'red':'redArmies'
    };
    let colorMoved = {
        'blue':'blueMoved',
        'red':'redMoved'
    };

    if (selections.length === 1) {
        //give the user the option to pick a second node or just send one. 
        let test = getCount(gameState.nodes);
        if(test[name][color[playerColor]] - test[name][colorMoved[playerColor]] == 1)
            find = find.filter(item => item != name);

        showDone();
        highlight(find, move1, "You picked " + name + ". Click done to continue or\nselect a second node to move from.");
    }
}

let nodeList2 = [];

let selections2 = [];
function moveReceived([nodes1, nodes2]) {

    highlight(nodes1, move2, "Pick the node where army from " + selections[0] + " should go");
    nodeList2 = nodes2;

}

//lets the user select the nodes that the armies are moving to. 
function move2(name) {
    selections2[selections2.length] = name;
    if (selections2.length == selections.length) {
        socket.emit('game', 'action', { type: 'move-confirm', data: selections2 });
        removeHighlight();
        updateActionBanner();
        //clears the selections 
        selections = [];
        selections2 = [];
    }

    //allows the user to pick the second node to move from if they selected two nodes originally. 
    else {
        highlight(nodeList2, move2, "You picked " + name + ". Choose where the army from " + selections[1] + " should go.");
    }
}


//done buttons that end the move after one node is selected if clicked
const doneText = Board.makeBoardText(100, 120, 1000, "Done", makeTextStyle(20, 0x000000, 'center'), false);

const doneButton = Board.makeRoundRect(90, 110, doneText.width + 20, doneText.height + 20, 60, 10, '0x00FF00', 1, 0x000000, false)
doneButton.alpha = 0.75;
doneButton.eventMode = 'static';
//sends the one node to the server if clicked
doneButton.on('pointerdown', () => {
    socket.emit('game', 'action', { type: 'move-select', data: selections });
    removeHighlight();
    updateActionBanner();
    hideDone();
    hideCancelMove();

});

app.stage.addChild(doneText, doneButton);

function showDone() {
    doneButton.visible = true;
    doneText.visible = true;
}

function hideDone() {
    doneButton.visible = false;
    doneText.visible = false;
}



//button that cancels move if clicked. opens cp tracker
const cancelMoveText = Board.makeBoardText(100, 110, 1000, "Cancel", text(20), false);

const cancelMoveButton = Board.makeRoundRect(90, 100, cancelMoveText.width + 20, cancelMoveText.height + 20, 60, 10, '0xFF0000', 1, 0x000000, false);
cancelMoveButton.alpha = 0.75;
cancelMoveButton.eventMode = 'static';
cancelMoveButton.on('pointerdown', () => {
    removeHighlight();
    updateActionBanner();
    Board.openCPQuery();
    hideCancelMove();
});

app.stage.addChild(cancelMoveText, cancelMoveButton);

function cancelMove() {
    cancelMoveButton.visible = true;
    cancelMoveText.visible = true;
}

function hideCancelMove() {
    cancelMoveButton.visible = false;
    cancelMoveText.visible = false;
}

////End Move
////End Move
////End Move

/////CHMR START
/////CHMR START
/////CHMR START
//first step, create the highlight locations for the safe havens
/////locations defined as path in format path: [x1,y1,x2,y2...] counterclockwise
const havenLoc = {
    'haven1': { path: [219, 280, 252, 304, 296, 271, 302, 208, 259, 183, 214, 218] },
    'haven2': { path: [306, 207, 301, 271, 333, 283, 413, 195, 397, 155, 363, 153] },
    'haven3': { path: [337, 283, 356, 320, 427, 306, 470, 245, 461, 214, 415, 200] },
    'haven4': { path: [345, 368, 379, 395, 445, 386, 460, 357, 428, 317, 357, 324] },
    'haven5': { path: [506, 182, 591, 220, 627, 192, 607, 157, 537, 118, 507, 129] },
    'haven6': { path: [474, 244, 542, 284, 590, 270, 589, 224, 503, 187, 466, 214] },
    'haven7': { path: [535, 334, 588, 368, 647, 344, 657, 304, 594, 278, 544, 291] },
    'haven8': { path: [451, 389, 477, 424, 577, 421, 585, 371, 533, 339, 465, 357] },
    'haven9': { path: [629, 186, 699, 224, 747, 197, 757, 134, 651, 109, 612, 154] },
    'haven10': { path: [651, 348, 687, 378, 787, 229, 751, 201, 701, 231, 660, 303] }
}

const highlightedHavens = {};


function highlightHavens(names, callback, message) {
    removeHighlight();
    for (const name of names) {
        const select = new PIXI.Graphics();
        select.poly(havenLoc[name].path);
        select.fill(0xFFDE21)
        select.alpha = 0.5;
        select.stroke({ width: 3, color: 0xF4BC1C })
        select.eventMode = 'static';
        select.zIndex = 75;
        select.on('pointerdown', () => callback(name))
        app.stage.addChild(select)
        highlightedHavens[name] = select;
    }
    updateActionBanner(message);
}

function removeHavenHighlight() {
    for (var member in highlightedHavens) {
        app.stage.removeChild(highlightedHavens[member]);
        delete highlightedHavens[member];
    }
}

let selectedHavens = "none";
function chmrClicked() {

    if(gameState['bluePlayer'].cp < 2 || gameState['redPlayer'].cp < 2){
    // if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 1) {
        updateActionBanner("Not enough Command Points. Open CP Menu to select a different action.");
        Board.openCPQuery();
        return;
    }

    cancelCHMR();

    var find = Board.findNodes(playerColor, getCount(gameState.nodes), false);
    highlight(find, chmr1, "Pick the node where CHMR will be conducted from");
}

function chmr1(name) {
    // selectedNodes[selectedNodes.length] = name;
    let numbers = getCount(gameState.nodes);
    let maxVal = 0;
    if (playerColor == 'blue') {
        maxVal = numbers[name].blueArmies;
    }
    else
        maxVal = numbers[name].redArmies;

    console.log("sending the name to server " + name + " " + maxVal + "armies");
    // socket.emit('game', 'action', { type: 'chmr-select', data: name });

    let counter = selectArmyNumber(400, 200, 1, maxVal, sendCHMRMessage1, name);
    updateActionBanner("Select the number of armies to conduct CHMR");
    app.stage.addChild(counter);

    removeHighlight();
    hideCancelCHMR();

}

function sendCHMRMessage1(nodeName, selectedNumber) {
    console.log(nodeName + " " + selectedNumber);
    socket.emit('game', 'action', { type: 'chmr-select', data: {node:nodeName, armies:selectedNumber}});
    updateActionBanner();
}

function selectArmyNumber(x, y, minValue, maxValue, onSelect, nodeName) {

    const container = new PIXI.Container();
    container.position.set(x, y);
    container.zIndex = 1001;

    let selectedNumber = 1;

    const text = new PIXI.Text({ text: selectedNumber, style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' }) });
    text.anchor.set(0.5);
    text.zIndex = 10;
    text.position.set(70, 30);

    const background = Board.makeRoundRect(0,0,140,100,0,10,0x000000,0,0x000000);

    const plusButton = new PIXI.Text({ text: '+', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' }) })
    plusButton.anchor.set(0.5);
    plusButton.position.set(120, 30);
    plusButton.interactive = true;
    plusButton.zIndex = 10;
    plusButton.buttonMode = true;
    plusButton.on('pointerdown', () => {
        if (selectedNumber < maxValue) {
            selectedNumber++;
            text.text = selectedNumber;
        }
    });

    const minusButton = new PIXI.Text({ text: '-', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' }) })
    minusButton.anchor.set(0.5);
    minusButton.position.set(20, 30);
    minusButton.zIndex = 10;
    minusButton.interactive = true;
    minusButton.buttonMode = true;
    minusButton.on('pointerdown', () => {
        if (selectedNumber > minValue) {
            selectedNumber--;
            text.text = selectedNumber;
        }
    });

    const confirmButton = new PIXI.Text({ text: 'OK', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' }) })
    confirmButton.anchor.set(0.5);
    confirmButton.position.set(70, 70);
    confirmButton.interactive = true;
    confirmButton.buttonMode = true;
    confirmButton.zIndex = 10;
    confirmButton.on('pointerdown', () => {
        // onSelect(selectedNumber);
        onSelect(nodeName, selectedNumber);
        container.visible = false; // Hide the selector after selection
    });

    container.addChild(minusButton, text, plusButton, confirmButton, background);
    return container;
}



function chmrReceived(havens) {
    highlightHavens(havens, chmr2, "Which safe haven would you like to move to?");
}


function chmr2(name) {
    //lets the user select the nodes that the armies are moving to. 
    selectedHavens = name;
    console.log("Selected haven " + selectedHavens);
    socket.emit('game', 'action', {type: 'chmr-haven', data: name}); // Request to conduct CHMR at haven1
    removeHavenHighlight();
    updateActionBanner();

}




function chmr2Recived(nodes) {
    highlight(nodes, chmr3, "Select the node to move civilians from");
}

function chmr3(name) {
    removeHighlight();
    updateActionBanner();
    console.log("selected ndoe" + name);
    //////////server message goes here. emiittttt
    socket.emit('game', 'action', {type: 'chmr-pull', data: name}); // Request to pull civilians from name

}



const cancelCHMRText = new PIXI.Text({ text: "Cancel CHMR", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
const cancelCHMRButton = Board.makeRoundRect(90,100,cancelCHMRText.width + 20,cancelCHMRText.height + 20,60,10,'0xFF0000',2,0x000000,false);
cancelCHMRButton.alpha = 0.75;
cancelCHMRButton.eventMode = 'static';
cancelCHMRButton.on('pointerdown', () => {
    removeHighlight();
    updateActionBanner();
    Board.openCPQuery();
    hideCancelCHMR();
});

cancelCHMRText.x = 100;
cancelCHMRText.y = 110;
cancelCHMRText.zIndex = 1000;
cancelCHMRText.visible = false;
app.stage.addChild(cancelCHMRText,cancelCHMRButton);


function cancelCHMR() {
    cancelCHMRButton.visible = true;
    cancelCHMRText.visible = true;
}

function hideCancelCHMR(){
    cancelCHMRButton.visible = false;
    cancelCHMRText.visible = false;
}


//CHMR END
//CHMR END
//CHMR END


///ARTILLERY FIRES START
///ARTILLERY FIRES START

function firesClicked() {
    if(gameState['bluePlayer'].cp < 1 || gameState['redPlayer'].cp < 1){
        updateActionBanner("Not enough Command Points. Open CP Menu to select a different action.");
        Board.openCPQuery();
        return;
    }
    var find = Board.findNodes(playerColor, getCount(gameState.nodes), false);
    cancelFireButton.visible = true;
    cancelFireText.visible = true;
    highlight(find, fires1, "Pick the node where your army will fire from");
}


function fires1(name) {
    hideCancelFires();
    removeHighlight();
    updateActionBanner();
    socket.emit('game', 'action', { type: 'artillery-select', data: name }); // Request to fire from blue base
}



const cancelFireText = new PIXI.Text({ text: "Cancel Fire", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
const cancelFireButton = Board.makeRoundRect(90, 100, cancelFireText.width + 20, cancelFireText.height + 20, 60,10,0xff0000,2,0x000000,false);
cancelFireButton.eventMode = 'static';
cancelFireButton.on('pointerdown', () => {
    removeHighlight();
    updateActionBanner();
    Board.openCPQuery();
    hideCancelFires();
});

cancelFireText.x = 100;
cancelFireText.y = 110;
cancelFireText.zIndex = 70;
cancelFireText.visible = false;
app.stage.addChild(cancelFireText,cancelFireButton);


function hideCancelFires(){
    cancelFireButton.visible = false;
    cancelFireText.visible = false;
}

function fires2(name) {

    socket.emit('game', 'action', { type: 'artillery-confirm', data: name }); // Request to fire on village2
    removeHighlight();
    updateActionBanner();
}



//ARTILLERY FIRES END
//ARTILLERY FIRES END
//ARTILLERY FIRES END

///////AIR STRIKE START
///////AIR STRIKE START
///////AIR STRIKE START

function strikeClicked() {
    if(gameState['bluePlayer'].cp < 2 || gameState['redPlayer'].cp < 2){
        updateActionBanner("Not enough Command Points. Open CP Menu to select a different action.");
        Board.openCPQuery();
        return;
    }

    var find = Board.findNodes(('blue' == playerColor ? 'red' : 'blue'), getCount(gameState.nodes), false);
    cancelStrike();
    highlight(find, strike1, "Pick the node you would like to attack");
}


function strike1(name) {
    hideCancelStrike();
    socket.emit('game', 'action', { type: 'air-strike', data: name });
    removeHighlight();
    updateActionBanner();

}




const cancelStrikeText = new PIXI.Text({ text: "Cancel Strike", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
const cancelStrikeButton = Board.makeRoundRect(90, 100, cancelStrikeText.width + 20, cancelStrikeText.height + 20,60, 10,0xff0000,2,0x000000,false);
cancelStrikeButton.eventMode = 'static';
cancelStrikeButton.on('pointerdown', () => {
    // console.log("cancel strike!");
    removeHighlight();
    updateActionBanner();
    Board.openCPQuery();
    hideCancelStrike();
});

cancelStrikeText.x = 100;
cancelStrikeText.y = 110;
cancelStrikeText.zIndex = 70;
cancelStrikeText.visible = false;
app.stage.addChild(cancelStrikeText,cancelStrikeButton);


function cancelStrike() {
    cancelStrikeButton.visible = true;
    cancelStrikeText.visible = true;
}

function hideCancelStrike(){
    cancelStrikeButton.visible = false;
    cancelStrikeText.visible = false;
}

//AIR STRIKE END
//AIR STRIKE END
//AIR STRIKE END


//////CLOSE COMBAT START
//////CLOSE COMBAT START
//////CLOSE COMBAT START
// function closeCombat(){



function displayRolls(rolls, next, minValue = 0) {

    let playerRolls = [];
    let otherPlayerRolls = [];
    let playerCivRolls = [];
    let otherPlayerCivRolls = [];

    if (gameState.turnPlayer == 'blue') {
        playerRolls = rolls.blueRolls;
        otherPlayerRolls = rolls.redRolls;
        playerCivRolls = rolls.blueCivRolls;
        otherPlayerCivRolls = rolls.redCivRolls;
    }
    else {
        otherPlayerRolls = rolls.blueRolls;
        playerRolls = rolls.redRolls;
        otherPlayerCivRolls = rolls.blueCivRolls;
        playerCivRolls = rolls.redCivRolls;
    }

    let height = 3;
    if (rolls.blueCivRolls && rolls.redCivRolls) {
        height = 4;
    }

    height = 95 * height + 20 * height + 20;

    let length = (rolls.blueRolls.length > rolls.redRolls.length) ? rolls.blueRolls.length : rolls.redRolls.length;
    length = length * 95 + 20;
    if (length < 211) length = 250;

    const rollDisplay = Board.makeRoundRect(290, 160, length, height, 60, 10, 0x808080, 3, 0xffffff, true);
    for (let i = 0; i < playerRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 195, playerRolls[i], gameState.turnPlayer);
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }
    for (let i = 0; i < otherPlayerRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 305, otherPlayerRolls[i], (gameState.turnPlayer == 'blue' ? 'red' : 'blue'));
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }

    for (let i = 0; i < playerCivRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 415, playerCivRolls[i], gameState.turnPlayer);
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }
    if (otherPlayerCivRolls) {
        for (let i = 0; i < otherPlayerCivRolls.length; i++) {
            let dice = Board.drawDice(310 + i * 95, 525, otherPlayerCivRolls[i], (gameState.turnPlayer == 'blue' ? 'red' : 'blue'));
            app.stage.addChild(dice);
            rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
        }
    }
    let text1 = Board.makeBoardText(310, 170, 70, gameState.turnPlayer + " rolled for armies:", text(22));
    let text2 = Board.makeBoardText(310, 280, 70, (gameState.turnPlayer == 'blue' ? 'red' : 'blue') + " rolled for armies:", text(22));
    let text3 = Board.makeBoardText(310, 390, 70, gameState.turnPlayer + " rolled for civilians:", text(22));
    let cont = Board.makeBoardText(310, 500, 70, "Click to continue", text(22));
    let text4 = "none";
    if (otherPlayerCivRolls) { text4 = Board.makeBoardText(310, 500, 70, (gameState.turnPlayer == 'blue' ? 'red' : 'blue') + " rolled for civilians:", text(22)); }
    if (text4 != "none") {
        app.stage.addChild(text4);
        cont.y = 610;
    }
    app.stage.addChild(text1, text2, text3, cont);

    const combatEx = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center',wordWrap: true, wordWrapWidth: 200 }) });
    combatEx.x = 90;
    combatEx.y = 170;
    combatEx.zIndex = 70;
    // strikeText.text = "X Armies Killed" + "\n\n\n\n\nY Civilians Killed\n\n\n\n\nClick to Continue"
    combatEx.text = "Army casualties occur on rolls of 5-6.\nCIV casualties occur on rolls of 6."
    app.stage.addChild(combatEx);

    const combatRollEx = Board.makeRoundRect(80, 160, combatEx.width+20, combatEx.height + 20, 60,10,0x808080,2,0xffffff);
    app.stage.addChild(combatRollEx);

    rollDisplay.eventMode = 'static';
    rollDisplay.on('pointerdown', () => {
        app.stage.removeChild(rollDisplay,combatEx, combatRollEx);
        app.stage.removeChild(text1, text2, text3, cont);
        if (text4 != "none") app.stage.removeChild(text4);
        if (next) {
            combatSelect(next,minValue);
            doCC(next); 
        }
        else {
            socket.emit('game', 'civ-move');
            console.log("send civ-move");
        }
    })

    app.stage.addChild(rollDisplay);

}

function combatSelect(next, minValue) {
    console.log("In combat select"+next);
    highlight([next], () => {}, "Select Number of Armies for close combat in " + next);

    let numbers = getCount(gameState.nodes);
    let maxVal = 0;
    if (playerColor == 'blue') {
        maxVal = numbers[next].blueArmies;
    }
    else
        maxVal = numbers[next].redArmies;

    let counter = selectArmyNumber(highLoc[next].x - 57, highLoc[next].y + 100, minValue, maxVal, closeCombatMessage, next);

    app.stage.addChild(counter);
}

function closeCombatMessage(name,number) {
    removeHighlight();
    // console.log(number + " " + name);
    updateActionBanner();
    console.log(number);
    socket.emit('game', 'close-combat',number);

}


//CLOSE COMBAT ANIMATION FUNCTION! 


function closeCombatAnim(ctx, originX, originY, duration) {
    const m4 = new Image();
    //  m4.src = "/content/m4.png";
    m4.src = "/content/m4_3.png";

    const RIFLE_WIDTH = 85 * 1.25;
    const RIFLE_HEIGHT = 40 * 1.25;
    const HALF_WIDTH = RIFLE_WIDTH / 2;
    const HALF_HEIGHT = RIFLE_HEIGHT / 2;

    let angle = 0;
    let clashed = false;
    // socket.emit('civ-move');
    let leftX = 0, rightX = 0;
    let ringActive = false;
    let ringRadius = 0;
    let ringOpacity = 0;

    function drawRifle(x, flip = false, rot = 0) {
        ctx.save();
        ctx.translate(originX + x, originY);
        if (flip) ctx.scale(-1, 1);
        ctx.rotate(rot);
        ctx.drawImage(m4, -HALF_WIDTH, -HALF_HEIGHT, RIFLE_WIDTH, RIFLE_HEIGHT);
        ctx.restore();
    }

    function drawRing() {
        if (!ringActive) return;
        ctx.beginPath();
        ctx.arc(originX, originY, ringRadius, 0, 2 * Math.PI);
        ctx.strokeStyle = `rgba(255, 60, 0, ${ringOpacity.toFixed(2) * 4})`;
        ctx.lineWidth = 3;
        ctx.stroke();
        ringRadius += 1.7;
        ringOpacity -= 0.03;
        if (ringOpacity <= 0) {
            ringActive = false;
            ringOpacity = 0;
        }
    }

    function animate() {
        // Clear just the relevant region around the clash
        ctx.clearRect(originX - 100, originY - 100, 200, 200);

        if (!clashed) {
            leftX = -60 + angle * 4;
            rightX = 60 - angle * 4;
        }

        drawRifle(leftX, false, Math.PI / 4);
        drawRifle(rightX, true, Math.PI / 4);
        drawRing();

        if (!clashed && leftX >= rightX - 10) {
            clashed = true;
            ringActive = true;
            ringRadius = 4;
            ringOpacity = 1;
        }

        if (!clashed || ringActive) {
            if (!clashed) angle += 0.35;
            requestAnimationFrame(animate);
        }
    }

    m4.onload = () => {
        animate();

        setTimeout(() => {
            ctx.clearRect(originX - 100, originY - 100, 200, 200);
        }, duration);
    };
}

function doCC(location) { 
    // 257, y: 299 },
    var x = highLoc[location].x
    var y = highLoc[location].y

    const fxCtx = fxCanvas.getContext("2d");
    closeCombatAnim(fxCtx, x+10, y+10, 3500); //PLACEHOLDER 
}

////CLOSE COMBAT FINISH
////CLOSE COMBAT FINISH
////CLOSE COMBAT FINISH


///ANIMATION FOR RESET

function playBobbingArrow(ctx, originX, originY, duration) {
    const arrow = new Image();
    arrow.src = "/content/arrow.png";

    const WIDTH = 80 * 1.2;
    const HEIGHT = 72 * 1.2;

    let opacity = 1;
    let bobOffset = 0;
    let direction = 1;

    function draw() {
        ctx.save();
        ctx.globalAlpha = opacity;
        ctx.drawImage(arrow, originX - WIDTH / 2, originY - HEIGHT / 2 + bobOffset, WIDTH, HEIGHT);
        ctx.restore();
    }

    function animate() {
        ctx.clearRect(originX - 50, originY - 50, 100, 100);

        draw();

        // Bobbing motion
        bobOffset += direction * 0.3;
        if (Math.abs(bobOffset) > 5) direction *= -1;

        // Fade out
        opacity -= 0.005;
        if (opacity > 0) {
            requestAnimationFrame(animate);
        }
    }

    arrow.onload = () => {
        animate();

        // Final cleanup (optional)
        setTimeout(() => {
            ctx.clearRect(originX - 50, originY - 50, 100, 100);
        }, duration);
    };
}


//usage doArrow(String locationName);
function doArrow(location) { 
   // console.log(locations[location].x);
    var x = Objects.locations[location].x
    var y = Objects.locations[location].y

    const fxCtx = fxCanvas.getContext("2d");
    playBobbingArrow(fxCtx, x+55, y-5, 10000); // Show arrow at (150, 100) for 2 seconds
}

//END ANIMATION FOR RESET


////CHMR RESET START
////CHMR RESET START
////CHMR RESET START

function startCHMRReset(){
    const chmrResetText = Board.makeBoardText(260,510,70,"Civilian Movement Complete,\nClick to Continue",text(30));
    const chreset = Board.makeRoundRect(250,500,chmrResetText.width+20,chmrResetText.height+20,60,10,0x3d52a0,2,0x000000,true);

    app.stage.addChild(chmrResetText,chreset);

    chreset.eventMode='static';
    chreset.on('pointerdown',()=>{
        app.stage.removeChild(chmrResetText,chreset);
        socket.emit('game','civ-return');
        updateActionBanner("Waiting on Opponent to Spend Command Points.");
    });

}



let selectedReturns = [];
let location = 0;
let toMove = [];
let updateCiv = [];


function selectCHMRNodes({civNodes, havenList, gameState}){
    

    toMove = havenList;
    updateCiv = civNodes;



    if (havenList.length > selectedReturns.length && playerColor == gameState.turnPlayer){
        console.log("start");
        console.log(havenList[0].adjacent);
        highlight(havenList[0].adjacent,resetResponse, "Select where the army from " + havenList[0].haven + " should go");
    }
    else if (playerColor == gameState.turnPlayer){
        console.log("in the else statement");
        const chmrResetText = Board.makeBoardText(260,510,70,"No CHMR Conducted,\nClick to Continue",text(30));
        const chreset = Board.makeRoundRect(250,500,chmrResetText.width+20,chmrResetText.height+20,60,10,0x808080,2,0x000000,true);

        app.stage.addChild(chmrResetText,chreset);

        chreset.eventMode='static';
        chreset.on('pointerdown',()=>{
            app.stage.removeChild(chmrResetText,chreset);
            console.log("this is where I would start the next round");
        });
    }
}

function resetResponse(name){
    removeHighlight();
    selectedReturns[location] = {
        haven:toMove[location].haven,
        node:name
    }
    if(toMove.length > selectedReturns.length){
        location+=1;
        highlight(toMove[location].adjacent,resetResponse, "Select where the army from " + toMove[location].haven + " should go");
    }
    else{
        for(let nodes of updateCiv){
            doArrow(nodes);
        }

        socket.emit('game','civ-return',selectedReturns);

        const chmrResetText = Board.makeBoardText(260,510,70,"Civilian Return from Haven Complete,\nClick to Continue",new PIXI.TextStyle({ fontSize: 30, fill: '#ffffff', align: 'center' }));
        chmrResetText.zIndex=1101;
        const chreset = Board.makeRoundRect(250,500,chmrResetText.width+20,chmrResetText.height+20,60,10,0x3d52a0,2,0x000000,true);

        app.stage.addChild(chmrResetText,chreset);

        chreset.eventMode='static';
        chreset.on('pointerdown',()=>{
            app.stage.removeChild(chmrResetText,chreset);
            console.log("this is where i send the selected nodes");
            console.log(selectedReturns);
            selectedReturns = [];
            location = 0;
            toMove = [];
            updateCiv = [];
            updateActionBanner();
        });
        
    }
}