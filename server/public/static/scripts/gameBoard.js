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

// Function to create and return a given piece
export function makeBoardPiece (x, y, width, height, zIndex, name) {
    return Object.assign(PIXI.Sprite.from(name), { x, y, width, height, zIndex });
}

// Function to create and return a circle
export function makeCircle (x, y, r, zIndex, fill, width, color, alpha) {
    return Object.assign(new PIXI.Graphics(), { x, y, zIndex, alpha, visible: true })
        .circle(0, 0, r)
        .fill(fill)
        .stroke({ width, color });
}

// Function to create and return a given text sprite
export function makeBoardText (x, y, zIndex, text, style, visible = true) {
    return Object.assign(new PIXI.Text({ text, style }), { x, y, zIndex, visible });
}

// Function to create and return a round rectangle
export function makeRoundRect (x, y, w, h, zIndex, corner, fill, width, color, visible = true) { //swapped zIndex from kinda last to 5th
    return Object.assign(new PIXI.Graphics(), { x, y, zIndex, visible })
        .roundRect(0, 0, w, h, corner)
        .fill(fill)
        .stroke({ width, color });
}

// Function to create and return a dice 
export function drawDice (x, y, n, color) {
    return Object.assign(PIXI.Sprite.from(colors[color][n-1]), { x, y, zIndex: 80, width: 75, height: 75 });
}

// Function to create specific white, centered text of a given font size
function gameTextStyle (fontSize) {
    return new PIXI.TextStyle({ fontFamily: 'normal', fontSize, fill: '#ffffff', align: 'center' });
}

// Create CP menu
const cpMenu = Object.assign(new PIXI.Container(), { zIndex: 200, visible: false });

// Add background to CP menu
cpMenu.addChild(
    new PIXI.Graphics()
        .roundRect(300, 100, 400, 275, 10)
        .fill({ color: 0, alpha: 0.85 })
        .stroke(2, 0xffffff)
);

// Create CP button
const cpButton = Object.assign(new PIXI.Graphics(), { zIndex: 10, alpha: 0.7, eventMode: 'static', visible: false })
    .roundRect(780, 40, 145, 33, 10)
    .fill(0x000000)
    .stroke(10, 0x101010)
    .on('pointerdown', () => {
        if (cpMenu.visible) closeCPQuery();
        else openCPQuery();            
    });

// Create CP button text
const cpButtonText = Object.assign(new PIXI.Text({ text: "Open CP Menu", style: gameTextStyle(20) }), { x: 790, y: 45, zIndex: 100, visible: false });

// Function to populate and return CP menu
export function makeCPMenu (actionFunctions, app) {

    // Create text styles for the action buttons and descriptions
    const actionTextStyle = gameTextStyle(18);
    const descriptionTextStyle = new PIXI.TextStyle({ fontFamily: 'normal', fontSize: 15, fill: '#000000', wordWrap: true, wordWrapWidth: 410 });

    // Create main text for CP menu
    cpMenu.addChild(Object.assign(new PIXI.Text({ text: "Spend CP?", style: gameTextStyle(30) }), { x: 434, y: 115 }));

    // Action labels and texts
    const actions = [
        { offset: 56, label: "Move [1]", text: "Move up to two Armies to an adjacent node. The two Armies do not have to travel to the same node. Armies cannot move through a space occupied by the opponent. Each Army can only move once per turn." },
        { offset: 51, label: "CHMR [2]", text: "Target one node for CHMR. Select the number of armies to move to the selected temporary haven adjacent to the targeted node. Roll 1 d6 for each Army. Based on the roll, the CIV value will be decreased from the node and added to the temporary haven.\nRoll 6: 2 CIV\nRoll 5-3: 1 CIV\nRoll 1-2: 0 CIV" },
        { offset: 12, label: "Humanitarian Aid [2]", text: "Roll 1 d6. On a roll of 6, increase your Influence by 1 on the Support Tracker. If your Support Marker is already on 6, nothing happens." },
        { offset: 57, label: "Surge [3]", text: "Add an additional 4 Armies to your team's base from your \"Available to Surge\" box. This action can only be used twice during the game." },
        { offset: 5, label: "Influence Operation [3]", text: "Roll 1 d6 for every 2 Civilians your opponent has in their Civilian casualties space. For each 6 rolled, reduce the opponent's influence on Support Tracker by one." },
        { offset: 26, label: "Artillery Fires [1]", text: "Select an Army to fire, roll 3 d6 to target an adjacent node.\nFirst Roll - Casualties occur on rolls of 4-6.\nSecond Roll - Civ Casualties occur on rolls of 3-6." },
        { offset: 42, label: "Air Strike [2]", text: "Roll 2 d6 to air strike any node on the map.\nFirst Roll - Combat casualties occur on rolls of 4-6.\nSecond Roll - Civ Casualties occur on rolls of 5-6." }
    ];

    // Loop through all actions
    for (let i = 0; i < actions.length; i++) {

        // Define x and y position based on loop
        const x = (i % 2) ? 510 : 310;
        const y = 170 + Math.floor(i / 2) * 50;

        // Create button
        const button = Object.assign(new PIXI.Graphics(), { x, y, eventMode: 'static' })
            .roundRect(0, 0, 180, 40, 5)
            .fill(0x333333);

        // Create button text
        const buttonText = Object.assign(
            new PIXI.Text({ text: actions[i].label, style: actionTextStyle }),
            {x: x + actions[i].offset, y: y + 10 }
        );
        
        // Create description box
        const descriptionBox = Object.assign(new PIXI.Graphics(), { zIndex: 100, visible: false })
            .roundRect(300, 390, 400, 150, 10)
            .fill('0xE4EFE7')
            .stroke(2,0x000000);

        // Create description text
        const descriptionText = Object.assign(
            new PIXI.Text({ text: actions[i].text, style: descriptionTextStyle }),
            { x: 310, y: 400, zIndex: 101, visible: false }
        );

        // Define event listeners for the button
        button.on('mouseover', () => { descriptionBox.visible = descriptionText.visible = true });
        button.on('mouseout', () => { descriptionBox.visible = descriptionText.visible = false });
        button.on('pointerdown', () => {
            actionFunctions[i]();
            closeCPQuery();
        });

        // Add all necessary elements to canvas
        cpMenu.addChild(button, buttonText, descriptionBox, descriptionText);
    }

    // Add end turn button to CP menu
    cpMenu.addChild(
        Object.assign(makeRoundRect(510, 320, 180, 40, 10, 0x823939, 0, 0x000000, 1001, true), { eventMode: 'static' })
            .on('pointerdown', () => {
                closeCPQuery();
                socket.emit('game', 'end-turn');
                cpButton.visible = cpButtonText.visible = false;
            })
    );
    
    // Add end turn text to CP menu
    cpMenu.addChild(
        Object.assign(new makeBoardText(556, 330, "[ End Turn ]", gameTextStyle(18)), { zIndex: 1002 })
    );
    
    // Add necessary items to canvas
    app.stage.addChild(cpMenu, cpButton, cpButtonText);
}

// Function to close the CP menu
export function closeCPQuery () {
    cpMenu.visible = false;
    cpButtonText.text = "Open CP Menu";
}

// Function to open the CP menu
export function openCPQuery () {
    cpMenu.visible = cpButton.visible = cpButtonText.visible = true;
    cpButtonText.text = "Close CP Menu";
}

// List of all node names
const nodeNames = ['blueBase', 'redBase', 'city4', 'city6', 'city9', 'city10', 'village2', 'village3', 'village7', 'village8'];

// Function to return all nodes that have armies of a given color
export function findNodes (color, num, move = true) {
    let result;
    if (color === 'blue')
        result =  Object.keys(num).filter(key => (move ? (num[key].blueArmies > num[key].blueMoved): num[key].blueArmies > 0));
    else
        result =  Object.keys(num).filter(key => (move ? (num[key].redArmies > num[key].redMoved): num[key].redArmies > 0));
    return result.filter(x => nodeNames.includes(x));
}