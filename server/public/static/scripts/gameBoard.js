// List of number names used for retrieving all die asset names
const numbers = ['one', 'two', 'three', 'four', 'five', 'six'];

// Function to retrieve all six die asset names of a given color
function getDies (color) {
    return Array.from(numbers, (x) => '/content/' + color + '_' + x + '_die.png');
}

// Variable to hold all die asset names
const colors = {
    'blue': getDies('blue'),
    'red': getDies('red'),
    'black': getDies('black')
}

// Function to load all necessary assets
export async function loadAssets(){

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

export function makeBoardPiece(x, y, width, height, name, zIndex = 0) {
    return Object.assign(PIXI.Sprite.from(name), { x, y, width, height, zIndex });
}

export function makeCircle(x, y, r, fill, stroke, strokefill, alpha) {
    return Object.assign(new PIXI.Graphics(), { alpha })
        .circle(x, y, r)
        .fill(fill)
        .stroke(stroke, strokefill);
}

export function makeBoardText(x, y, text, style) {
    return Object.assign(new PIXI.Text({ text, style }), { x, y });
}

export function makeRoundRect(x, y, w, h, corner, fill, width, color, zIndex, visible = true) {
    return Object.assign(new PIXI.Graphics(), { zIndex, visible })
        .roundRect(x, y, w, h, corner)
        .fill(fill)
        .stroke({ width, color });
}

export function drawDice(x, y, zIndex, n, color) {
    return Object.assign(PIXI.Sprite.from(colors[color][n-1]), { x, y, zIndex, width: 75, height: 75 });
}

const cpButtonText = new PIXI.Text({ text: "Open CP Menu", style: new PIXI.TextStyle({ fontSize: 20, fill: '#ffffff', align: 'center' }) });
cpButtonText.x = 790;
cpButtonText.y = 45;
cpButtonText.visible = false;
cpButtonText.zIndex = 100;

//buttons that are used to test functions
const cpButton = new PIXI.Graphics();
cpButton.roundRect(780,40, cpButtonText.width+20,cpButtonText.height+10,10);
cpButton.alpha = 0.7
cpButton.zIndex = 90;
cpButton.fill(0x000000);
cpButton.stroke(10,0x101010)
cpButton.eventMode = 'static';
cpButton.visible=false;
cpButton.on('pointerdown', () => {
    if (cpQuery.visible == false) 
        openCPQuery();
    else
        closeCPQuery();
});

const cpQuery = new PIXI.Container();

export function makeCPContainer(actionFunctions,app) {

    // QUERY user for using combat points (combat and non combat both use.)
    // chat 4o used for this 
    cpQuery.visible = false;
    cpQuery.zIndex = 200;

    const cpQueryBackground = new PIXI.Graphics();
    cpQueryBackground.roundRect(300, 100, 400, 300, 10);
    cpQueryBackground.fill({ color: 0, alpha: 0.85 });
    cpQueryBackground.stroke(2, 0xffffff);
    cpQuery.addChild(cpQueryBackground);

    const actionText = new PIXI.TextStyle({
        // fontFamily: 'Arial',
        fontSize: 18,
        fill: '#FFFFFF', // white text color
        align: 'center'
    });

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
    cpQuery.addChild(cpQueryText);

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
        cpQuery.addChild(actionButton);
        cpQuery.addChild(buttonText,descBack,descriptionText);
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
    cpQuery.addChild(endTurnButton);
    
    const endTurnText = new makeBoardText(550,330,"End Turn [0]", new PIXI.TextStyle({ fontSize: 18, fill: '#000000', align: 'center' }));
    endTurnText.zIndex = 1002;
    // endTurnText.visible = false;
    cpQuery.addChild(endTurnText);
    return cpQuery;
}

export function closeCPQuery() {
    cpQuery.visible = false;
    cpButtonText.text = "Open CP Menu";
}

export function openCPQuery() {
    console.log("open cp query");
    cpQuery.visible = cpButton.visible = cpButtonText.visible = true;
    cpButtonText.text = "Close CP Menu";
}

 //finds all of the nodes that have armies of the given color
export function findNodes(color,num, move = true) {
    if (color == 'blue') {
        return Object.keys(num).filter(key => (move ? (num[key].blueArmies > num[key].blueMoved): num[key].blueArmies > 0));
    }
    else {
        return Object.keys(num).filter(key => (move ? (num[key].redArmies > num[key].redMoved): num[key].redArmies > 0));

    }
}