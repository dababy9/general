import * as Board from './gameBoard.js';
import {makeTextStyle} from './moreFunctions.js';

//Client responses

//message from the server
socket.on('message-log', (messages) => {
    console.log(messages);
});

//displays when the client has connected to the server 
socket.on('connect', async () => {
    console.log('Connected to server');
});

//requests the color of the player from the server
socket.emit('game', 'fetch-color');

//saves the color of the player to playerColor so it can be used throughout the game
var playerColor;
socket.on('color', (color) => {
    playerColor = color;
});


// Catch connection errors
socket.on('connect_error', (error) => {
    console.log('Connection error:', error);
});

//where the gamestate is saved that allows for the board to be updated
//updates the board upon first gamestate receipt
var gameState;
socket.on('game-state', (game) => {
    gameState = JSON.parse(game);
    updateBoard(gameState);
});

//logs where there is a message and what the data is. 
socket.on('new-message', (message) => {
    console.log(message.from);
    console.log(message.data);
});

socket.on('new-turn', (player) => {
   gameState.turnPlayer = player; 
   if (playerColor == player){
    Board.openCPQuery();
   }
   playerTurnBanner();
});


socket.on('initiative-ready', () => {
    startInitiative();
});

//start the pixi application to make canvas to add sprites to
const app = new PIXI.Application();
await app.init({ height:650, width:1000, background: '#DFE8F3' });
// Append PixiJS canvas to the correct container
document.getElementById("pixi-container").appendChild(app.canvas);
// document.body.appendChild(app.canvas);
// load the PNG asynchronously

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

await Board.loadAssets();

const background = await Board.makeBackground();
const board = await Board.makeGameBoard();

//add the left gravestone
const blueGrave = await Board.makeBoardPiece(128,450,100,175,'/content/blueGrave.png');
const redGrave = await Board.makeBoardPiece(848,450,100,175,'/content/redGrave.png');

app.stage.addChild(background, board,blueGrave,redGrave);

//add the command point trackers (value updated from gamestate in update board)
const redCPTracker = Board.makeBoardPiece(750,350,193,89,'/content/redCPTracker.png');
const blueCPTracker = Board.makeBoardPiece(30,350,193,89,'/content/blueCPTracker.png');

const blueSur = Board.makeBoardPiece(20,450,95,170,'/content/blueSurgeNew.png');
const redSur = Board.makeBoardPiece(740,450,95,170,'/content/redSurgeNew.png');
app.stage.addChild(redCPTracker,blueCPTracker,redSur,blueSur);

//add the support tracker
const suppTrack = Board.makeBoardPiece(230,490,500,115,'/content/support_tracker.png');
app.stage.addChild(suppTrack);

//adds the menu button to the top left
const menubutton = Board.makeBoardPiece(15,15,25,25,'/content/button.png');
menubutton.eventMode = 'static';
menubutton.cursor = 'pointer';

//creates a sprite that shows the Instructions. this pops up and disappears 
//by clicking the menu button
const menuInstructions = Board.makeBoardPiece(40,40,950,534,'/content/VVInstructions.jpg',1200)
menuInstructions.visible = false;
app.stage.addChild(menuInstructions);

menubutton.on('pointerdown', () => {
    menuInstructions.visible = !menuInstructions.visible;
});
app.stage.addChild(menubutton);

//support tracker locations
const supportLoc = {
    '0': { x: 330, y: 538 },
    '1': { x: 390, y: 538 },
    '2': { x: 440, y: 538 },
    '3': { x: 490, y: 538 },
    '4': { x: 540, y: 538 },
    '5': { x: 590, y: 538 },
    '6': { x: 640, y: 538 },
    '7': { x: 695, y: 538 },
}

// Create a text style 
const redArmy = makeTextStyle('Arial',30,'#520e05','center');

const blueArmy = makeTextStyle('Arial',30,'#000080','center');

const civStyle = makeTextStyle('Arial',35,'#FFFFFF','center');
civStyle.fontWeight= 'bold';

////circles for representing the amount of support points 
const redSupport = Board.makeCircle(0,0,20,0xFF0000,2,0xFF0FF0,0.75);
const blueSupport = Board.makeCircle(0,0,20,0x0000FF,2,0x0F0FFF,0.75);
app.stage.addChild(redSupport,blueSupport);

//Civilian Casualties trackers
const redCas = Board.makeBoardText(888,492,'0',redArmy);
const blueCas = Board.makeBoardText(168,492,'0',blueArmy);
//surge trackers
const redSurge = Board.makeBoardText(778,492,'0',redArmy);
const blueSurge = Board.makeBoardText(58,492,'0',blueArmy);
//command point trackers
const redCP = Board.makeBoardText(833,385,'0',redArmy);
const blueCP = Board.makeBoardText(113,385,'0',blueArmy);

app.stage.addChild(redCas, blueCas, redSurge, blueSurge, redCP, blueCP);

// function that takes the gamestate and updates the board given it. 
async function updateBoard(gameState) {

    // console.log(gameState);
    //update the player turn banner
    playerTurnBanner();
    roundNum.text = gameState.turnCounter;

    //starts initiative if no turn player is assigned
    if (!gameState.turnPlayer) {
        startInitiative();
    }

    //update the surge and casualites
    redSurge.text=gameState['redPlayer'].surgeArmies;
    blueSurge.text=gameState['bluePlayer'].surgeArmies;
    redCas.text=gameState['redPlayer'].casualties;
    blueCas.text=gameState['bluePlayer'].casualties;

    //update support tracker locations
    let redS = gameState.redPlayer.support;
    let blueS = gameState.bluePlayer.support;
    redSupport.x = supportLoc[redS].x;
    redSupport.y = supportLoc[redS].y + 40;
    blueSupport.x = supportLoc[blueS].x;
    blueSupport.y = supportLoc[blueS].y;

    //update the CP values (these change when spending command points)
    redCP.text = gameState.redPlayer.cp;
    blueCP.text = gameState.bluePlayer.cp;

    //game state with numbers instead of object values
    let numberedGameState = getCount(gameState.nodes);
    //updating all of the node player values
    for (const data in numberedGameState) {

        nodeSprites[data].redSprite.text = numberedGameState[data].redArmies;
        nodeSprites[data].blueSprite.text = numberedGameState[data].blueArmies;
        nodeSprites[data].civSprite.text = numberedGameState[data].civilians;
        

        if (nodeSprites[data].civSprite.text == '0') {
            nodeSprites[data].civSprite.visible = false; 
            nodeSprites[data].civImg.visible = false;
        }
        else {
            nodeSprites[data].civSprite.visible = true;
            nodeSprites[data].civImg.visible = true;
        }

        if (nodeSprites[data].redSprite.text == '0') {
            nodeSprites[data].redSprite.visible = false;
            nodeSprites[data].redIFV.visible = false;
        }
        else {
            nodeSprites[data].redSprite.visible = true;
            nodeSprites[data].redIFV.visible = true;

        }

        if (nodeSprites[data].blueSprite.text == '0') {
            nodeSprites[data].blueSprite.visible = false;
            nodeSprites[data].blueIFV.visible = false;
        }
        else {
            nodeSprites[data].blueSprite.visible = true;
            nodeSprites[data].blueIFV.visible = true;
        }
        if (nodeSprites[data].civSprite.text == '0') {
            nodeSprites[data].civSprite.visible = false;
        }
        else {
            nodeSprites[data].civSprite.visible = true;
        }

    }
}


//locations where the red troops will appear on node.
// blue and civilian are in reference to this. 
const locations = {
    'blueBase': { x: 120, y: 220 },
    'redBase': { x: 756, y: 140 },
    'city4': { x: 546, y: 65 },
    'city6': { x: 257, y: 299 },
    'city9': { x: 377, y: 407 },
    'city10': { x: 590, y: 376 },
    'village2': { x: 263, y: 114 },
    'village3': { x: 408, y: 125 },
    'village7': { x: 436, y: 270 },
    'village8': { x: 602, y: 212 },
    'haven1': { x: 220, y: 200 },
    'haven2': { x: 325, y: 186 },
    'haven3': { x: 369, y: 232 },
    'haven4': { x: 366, y: 317 },
    'haven5': { x: 518, y: 138 },
    'haven6': { x: 492, y: 205 },
    'haven7': { x: 554, y: 287 },
    'haven8': { x: 474, y: 358 },
    'haven9': { x: 648, y: 131 },
    'haven10': { x: 707, y: 237 }
}

//add all of the sprites that have armies and civilians so they can be updated. 
const nodeSprites = {}

let i = 0
for (const data in locations) {

    //text for the pieces
    const redText = Board.makeBoardText(locations[data].x,locations[data].y,'0',redArmy);
    redText.visible = false;
    redText.zIndex = 80;
    const blueText = Board.makeBoardText(locations[data].x + 52, locations[data].y,'0',blueArmy);
    blueText.visible=false;
    blueText.zIndex = 80;
    const civText = Board.makeBoardText(locations[data].x + 35, locations[data].y + 40, '0',civStyle);
    civText.zIndex = 80;
    civText.visible=false;

    app.stage.addChild(redText, blueText,civText);

    //icons for the pieces
    const redIFV = Board.makeBoardPiece(locations[data].x - 5,locations[data].y - 10,50,50,'/content/IFV_Red.png',79);
    const blueIFV = Board.makeBoardPiece(locations[data].x + 47,locations[data].y - 10,50,50,'/content/IFV_Blue.png',79);
    const civImg = Board.makeBoardPiece(locations[data].x + 20,locations[data].y + 35,50,50,'/content/civilians2.png',79);
    redIFV.visible = false;
    blueIFV.visible=false;
    civImg.visible=false;

    app.stage.addChild(redIFV, blueIFV,civImg);

    //add the node to the map
    nodeSprites[data] = {
        redIFV: redIFV,
        blueIFV: blueIFV,
        redSprite: redText,
        blueSprite: blueText,
        civSprite: civText,
        civImg: civImg
    };
}

//making the initiative button
const initiative = Board.makeRoundRect(0,0,350,75,30,0x77a1b5,3,0x426779,100,false);
initiative.x=280;
initiative.y=280;
initiative.eventMode = 'static';
initiative.zIndex = 100;
app.stage.addChild(initiative);

const initiativeText = Board.makeBoardText(300,300,"",redArmy);
initiativeText.zIndex = 101;
app.stage.addChild(initiativeText);

function startInitiative() {
    initiative.width = 350;
    initiative.removeAllListeners();
    initiative.on('pointerdown', ()=>{
        initiativeText.text = 'Waiting on Opponent';
        socket.emit('game', 'initiative');
    });
    initiativeText.text = 'Click to roll for Initiative';
    initiative.visible = true;
}

//sets the player based on who won the initiative. Additionally creates the dice and displays them.
var initiativeWinner;
socket.on('initiative-result', (result) => {
    result = JSON.parse(result);
    // console.log(result);
    initiativeWinner = result.winner;
    gameState.turnPlayer = result.winner;
    initiativeText.text = result.winner + " wins! Click again to continue";
    initiative.width = 480
    initiative.removeAllListeners();
    let blueDice = Board.drawDice(280,370,950,result.blueRoll,'blue');
    let redDice = Board.drawDice(375,370,950,result.redRoll,'red');
    app.stage.addChild(blueDice,redDice);

    initiative.on('pointerdown', () => {
        endInit()
        app.stage.removeChild(blueDice);
        app.stage.removeChild(redDice);
    });
    playerTurnBanner();
});

//removes the init function and opens CP spender
function endInit() {
    playerTurnBanner()
    initiativeText.text = ''
    initiative.visible = false;
    if (gameState.turnPlayer == playerColor) {
        Board.openCPQuery();
    }
    else {
        changeMessage("Waiting on " + gameState.turnPlayer + " to spend Command points");
    }

}

//asks for the game state after the values in update board have been created
socket.emit('game', 'fetch-game-state');

//array of the functions that the buttons would call
const actionFunctions = [moveClicked, chmrClicked, humanAid, surge, influenceOp, firesClicked, strikeClicked]
// // QUERY user for using combat points (combat and non combat both use.)
// // chat 4o used for this 
// const cpQuery = new PIXI.Container();
const cpQuery = Board.makeCPContainer(actionFunctions,app);
// cpQuery.visible = false;
// cpQuery.zIndex = 200;
app.stage.addChild(cpQuery);


function clicked() {
    console.log("You have clicked!");
}

//here is where the messages will be put for all of the moves
const cpMessage = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 20, fill: '#000000', align: 'center' }) });
cpMessage.x = 120;
cpMessage.y = 30;
cpMessage.zIndex = 1000;;
app.stage.addChild(cpMessage);

const messageBackground = new PIXI.Graphics();
messageBackground.zIndex = 900;
app.stage.addChild(messageBackground);

function changeMessage(message) {
    cpMessage.text = message;
    messageBackground.clear();
    messageBackground.roundRect(115, 25, cpMessage.width + 10, cpMessage.height + 10, 10);
    messageBackground.alpha = 0.75;
    messageBackground.fill(0xC06C84);
}

function clearMessage() {
    messageBackground.clear();
    cpMessage.text = "";
}


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

const highlightedNodes = {};

function highlight(names, callback, message) {
    removeHighlight();
    for (const name of names) {
        const select = new PIXI.Graphics();
        select.circle(highLoc[name].x, highLoc[name].y, 57);
        select.fill(0xFFDE21)
        select.alpha = 0.5;
        select.stroke({ width: 3, color: 0xF4BC1C })
        select.eventMode = 'static';
        select.zIndex = 75;
        select.on('pointerdown', () => callback(name))
        app.stage.addChild(select)
        highlightedNodes[name] = select;
    }
    changeMessage(message);
}

/////////////START MOVE
/////////////START MOVE
/////////////START MOVE

//selected nodes for the move function
let selections = [];

function moveClicked() {
    //find the clickable nodes based on player
    //call the highlight function with the nodes
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 1) {
        Board.openCPQuery();
        return;
    }
    var find = Board.findNodes(playerColor,getCount(gameState.nodes));
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
        clearMessage();
        hideDone();
    }

    if (selections.length === 1) {  
        //give the user the option to pick a second node or just send one. 
        showDone();
        highlight(Board.findNodes(playerColor,getCount(gameState.nodes)), move1, "You picked " + name + ". Click done to continue or\nselect a second node to move from.");
    }
}

//receives the moves that the armies can move to. 
socket.on('move-lists', (lists) => {
    let parsedLists = JSON.parse(lists); // Parse the JSON string
    let list1 = parsedLists[0]; // Grab the first list
    let list2 = parsedLists[1]; // Grab the second list
    if (playerColor == gameState.turnPlayer) {
        moveReceived(list1, list2);
    }
});

let nodeList2 = [];

let selections2 = [];
function moveReceived(nodes1, nodes2) {
    
    highlight(nodes1, move2, "Pick the node where army from " + selections[0] + " should go");
    nodeList2 = nodes2;

}

//lets the user select the nodes that the armies are moving to. 
function move2(name) {
    selections2[selections2.length] = name;
    if (selections2.length == selections.length) {
        socket.emit('game', 'action', { type: 'move-confirm', data: selections2 });
        removeHighlight();
        clearMessage();
        //clears the selections 
        selections = [];
        selections2 = [];
    }

    //allows the user to pick the second node to move from if they selected two nodes originally. 
    else {
        highlight(nodeList2, move2, "You picked " + name + ". Choose where the army from " + selections[1] + " should go.");
    }
}

//this is the end of the move. Updates the board and sends the user back to CP query.
socket.on('move', (game) => {
    gameState = JSON.parse(game); // Parse the JSON string
    updateBoard(gameState);
    
    if (playerColor == gameState.turnPlayer) {
        Board.openCPQuery();
    }
    else {
        changeMessage("Opponent finished a move!\nWaiting on them to spend command points");
    }
});


//done buttons that end the move after one node is selected if clicked
const doneText = new PIXI.Text({ text: "Done", style: new PIXI.TextStyle({ fontSize: 20, fill: '#000000', align: 'center' }) });
const doneButton = Board.makeRoundRect(90,110,doneText.width + 20, doneText.height+20,10,'0x00FF00',1,0x000000,50,false)
doneButton.alpha = 0.75;
doneButton.eventMode = 'static';
//sends the one node to the server if clicked
doneButton.on('pointerdown', () => {
    socket.emit('game', 'action', { type: 'move-select', data: selections });
    removeHighlight();
    clearMessage();
    hideDone();
    hideCancelMove();

});

doneText.x = 100;
doneText.y = 120;
doneText.zIndex = 1000;
doneText.visible = false;
app.stage.addChild(doneText,doneButton);

function showDone(){
    doneButton.visible=true;
    doneText.visible=true;
}

function hideDone(){
    doneButton.visible=false;
    doneText.visible=false;
}

function removeHighlight() {
    for (var member in highlightedNodes) {
        app.stage.removeChild(highlightedNodes[member]);
        delete highlightedNodes[member];
    }
}



//button that cancels move if clicked. opens cp tracker
const cancelMoveText = new PIXI.Text({ text: "Cancel Move", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
const cancelMoveButton = Board.makeRoundRect(90,100,cancelMoveText.width + 20,cancelMoveText.height + 20,10,'0xFF0000',1,0x000000,100,false);
cancelMoveButton.alpha = 0.75;
cancelMoveButton.eventMode = 'static';
cancelMoveButton.on('pointerdown', () => {
    removeHighlight();
    clearMessage();
    Board.openCPQuery();
    hideCancelMove();
});

cancelMoveText.x = 100;
cancelMoveText.y = 110;
cancelMoveText.zIndex = 1000;
cancelMoveText.visible = false;
app.stage.addChild(cancelMoveText, cancelMoveButton);

function cancelMove() {
    cancelMoveButton.visible = true;
    cancelMoveText.visible = true;
}

function hideCancelMove(){
    cancelMoveButton.visible=false;
    cancelMoveText.visible=false;
}

////End Move
////End Move
////End Move


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

//this will display to the player whos turn it is. 
const playerText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
playerText.x = 780;
playerText.y = 290;
playerText.zIndex = 901;
app.stage.addChild(playerText);

const playerBanner = new PIXI.Graphics();
playerBanner.zIndex = 0;
app.stage.addChild(playerBanner);

//updates the player turn banner
function playerTurnBanner() {

    const colors = {
        'blue': '0x0000FF',
        'red': '0xFF0000'
    }
    // console.log(playerColor);


    if ((gameState.turnPlayer == "" && playerColor == initiativeWinner) || gameState.turnPlayer == playerColor) {
        playerBanner.zIndex = 900;
        playerText.text = "You are " + playerColor + " player,\nit is your turn!";
        playerBanner.clear();
        playerBanner.roundRect(playerText.x - 5, playerText.y - 5, playerText.width + 10, playerText.height + 10, 10);
        playerBanner.fill(colors[playerColor]);
    }
    else if (gameState.turnPlayer != playerColor) {
        playerBanner.zIndex = 900;
        playerText.text = "You are " + playerColor + " player,\nIt is not your turn.";
        playerBanner.clear();
        playerBanner.roundRect(playerText.x - 5, playerText.y - 5, playerText.width + 10, playerText.height + 10, 10);
        playerBanner.fill(colors[playerColor]);
    }
}

/////Display round start
/////Display round start
const roundBack = new PIXI.Graphics();
roundBack.roundRect(850,200,100,80,10);
roundBack.fill({color:0x000000,alpha:0});
roundBack.stroke(10,0x000000);
app.stage.addChild(roundBack);

const roundText = new PIXI.Text({ text: "Round", style: new PIXI.TextStyle({ fontSize: 17, fill: '#000000', align: 'center' }) });
roundText.x = 871;
roundText.y = 210;
app.stage.addChild(roundText);


const roundNum = new PIXI.Text({ text: "1", style: new PIXI.TextStyle({ fontSize: 30, fill: '#000000', align: 'center' }) });
roundNum.x = 889;
roundNum.y = 235;
app.stage.addChild(roundNum);



////HUMANITARIAN AID START
//button that confirms humanitarian aid
const humanAidButton = new PIXI.Graphics();
const humanAidText = new PIXI.Text({ text: "Roll for Humanitarian Aid", style: new PIXI.TextStyle({ fontSize: 35, fill: '#FFFFFF', align: 'center' }) });
const humanAidBackground = new PIXI.Graphics();

humanAidButton.roundRect(250, 250, humanAidText.width + 20, humanAidText.height + 20, 10);
humanAidButton.fill('0x5b3775');
humanAidButton.stroke(6, 0xffffff);
humanAidButton.visible = false;
humanAidButton.zIndex = 900;
humanAidButton.eventMode = 'static';

humanAidBackground.roundRect(230, 230, humanAidText.width + 60, 160, 10);
humanAidBackground.fill(0xd0d0d0);
humanAidBackground.stroke(4, 0x000000);
humanAidBackground.zIndex = 800;
humanAidBackground.visible = false;
app.stage.addChild(humanAidBackground);

app.stage.addChild(humanAidButton);
humanAidText.x = 260;
humanAidText.y = 260;
humanAidText.zIndex = 1000;
humanAidText.visible = false;
app.stage.addChild(humanAidText);

humanAidButton.on('pointerdown', () => {
    // console.log("Aid!");
    humanAidButton.visible = false;
    humanAidText.visible = false;
    cancelAidButton.visible = false;
    cancelAidText.visible = false;
    humanAidBackground.visible = false;
    clearMessage();
    socket.emit('game', 'action', { type: 'humanitarian-aid' }); // Request to conduct humanitarian aid action
});

function humanAid() {
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 2) {
        Board.openCPQuery();
        return;
    }
    humanAidButton.visible = true;
    humanAidText.visible = true;
    humanAidBackground.visible = true;
    cancelAid();
    changeMessage("Confirm Humanitarian Aid Selection")
}

const cancelAidButton = new PIXI.Graphics();
const cancelAidText = new PIXI.Text({ text: "Cancel Aid", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
cancelAidButton.roundRect(390, 330, cancelAidText.width + 20, cancelAidText.height + 20, 10);
cancelAidButton.fill('0xFF0000');
cancelAidButton.stroke(6, 0xffffff);
// cancelAidButton.alpha = 0.75;
cancelAidButton.zIndex = 900;
cancelAidButton.visible = false;
cancelAidButton.eventMode = 'static';
cancelAidButton.on('pointerdown', () => {
    // console.log("cancel aid!");
    humanAidBackground.visible = false;
    cancelAidButton.visible = false;
    clearMessage();
    Board.openCPQuery();
    cancelAidText.visible = false;
    humanAidButton.visible = false;
    humanAidText.visible = false;
});

app.stage.addChild(cancelAidButton);
cancelAidText.x = 400;
cancelAidText.y = 340;
cancelAidText.zIndex = 900;
cancelAidText.visible = false;
app.stage.addChild(cancelAidText);

function cancelAid() {
    cancelAidButton.visible = true;
    cancelAidText.visible = true;
}

socket.on('humanitarianAid', (response) => {
    let parsedResponse = JSON.parse(response); // Parse the JSON string
    // console.log(parsedResponse.result); // Should print a number 1-6
    // console.log(parsedResponse.gameState); // Should print the entire updated gameState
    gameState = parsedResponse.gameState;
    updateBoard(gameState);

    if (playerColor != gameState.turnPlayer) {
        changeMessage("Opponent is rolling for Humanitarian Aid");
    }


    const aidRoll = new PIXI.Graphics();
    aidRoll.roundRect(300, 150, 350, 200, 10);
    aidRoll.fill('0x808080');
    aidRoll.stroke(4, '0xffffff');
    aidRoll.zIndex = 900;
    aidRoll.eventMode = 'static';
    app.stage.addChild(aidRoll);

    let roll = parsedResponse.result;

    const aidText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }) });
    aidText.x = 350;
    aidText.y = 160;
    aidText.zIndex = 1000
    aidText.text = ((playerColor == gameState.turnPlayer) ? 'You' : "Opponent") + " rolled a " + roll + ".\n+" + ((roll > 4 ? '1' : '0')) + " Support Tracker Points\n\n\n\n\nClick to Continue"
    app.stage.addChild(aidText);


    let aidDice = Board.drawDice(440,220,950,roll,'black');
    app.stage.addChild(aidDice);

    aidRoll.on('pointerdown', () => {
        app.stage.removeChild(aidRoll);
        app.stage.removeChild(aidText);
        app.stage.removeChild(aidDice);
        if (playerColor == gameState.turnPlayer) {
            Board.openCPQuery();
        }
        else {
            changeMessage("Opponent completed Humanitarian Aid\nWaiting on them to spend command points");
        }
    })

});

//HUMANITARIAN AID END

/////SURGE START 
/////SURGE START 
/////SURGE START 

const surgeButton = new PIXI.Graphics();
const surgeText = new PIXI.Text({ text: "Confirm Surge", style: new PIXI.TextStyle({ fontSize: 35, fill: '#FFFFFF', align: 'center' }) });
const surgeBackground = new PIXI.Graphics();


surgeButton.roundRect(330, 250, surgeText.width + 20, surgeText.height + 20, 10);
surgeButton.fill('0x5b3775');
surgeButton.stroke(6, 0xffffff);
// humanAidButton.alpha = 0.75;
surgeButton.visible = false;
surgeButton.zIndex = 900;
surgeButton.eventMode = 'static';

surgeBackground.roundRect(310, 230, surgeText.width + 60, 160, 10);
surgeBackground.fill(0xd0d0d0);
surgeBackground.stroke(4, 0x000000);
surgeBackground.zIndex = 800;
surgeBackground.visible = false;
app.stage.addChild(surgeBackground);

app.stage.addChild(surgeButton);
surgeText.x = 340;
surgeText.y = 260;
surgeText.zIndex = 1000;
surgeText.visible = false;
app.stage.addChild(surgeText);

surgeButton.on('pointerdown', () => {
    // console.log("Surge!");
    surgeButton.visible = false;
    surgeText.visible = false;
    cancelSurgeButton.visible = false;
    cancelSurgeText.visible = false;
    surgeBackground.visible = false;
    clearMessage();
    socket.emit('game', 'action', { type: 'surge' }); // Request to conduct surge action
});

function surge() {
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 3) {
        Board.openCPQuery();
        return;
    }
    surgeButton.visible = true;
    surgeBackground.visible = true;
    surgeText.visible = true;
    cancelSurge();
    changeMessage("Confirm Surge Selection")
}

const cancelSurgeButton = new PIXI.Graphics();
const cancelSurgeText = new PIXI.Text({ text: "Cancel Surge", style: new PIXI.TextStyle({ fontSize: 20, fill: '#FFFFFF', align: 'center' }) });
cancelSurgeButton.roundRect(390, 330, cancelSurgeText.width + 20, cancelSurgeText.height + 20, 10);
cancelSurgeButton.fill('0xFF0000');
cancelSurgeButton.stroke(6, 0xffffff);
// cancelAidButton.alpha = 0.75;
cancelSurgeButton.zIndex = 900;
cancelSurgeButton.visible = false;
cancelSurgeButton.eventMode = 'static';
cancelSurgeButton.on('pointerdown', () => {
    // console.log("cancel surge!");
    cancelSurgeButton.visible = false;
    clearMessage();
    Board.openCPQuery();
    cancelSurgeText.visible = false;
    surgeBackground.visible = false;
    surgeButton.visible = false;
    surgeText.visible = false;
});

app.stage.addChild(cancelSurgeButton);
cancelSurgeText.x = 400;
cancelSurgeText.y = 340;
cancelSurgeText.zIndex = 900;
cancelSurgeText.visible = false;
app.stage.addChild(cancelSurgeText);

function cancelSurge() {
    cancelSurgeButton.visible = true;
    cancelSurgeText.visible = true;
}


socket.on('surge', (gameState) => {
    gameState = JSON.parse(gameState); // Parse the JSON string
    // console.log("SURGE");
    // console.log(gameState);
    updateBoard(gameState);
    if (playerColor != gameState.turnPlayer) {
        changeMessage("Opponent completed a surge\n waiting on them to spend command points");
    }
    else {
        Board.openCPQuery();
        clearMessage();
    }
}

);
//SURGE END
//SURGE END
//SURGE END


//INFLUENCE OPERATION START
//INFLUENCE OPERATION START
//INFLUENCE OPERATION START
const influenceButton = new PIXI.Graphics();
const influenceText = new PIXI.Text({text:"Roll for Influence Operation", style : new PIXI.TextStyle({fontSize:35, fill:'#FFFFFF', align:'center'})});
const influenceBackground = new PIXI.Graphics();

influenceButton.roundRect(250,250,influenceText.width+20,influenceText.height+20,10);
influenceButton.fill('0x5b3775');
influenceButton.stroke(6, 0xffffff);
influenceButton.visible=false;
influenceButton.zIndex=900;
influenceButton.eventMode = 'static';


influenceBackground.roundRect(230, 230, influenceText.width+60, 160, 10);
influenceBackground.fill(0xd0d0d0);
influenceBackground.stroke(4, 0x000000);
influenceBackground.zIndex=800;
influenceBackground.visible=false;
app.stage.addChild(influenceBackground);

app.stage.addChild(influenceButton);
influenceText.x = 260;
influenceText.y = 260;
influenceText.zIndex = 1000;
influenceText.visible=false;
app.stage.addChild(influenceText);

influenceButton.on('pointerdown', ()=> {
    // console.log("Influence!");
    influenceButton.visible = false;
    influenceText.visible=false;
    cancelInfluButton.visible=false;
    cancelInfluText.visible=false;
    influenceBackground.visible=false;
    clearMessage();
    // console.log("this is where we select influcence");
    // socket.emit('game', 'action', {type: 'influence-operation'});
    socket.emit('game', 'action', {type: 'influence-operation'}); // Request to conduct influence operation
});

function influenceOp(){

    let colors = {
        'blue' : 'bluePlayer',
        'red' : 'redPlayer'
    }

    if (gameState[colors[playerColor]].cp < 2)
    {
        Board.openCPQuery();
        return;
    }
    influenceButton.visible = true;
    influenceText.visible=true;
    influenceBackground.visible=true;
    cancelIO();
    changeMessage("Confirm Influence Operation\n(Rolls one dice per two civilian casualties)");
}

const cancelInfluButton = new PIXI.Graphics();
const cancelInfluText = new PIXI.Text({text:"Cancel IO", style : new PIXI.TextStyle({fontSize:20, fill:'#FFFFFF', align:'center'})});
cancelInfluButton.roundRect(410,330,cancelInfluText.width+20,cancelInfluText.height+20,10);
cancelInfluButton.fill('0xFF0000');
cancelInfluButton.stroke(6, 0xffffff);
cancelInfluButton.zIndex = 900;
cancelInfluButton.visible=false;
cancelInfluButton.eventMode = 'static';
cancelInfluButton.on('pointerdown', ()=> {
    // console.log("cancel Influ!");
    clearMessage();
    Board.openCPQuery();
    cancelInfluButton.visible=false;
    cancelInfluText.visible=false;
    influenceButton.visible=false;
    influenceBackground.visible=false;
    influenceText.visible=false;
});

app.stage.addChild(cancelInfluButton);
cancelInfluText.x = 420;
cancelInfluText.y = 340;
cancelInfluText.zIndex = 900;
cancelInfluText.visible=false;
app.stage.addChild(cancelInfluText);

function cancelIO(){
    cancelInfluButton.visible = true;
    cancelInfluText.visible=true;
}

// socket.on('influenceOperation', (gameState) => {
socket.on('influenceOperation', (response) => {
    let parsedResponse = JSON.parse(response); // Parse the JSON string
    let result = parsedResponse.result;
    gameState = parsedResponse.gameState;
    updateBoard(gameState);

    if (playerColor != gameState.turnPlayer){
        changeMessage("Opponent is rolling for Influence Operations");
    }

    const iORoll = new PIXI.Graphics();
    let x2 = result.length * 85 + 20
    iORoll.roundRect(300, 150, (350 > x2 ? 350 : x2), 200, 10);
    iORoll.fill('0x808080');
    iORoll.stroke(4, '0xffffff');
    iORoll.zIndex = 920;
    iORoll.eventMode = 'static';
    app.stage.addChild(iORoll);

    const iOText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }) });
    iOText.x = 350;
    iOText.y = 160;
    iOText.zIndex = 1000
    let sixs = result.filter(num => num === 6).length
    iOText.text = "-" + sixs + " opponent support points\n\n\n\n\n\nClick to Continue"
    app.stage.addChild(iOText);

    for (let i = 0; i < result.length; i++) {

        let iODice = Board.drawDice(310 + i * 85, 220, 950, result[i], 'black');
        app.stage.addChild(iODice);
        iORoll.on('pointerdown', () => {
            app.stage.removeChild(iODice);
        })

    }

    iORoll.on('pointerdown', () => {
        app.stage.removeChild(iORoll);
        app.stage.removeChild(iOText);
        if (playerColor == gameState.turnPlayer) {
            Board.openCPQuery();
        }
        else {
            changeMessage("Opponent completed Influence Operation\nWaiting on them to spend command points");
        }
    })

});

////END INFLUENCE OPERATIONS
////END INFLUENCE OPERATIONS
////END INFLUENCE OPERATIONS



/////CHMR START
/////CHMR START
/////CHMR START
//first step, create the highlight locations for the safe havens
/////locations defined as path in format path: [x1,y1,x2,y2...] counterclockwise
const havenLoc = {
    'haven1':{path:[219,280,252,304,296,271,302,208,259,183,214,218]},
    'haven2':{path:[306,207,301,271,333,283,413,195,397,155,363,153]},
    'haven3':{path:[337,283,356,320,427,306,470,245,461,214,415,200]},
    'haven4':{path:[345,368,379,395,445,386,460,357,428,317,357,324]},
    'haven5':{path:[506,182,591,220,627,192,607,157,537,118,507,129]},
    'haven6':{path:[474,244,542,284,590,270,589,224,503,187,466,214]},
    'haven7':{path:[535,334,588,368,647,344,657,304,594,278,544,291]},
    'haven8':{path:[451,389,477,424,577,421,585,371,533,339,465,357]},
    'haven9':{path:[629,186,699,224,747,197,757,134,651,109,612,154]},
    'haven10':{path:[651,348,687,378,787,229,751,201,701,231,660,303]}
}

const highlightedHavens = {};

let havenNames = ['haven3','haven4','haven6','haven7','haven8'];

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
    changeMessage(message);
}



function removeHavenHighlight() {
    for (var member in highlightedHavens) {
        app.stage.removeChild(highlightedHavens[member]);
        delete highlightedHavens[member];
    }
}



let selectedHavens = "none"

let selectedNodes = [];
function chmrClicked(){
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 2)
    {
        Board.openCPQuery();
        return;
    }

    var find = Board.findNodes(playerColor,getCount(gameState.nodes));
    ////cancel CHMR
    // cancelCHMR();
    highlight(find, chmr1, "Pick the node where CHMR will be conducted from");
}

function chmr1(name){
    console.log("inchmr1"+name);
    selectedNodes[selectedNodes.length] = name;
    
    let numbers = getCount(gameState.nodes);
    let maxVal = 0;
    if (playerColor == 'blue')
    {
        maxVal = numbers[name].blueArmies;
    }
    else 
        maxVal = numbers[name].redArmies;

    console.log("sending the name to server "+name + " " + maxVal+"armies");

    let counter = selectArmyNumber(400,200,1, maxVal,sendCHMRMessage1, name);
    changeMessage("Select the number of armies to conduct CHMR");
    app.stage.addChild(counter);

    // socket.emit('game','action',{type:'chmr-select',data:safeNode});

    removeHighlight();
    // clearMessage();

}

function sendCHMRMessage1(nodeName, selectedNumber){
    console.log(nodeName + " " + selectedNumber);
    clearMessage();
}

function selectArmyNumber(x,y,minValue, maxValue,onSelect,nodeName){

    const container = new PIXI.Container();
    container.position.set(x,y);
    container.zIndex=1001;

    let selectedNumber = 1;

    const text = new PIXI.Text({text: selectedNumber, style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' })});
    text.anchor.set(0.5);
    text.zIndex = 10;
    text.position.set(70, 30);
   
    const background = new PIXI.Graphics();
    background.roundRect(0,0,140,100,10);
    background.fill('0x000000');
    background.zIndex = 0;
    

    const plusButton = new PIXI.Text({text: '+', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' })})
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

    const minusButton =new PIXI.Text({text: '-', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' })}) 
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

    const confirmButton = new PIXI.Text({text: 'OK', style: new PIXI.TextStyle({ fontSize: 24, fill: 'white' })})
    confirmButton.anchor.set(0.5);
    confirmButton.position.set(70, 70);
    confirmButton.interactive = true;
    confirmButton.buttonMode = true;
    confirmButton.zIndex=10;
    confirmButton.on('pointerdown', () => {
        // onSelect(selectedNumber);
        onSelect(nodeName, selectedNumber);
        container.visible = false; // Hide the selector after selection
    });

    container.addChild(minusButton, text, plusButton, confirmButton,background);
    return container;
}



// socket.on('chmr-list',(lists) => {
//     let listOfHavens = JSON.parse(lists);
//     console.log(listOfHavens);
//     if (playerColor == gameState.turnPlayer){
//         chmrReceived(listOfHavens);
//     }
// });

let havenssss = ['haven1','haven2'];

function chmrReceived(/*havens*/){

    highlightHavens(havenssss, chmr2, "Which safe haven would you like to move to?");
}


function chmr2(name){
    //lets the user select the nodes that the armies are moving to. 
    selectedHavens = name;
    console.log("Selected haven " + selectedHavens);
    // socket.emit('game', 'action', { type: 'move-confirm', data: selections2 });
    removeHavenHighlight();
    clearMessage();

}

function chmr2Recived(nodes){
    highlight(nodes,chmr3,"Select node to move civilians from");
}

function chmr3(name){
    removeHighlight();
    clearMessage;
    console.log("selected ndoe" + name);
    //////////server message goes here. emiittttt
}

function chmr3Received(){

// socket.on('influenceOperation', (gameState) => {
// socket.on('influenceOperation', (response) => {
    // let parsedResponse = JSON.parse(response); // Parse the JSON string
    // let result = parsedResponse.result;
    // gameState = parsedResponse.gameState;
    // updateBoard(gameState);

    let result = [4,5,6,6];

    if (playerColor != gameState.turnPlayer){
        changeMessage("Opponent is rolling for CHMR");
    }

    const chmrRoll = new PIXI.Graphics();
    let x2 = result.length * 85 + 20
    chmrRoll.roundRect(300, 150, (275 > x2 ? 275 : x2), 200, 10);
    chmrRoll.fill('0x808080');
    chmrRoll.stroke(4, '0xffffff');
    chmrRoll.zIndex = 920;
    chmrRoll.eventMode = 'static';
    app.stage.addChild(chmrRoll);

    const chmrText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }) });
    chmrText.x = 350;
    chmrText.y = 160;
    chmrText.zIndex = 1000
    let moves = result.filter(num => num === 6).length * 2 + result.filter(num => num >=3 && num <= 5).length;
    chmrText.text = moves + " civilians moved\n\n\n\n\n\nClick to Continue"
    app.stage.addChild(chmrText);

    for (let i = 0; i < result.length; i++) {

        let chmrDice = Board.drawDice(310 + i * 85, 220, 950, result[i], 'black');
        app.stage.addChild(chmrDice);
        chmrRoll.on('pointerdown', () => {
            app.stage.removeChild(chmrDice);
        })

    }

    chmrRoll.on('pointerdown', () => {
        app.stage.removeChild(chmrRoll);
        app.stage.removeChild(chmrText);
        if (playerColor == gameState.turnPlayer) {
            Board.openCPQuery();
        }
        else {
            changeMessage("Opponent completed CHMR\nWaiting on them to spend command points");
        }
    })

}//);



//CHMR END
//CHMR END
//CHMR END

///ARTILLERY FIRES START
///ARTILLERY FIRES START

function firesClicked(){
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 1)
    {
        Board.openCPQuery();
        return;
    }
    var find = Board.findNodes(playerColor,getCount(gameState.nodes));
    cancelFires();
    highlight(find, fires1, "Pick the node where your army will fire from");
}

let fireSelection = "noneSelected";

function fires1(name){
    // console.log("in fires one");
    fireSelection = name;
    cancelFireButton.visible=false;
    cancelFireText.visible=false;
    console.log("this is where the server message would go");
    // console.log("Attacking from " +fireSelection);
    removeHighlight();
    clearMessage();

    // socket.emit('game','action',{type:'fire-select',data:fireSelection});
}

let nodes = ['city4','village3']

function fireReceived(/*nodes*/){
    if (nodes.length > 0)
        highlight(nodes, fires2, "pick the node where you are firing to");
    else{
        changeMessage("No adjacent nodes to selected node with armies");
        Board.openCPQuery();
    }
}

const cancelFireButton = new PIXI.Graphics();
const cancelFireText = new PIXI.Text({text:"Cancel Fire", style : new PIXI.TextStyle({fontSize:20, fill:'#FFFFFF', align:'center'})});
cancelFireButton.roundRect(90,100,cancelFireText.width+20,cancelFireText.height+20,10);
cancelFireButton.fill('0xFF0000');
cancelFireButton.stroke(6, 0xffffff);
cancelFireButton.alpha = 0.75;
cancelFireButton.visible=false;
cancelFireButton.eventMode = 'static';
cancelFireButton.on('pointerdown', ()=> {
    // console.log("cancel fire!");
    cancelFireButton.visible = false;
    removeHighlight();
    clearMessage();
    Board.openCPQuery();
    cancelFireText.visible=false;
});

app.stage.addChild(cancelFireButton);
cancelFireText.x = 100;
cancelFireText.y = 110;
cancelFireText.zIndex = 1000;
cancelFireText.visible=false;
app.stage.addChild(cancelFireText);


function cancelFires(){
    cancelFireButton.visible = true;
    cancelFireText.visible=true;
}

socket.on('fire-list',(list) => {
    let parsedList = JSON.parse(lists); // Parse the JSON string
    // console.log(parsedList);
    if (playerColor == gameState.turnPlayer){
        moveReceived(parsedList);
    }
});

let fireSelection2 = "noneSelected";

function fires2(name){
    // console.log("in fire 2");
    fireSelection2 = name;
    console.log("this is where the server message would go " + name)

    //socket.emit('game','action',{type:"fires-confirm', data:fireSelection2"});

    removeHighlight();
    clearMessage();
    fireSelection = "none";
    fireSelection2 = "none";
}

// socket.on('artillaryFires', (response) => {

function endFires(){
    // let parsedResponse = JSON.parse(response); // Parse the JSON string
    // console.log(parsedResponse.result); // Should be an array of numbers 1-6
    // console.log(parsedResponse.gameState); // Should print the entire updated gameState
    // gameState = parsedResponse.gameState;
    // updateBoard(gameState);

    if (playerColor != gameState.turnPlayer) {
        changeMessage("Opponent rolled for artillery fires");
    }


    let rolls1 = [1,2,3];
    let rolls2 = [4,5,6];

    const firesText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }) });
    firesText.x = 390;
    firesText.y = 160;
    firesText.zIndex = 1000
    firesText.text = "X Armies Killed" + "\n\n\n\n\nY Civilians Killed\n\n\n\n\nClick to Continue"
    app.stage.addChild(firesText);

    const firesRoll = new PIXI.Graphics();
    firesRoll.roundRect(300, 150, 350, firesText.height + 30, 10);
    firesRoll.fill('0x808080');
    firesRoll.stroke(4, '0xffffff');
    firesRoll.zIndex = 900;
    firesRoll.eventMode = 'static';
    app.stage.addChild(firesRoll);

    for (let i = 0; i < rolls1.length; i++) {

        let dice1 = Board.drawDice(345 + i*95, 200, 950,rolls1[i],playerColor);
        let dice2 = Board.drawDice(345 + i*95, 320, 950,rolls2[i],'black');

        app.stage.addChild(dice1);
        app.stage.addChild(dice2);
        firesRoll.on('pointerdown',()=>{
            app.stage.removeChild(dice1);
            app.stage.removeChild(dice2);
        }) 

    }

    firesRoll.on('pointerdown', () => {
        app.stage.removeChild(firesRoll);
        app.stage.removeChild(firesText);
        if (playerColor == gameState.turnPlayer) {
            Board.openCPQuery();
        }
        else {
            changeMessage("Opponent completed Artillery Fires\nWaiting on them to spend command points");
        }
    })
}

//ARTILLERY FIRES END
//ARTILLERY FIRES END
//ARTILLERY FIRES END

///////AIR STRIKE START
///////AIR STRIKE START
///////AIR STRIKE START

function strikeClicked(){
    if (gameState[(('blue' == playerColor) ? 'bluePlayer' : 'redPlayer')].cp < 2) {
            Board.openCPQuery();
            return;
        }

    var find = Board.findNodes(('blue' == playerColor ? 'red':'blue'),getCount(gameState.nodes));
    // console.log(find);
    cancelStrike();
    highlight(find, strike1, "Pick the node you would like to attack");
}


function strike1(name){
    // console.log("in strike 1");
    cancelStrikeButton.visible = false;
    cancelStrikeText.visible = false;


    console.log("This is where the server message would go");
    // socket.emit('game', 'action', { type: 'strike-select', data: selections });
    removeHighlight();
    clearMessage();
    
}

function strikeReceive(){

// let parsedResponse = JSON.parse(response); // Parse the JSON string
    // console.log(parsedResponse.result); // Should be an array of numbers 1-6
    // console.log(parsedResponse.gameState); // Should print the entire updated gameState
    // gameState = parsedResponse.gameState;
    // updateBoard(gameState);

if (playerColor != gameState.turnPlayer) {
        changeMessage("Opponent rolled for air strike");
    }
    
    let rolls1 = [1,2];
    let rolls2 = [4,5];

const strikeText = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }) });
    strikeText.x = 390;
    strikeText.y = 160;
    strikeText.zIndex = 1000
    strikeText.text = "X Armies Killed" + "\n\n\n\n\nY Civilians Killed\n\n\n\n\nClick to Continue"
    app.stage.addChild(strikeText);

    const strikeRoll = new PIXI.Graphics();
    strikeRoll.roundRect(300, 150, 350, strikeText.height + 30, 10);
    strikeRoll.fill('0x808080');
    strikeRoll.stroke(4, '0xffffff');
    strikeRoll.zIndex = 900;
    strikeRoll.eventMode = 'static';
    app.stage.addChild(strikeRoll);

    for (let i = 0; i < rolls1.length; i++) {

        let dice1 = Board.drawDice(385 + i*95, 200, 950,rolls1[i],playerColor);
        let dice2 = Board.drawDice(385 + i*95, 320, 950,rolls2[i],'black');

        app.stage.addChild(dice1);
        app.stage.addChild(dice2);
        strikeRoll.on('pointerdown',()=>{
            app.stage.removeChild(dice1);
            app.stage.removeChild(dice2);
        }) 

    }

    strikeRoll.on('pointerdown', () => {
        app.stage.removeChild(strikeRoll);
        app.stage.removeChild(strikeText);
        if (playerColor == gameState.turnPlayer) {
            Board.openCPQuery();
        }
        else {
            changeMessage("Opponent completed Air Strike\nWaiting on them to spend command points");
        }
    })

}

const cancelStrikeButton = new PIXI.Graphics();
const cancelStrikeText = new PIXI.Text({text:"Cancel Strike", style : new PIXI.TextStyle({fontSize:20, fill:'#FFFFFF', align:'center'})});
cancelStrikeButton.roundRect(90,100,cancelStrikeText.width+20,cancelStrikeText.height+20,10);
cancelStrikeButton.fill('0xFF0000');
cancelStrikeButton.stroke(6, 0xffffff);
cancelStrikeButton.alpha = 0.75;
cancelStrikeButton.visible=false;
cancelStrikeButton.eventMode = 'static';
cancelStrikeButton.on('pointerdown', ()=> {
    // console.log("cancel strike!");
    cancelStrikeButton.visible = false;
    removeHighlight();
    clearMessage();
    Board.openCPQuery();
    cancelStrikeText.visible=false;
});

app.stage.addChild(cancelStrikeButton);
cancelStrikeText.x = 100;
cancelStrikeText.y = 110;
cancelStrikeText.zIndex = 1000;
cancelStrikeText.visible=false;
app.stage.addChild(cancelStrikeText);


function cancelStrike(){
    cancelStrikeButton.visible = true;
    cancelStrikeText.visible=true;
}

//AIR STRIKE END
//AIR STRIKE END
//AIR STRIKE END



//////CLOSE COMBAT START
//////CLOSE COMBAT START
//////CLOSE COMBAT START
// function closeCombat(){
socket.on('close-combat', (data) => {
    let parsedData = JSON.parse(data);
    console.log(parsedData.rolls); // Should print all four arrays of rolls
    console.log(parsedData.gameState); // Should print the entire updated gameState
    console.log(parsedData.next); // Should print name of a node (or undefined if close combat is over)

    let rolls = parsedData.rolls;
    let next = parsedData.next;

    if (!rolls && next) {
        combatSelect(next);
        // socket.emit('game','action',{type:'chmr-select',data:safeNode}) 
    }
    else if (rolls && next) {
        displayRolls(rolls, next);
        gameState = parsedResponse.gameState;
        updateBoard(gameState);
    }
    else {
        socket.emit('game','civ-move');console.log("send civ-move");
    }

    // if (parsedData.next) {
    //     socket.emit('game', 'close-combat', 0); // Always choose 0 dice to roll
    // }
});

let rolls1 =
{
    blueRolls: [1, 2, 3, 4, 5],
    redRolls: [2, 3, 4],
    blueCivRolls: [3, 4, 5, 4, 5],
    redCivRolls: [4, 5, 6]
}

function displayRolls(rolls, next) {

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

    const rollDisplay = Board.makeRoundRect(290, 160, length, height, 10, 0x808080, 3, 0xffffff, 1000, true);
    for (let i = 0; i < playerRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 195, 1001, playerRolls[i], gameState.turnPlayer);
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }
    for (let i = 0; i < otherPlayerRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 305, 1001, otherPlayerRolls[i], (gameState.turnPlayer == 'blue' ? 'red' : 'blue'));
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }

    for (let i = 0; i < playerCivRolls.length; i++) {
        let dice = Board.drawDice(310 + i * 95, 415, 1001, playerCivRolls[i], gameState.turnPlayer);
        app.stage.addChild(dice);
        rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
    }
    if (otherPlayerCivRolls) {
        for (let i = 0; i < otherPlayerCivRolls.length; i++) {
            let dice = Board.drawDice(310 + i * 95, 525, 1001, otherPlayerCivRolls[i], (gameState.turnPlayer == 'blue' ? 'red' : 'blue'));
            app.stage.addChild(dice);
            rollDisplay.on('pointerdown', () => { app.stage.removeChild(dice) });
        }
    }
    let text1 = Board.makeBoardText(310, 170, gameState.turnPlayer + " rolled for armies:", new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }))
    let text2 = Board.makeBoardText(310, 280, gameState.turnPlayer == 'blue' ? 'red' : 'blue' + " rolled for armies:", new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }))
    let text3 = Board.makeBoardText(310, 390, gameState.turnPlayer + " rolled for civilians:", new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }))
    let cont = Board.makeBoardText(310, 610, "Click to continue", new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' }))
    let text4 = "none";
    if (otherPlayerCivRolls) { text4 = Board.makeBoardText(310, 500, gameState.turnPlayer == 'blue' ? 'red' : 'blue' + " rolled for civilians:", new PIXI.TextStyle({ fontSize: 22, fill: '#FFFFFF', align: 'center' })) }
    text1.zIndex = 10000;
    text2.zIndex = 10000;
    cont.zIndex = 10000;
    text3.zIndex = 10000;
    if (text4 != "none") {
        text4.zIndex = 10000;
        app.stage.addChild(text4);
    }
    app.stage.addChild(text1, text2, text3, cont);





    rollDisplay.eventMode = 'static';
    rollDisplay.on('pointerdown', () => {
        app.stage.removeChild(rollDisplay);
        app.stage.removeChild(text1, text2, text3, cont);
        if (text4 != "none") app.stage.removeChild(text4);
        if (next) {
            combatSelect(next);
        }
        else {
            socket.emit('game','civ-move');
            console.log("send civ-move");
        }
    })




    app.stage.addChild(rollDisplay);

}

function combatSelect(next){
    console.log("In combat select");
        highlight([next], clicked, "Select Number of Armies for close combat in " + next);
    
        let numbers = getCount(gameState.nodes);
        let maxVal = 0;
        if (playerColor == 'blue') {
            maxVal = numbers[next].blueArmies;
        }
        else
            maxVal = numbers[next].redArmies;

        let counter = selectArmyNumber(highLoc[next].x - 57, highLoc[next].y +100, 0, maxVal, closeCombatMessage, next);
        
        app.stage.addChild(counter);
}

//////
function closeCombatMessage(number, name){
        removeHighlight();
        // console.log(number + " " + name);
        clearMessage();
        socket.emit('game','action',{type:'chmr-select',data:number});

    }


//CLOSE COMBAT ANIMATION FUNCTION! 


function closeCombatAnim(ctx, originX, originY, duration) {
    const m4 = new Image();
  //  m4.src = "/content/m4.png";
     m4.src = "/content/m4_3.png";
  
    const RIFLE_WIDTH = 85*1.25;
    const RIFLE_HEIGHT = 40*1.25;
    const HALF_WIDTH = RIFLE_WIDTH / 2;
    const HALF_HEIGHT = RIFLE_HEIGHT / 2;
  
    let angle = 0;
    let clashed = falsesocket.emit('civ-move');;
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
      ctx.strokeStyle = `rgba(255, 60, 0, ${ringOpacity.toFixed(2)*4})`;
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

//const ctx = app.canvas.getContext("2d");
//   /playRifleClash(ctx, 150, 150, 1500);
//const ctx = canvas.getContext('2d');
// DOESN'T WORK HERE.


// CALL THE CLOSE COMBAT FUNCTION LIEK THE FOLLOWING :::

//const fxCtx = fxCanvas.getContext("2d");
//closeCombatAnim(fxCtx, 100, 100, 3500); //x y milliseconds of animation
  

//closeCombatAnim(fxCtx, 100, 100, 3500); //x y milliseconds of animation

function doCC() { 
    // 257, y: 299 },
    const fxCtx = fxCanvas.getContext("2d");
    closeCombatAnim(fxCtx, 309, 339, 3500)
}


function playBobbingArrow(ctx, originX, originY, duration) {
    const arrow = new Image();
    arrow.src = "/content/arrow.png"; 
  
    const WIDTH = 80*1.2;
    const HEIGHT = 72*1.2;
  
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



// arrow pointing at civilians when updated.


function doArrow() { 
    const fxCtx = fxCanvas.getContext("2d");
    playBobbingArrow(fxCtx, 100, 100, 10000); // Show arrow at (150, 100) for 2 seconds
}




//testing buttons

function createDebugTable() {
    const debugDiv = document.getElementById("debug-buttons");

    // Create table
    const table = document.createElement("table");
    table.style.border = "1px solid black";
    table.style.borderCollapse = "collapse";

    // Define button actions (each button triggers a specific function)
    const actions = [
        { name: "Strike Received", func: strikeReceive },
        { name: "Fires Received 1", func: fireReceived },
        { name: "Fired Received 2", func: endFires },
        { name: "CHMR Received 1" , func: chmrReceived},
        { name: 'CHMR Received 2', func: chmr3Received},
        { name: "Close Combat animation", func: doCC},
        { name: "Arrow animation", func: doArrow},
        { name: "CP Button", func: Board.openCPQuery},
        { name: "Display Rolls",func: ()=>{displayRolls(rolls1,"redBase")}},
        { name: "send civ-move", func: ()=>{socket.emit('game','civ-move');console.log("send civ-move");}},
        { name: "send civ-return", func: ()=> {socket.emit('game','civ-return');console.log("send civ-return");}}

        
    ];

    // Number of columns per row
    const numCols = 1;
    let row = document.createElement("tr");

    actions.forEach((action, index) => {
        let cell = document.createElement("td");
        cell.style.border = "1px solid black";
        cell.style.padding = "10px";

        let button = document.createElement("button");
        button.textContent = action.name;
        button.onclick = action.func;  // Calls the corresponding function

        cell.appendChild(button);
        row.appendChild(cell);

        // If row is full, append to table and start a new row
        if ((index + 1) % numCols === 0) {
            table.appendChild(row);
            row = document.createElement("tr");
        }
    });

    // Append the last row if not full
    if (row.children.length > 0) {
        table.appendChild(row);
    }

    debugDiv.appendChild(table);
}

// Call function to generate the debug table
createDebugTable();