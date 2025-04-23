import { makeTextStyle } from './moreFunctions.js';

// List of number names used for retrieving all dice asset names
const numbers = ['one', 'two', 'three', 'four', 'five', 'six'];

// Function to retrieve all six dice asset names of a given color
function getDice (color) {
    return Array.from(numbers, (x) => '/content/' + color + '_' + x + '_die.png');
}

// Variable to hold all dice asset names
const colors = {
    'blue': getDice('blue'),
    'red': getDice('red'),
    'black': getDice('black')
}

// Function to load all necessary assets
export async function loadAssets () {

    // List of all asset names in the 'content' directory
    const assetNames = [
        'backgroundVV.png', // Background blue picture
        'Board_Circles.png', // Board picture with the nodes
        'button.png', // Menu button in top left of screen
        'blueGrave.png', // Blue grave
        'redGrave.png', // Red grave
        'redCPTracker.png', // Tracker for red command points
        'blueCPTracker.png', // Tracker for blue command points
        'IFV_Blue.png', // Blue tank
        'IFV_Red.png', // Red tank
        'blueSurgeNew.png', // Blue surge photo
        'redSurgeNew.png', // Red surge photo
        'civilians2.png', // Civilians photo
        'VVInstructions.jpg', // Powerpoint instructions that appear from menu button.
        'support_tracker.png', // Support tracker (bottom of screen)
    ];

    // Load all assets
    await PIXI.Assets.load([
        ...Array.from(assetNames, (x) => '/content/' + x),
        ...colors['blue'],
        ...colors['red'],
        ...colors['black']
    ]); 
}

// Function to create and return background sprite
export function makeBackground() {
    return Object.assign(PIXI.Sprite.from('/content/backgroundVV.png'), { width: 970, height: 634 });
}

// Function to create and return game board sprite
export function makeGameBoard() {
    const board = Object.assign(PIXI.Sprite.from('/content/Board_Circles.png'), { x: 110, y: 40, width: 750, height: 456 });

    // Prints the location of a click in console
    // board.eventMode = 'static';
    // board.on('pointerdown', (e) => {
    //     console.log(e.global);
    // });

    return board;
}

// Function to create and return a given piece
export function makeBoardPiece (x, y, width, height, name, zIndex = 0) {
    return Object.assign(PIXI.Sprite.from(name), { x, y, width, height, zIndex });
}

// Function to create and return a circle
export function makeCircle (x, y, r, fill, stroke, strokefill, alpha) {
    return Object.assign(new PIXI.Graphics(), { alpha })
        .circle(x, y, r)
        .fill(fill)
        .stroke(stroke, strokefill);
}

// Function to create and return a given text sprite
export function makeBoardText (x, y, text, style) {
    return Object.assign(new PIXI.Text({ text, style }), { x, y });
}

// Function to create and return a round rectangle
export function makeRoundRect (x, y, w, h, corner, fill, width, color, zIndex, visible = true) {
    return Object.assign(new PIXI.Graphics(), { zIndex, visible })
        .roundRect(x, y, w, h, corner)
        .fill(fill)
        .stroke({ width, color });
}

// Function to create and return a dice 
export function drawDice (x, y, zIndex, n, color) {
    return Object.assign(PIXI.Sprite.from(colors[color][n-1]), { x, y, zIndex, width: 75, height: 75 });
}

// Create CP menu
const cpMenu = Object.assign(new PIXI.Container(), { zIndex: 200, visible: false });

// Add background to CP menu
cpMenu.addChild(
    new PIXI.Graphics()
        .roundRect(300, 100, 400, 300, 10)
        .fill({ color: 0, alpha: 0.85 })
        .stroke(2, 0xffffff)
);

// Create CP button
const cpButton = Object.assign(new PIXI.Graphics(), { zIndex: 90, alpha: 0.7, evenMode: 'static', visible: false })
    .roundRect(780, 40, 145, 33, 10)
    .fill(0x000000)
    .stroke(10, 0x101010)
    .on('pointerdown', () => {
        if (cpMenu.visible) closeCPQuery();
        else openCPQuery();            
    });

// Create CP button text
const cpButtonText = Object.assign(new PIXI.Text({ text: "Open CP Menu", style: makeTextStyle('normal', 20, '#ffffff', 'center') }), { x: 790, y: 45, zIndex: 100, visible: false });

// Function to populate and return CP menu
export function makeCPMenu (actionFunctions, app) {

    const actionText = makeTextStyle('normal', 18, '#ffffff', 'center');

    const descriptionStyle = new PIXI.TextStyle({
        fontSize: 14,
        fill: 0x000000,
        wordWrap: true,
        wordWrapWidth: 200
    });

    app.stage.addChild(cpButtonText,cpButton);
    
    const cpQueryText = new PIXI.Text({ text: "Spend CP?", style: new PIXI.TextStyle({ fontSize: 30, fill: '#FFFFFF', align: 'center' }) });
    cpQueryText.x = 420;
    cpQueryText.y = 130;
    cpMenu.addChild(cpQueryText);

    // Action buttons
    const actions = [
        "Move [1]", "CHMR[2]", "Humanitarian Aid [2]", "Surge [3]", "Influence Operation [3]", "Artillery Fires [1]", "Air Strike [2]"
    ];

    const actionDescription = [
        "Move up to two Armies to an adjacent node. The two Armies do not have to travel to the same node. Armies cannot move through a space occupied by the opponent. Each Army can only move once per turn.",
        "Target one node for CHMR. Select the number of armies to move to the selected temporary haven adjacent to the targeted node. Roll 1 d6 for each Army. Based on the roll, the CIV value will be decreased from the node and added to the temporary haven.\nRoll 6: 2 CIV\nRoll 5-3: 1 CIV\nRoll 1-2: 0 CIV",
        "Roll 1 d6. On a roll of 6, increase your Influence by 1 on the Support Tracker. If your Support Marker is already on 6, nothing happens",
        "Add an additional 4 Armies to your team's base from your \"Available to Surge\" box. This action can only be used twice during the game.",
        "Roll 1 d6 for every 2 Civilians your opponent has in their Civilian casualties space. For each 6 rolled, reduce the opponent's influence on Support Tracker by one",
        "Select an Army to fire, roll 3 d6 to target an adjacent node.\nFirst Roll - Casualties occur on rolls of 4-6.\nSecond Roll - Civ Casualties occur on rolls of 3-6.",
        "Roll 2 d6 to air strike any node on the map.\nFirst Roll - Combat casualties occur on rolls of 4-6.\nSecond Roll - Civ Casualties occur on rolls of 5-6."
    ];



    const buttons = [];
    for (let i = 0; i < actions.length; i++) {
        let actionButton = new PIXI.Graphics();
        actionButton.roundRect(0, 0, 180, 40, 5);
        actionButton.fill(0x333333);

        actionButton.x = (i % 2 === 0) ? 310 : 510; // Left and right column
        actionButton.y = 170 + Math.floor(i / 2) * 50;

        let buttonText = new PIXI.Text({ text: actions[i], style: actionText });
        buttonText.x = actionButton.x + (180 - buttonText.width) / 2;
        buttonText.y = actionButton.y + 10;

        let descriptionText = new PIXI.Text({text:actionDescription[i], style:descriptionStyle});
        descriptionText.x = 80;
        descriptionText.y = 200;
        descriptionText.visible=false;
        descriptionText.zIndex=101;

        let descBack = new PIXI.Graphics();
        descBack.roundRect(70,190,descriptionText.width + 20, descriptionText.height+20,10);
        descBack.fill('0xE4EFE7');
        descBack.stroke(2,0x000000);
        descBack.visible=false;
        descBack.zIndex = 100;

        actionButton.eventMode = 'static';
        actionButton.on("pointerdown", () => {
            actionFunctions[i]();
            closeCPQuery();
        });
        actionButton.on("mouseover",()=>{
            descriptionText.visible=true;
            descBack.visible=true;
        });
        actionButton.on("mouseout",()=>{
            descriptionText.visible=false;
            descBack.visible=false;
        });

        buttons.push(actionButton);
        cpMenu.addChild(actionButton);
        cpMenu.addChild(buttonText,descBack,descriptionText);
    }

    const endTurnButton = makeRoundRect(510,320,180,40,10,0x823939,0,0x000000,1001,true);
    endTurnButton.eventMode='static';
    endTurnButton.on('pointerdown', () => {
        closeCPQuery();
        console.log("end turn");
        socket.emit('game', 'end-turn'); // End current turn
        cpButton.visible=false;
        cpButtonText.visible=false;
    });
    cpMenu.addChild(endTurnButton);
    
    const endTurnText = new makeBoardText(550,330,"End Turn [0]", new PIXI.TextStyle({ fontSize: 18, fill: '#000000', align: 'center' }));
    endTurnText.zIndex = 1002;
    // endTurnText.visible = false;
    cpMenu.addChild(endTurnText);
    app.stage.addChild(cpMenu);
}

// Function to close the CP menu
export function closeCPQuery() {
    cpMenu.visible = false;
    cpButtonText.text = "Open CP Menu";
}

// Function to open the CP menu
export function openCPQuery() {
    cpMenu.visible = cpButton.visible = cpButtonText.visible = true;
    cpButtonText.text = "Close CP Menu";
}

// List of all node names
const nodeNames = ['blueBase', 'redBase', 'city4', 'city6', 'city9', 'city10', 'village2', 'village3', 'village7', 'village8'];

// Function to return all nodes that have armies of a given color
export function findNodes(color, num, move = true) {

    let result;

    if (color === 'blue') {
        result =  Object.keys(num).filter(key => (move ? (num[key].blueArmies > num[key].blueMoved): num[key].blueArmies > 0));
    }
    else {
        result =  Object.keys(num).filter(key => (move ? (num[key].redArmies > num[key].redMoved): num[key].redArmies > 0));

    }



    return result.filter(x => nodeNames.includes(x));
}