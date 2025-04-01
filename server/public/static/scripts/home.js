import {createAboutButton} from './homebuttons/aboutButton.js';
import { createHowButton } from './homebuttons/howToPlay.js';
import { createJoinButton } from './homebuttons/joinButton.js';
import{createStartButton} from './homebuttons/startButton.js';

const app = new PIXI.Application();
await app.init({ height:650, width:1000, background: '#DFE8F3' });
document.body.appendChild(app.canvas);

// load the PNG asynchronously
await PIXI.Assets.load(['content/backgroundVV.png', 
                        'content/StartGameVV.png',
                        'content/joingame.png',
                        'content/howtoplay.png',
                        'content/about.png']);


//create the background
const background = PIXI.Sprite.from('content/backgroundVV.png');

// make the width of the game the size of the screen and 
// then make the height proportional. 
const w = 970;
const h = w * (495 / 899);

background.width = w;
background.height = h;

// add background to the canvas
app.stage.addChild(background);

// Create a text style (you can customize it)
const textStyle = new PIXI.TextStyle({
    fontFamily: 'Lexend',
    fontSize: 30,
    fill: '#000000', //black text color
    align: 'center'
});


createStartButton(app, 365, 150);

const createText = new PIXI.Text({text: "Create Game", style: textStyle})
createText.x= 405;
createText.y=163;
app.stage.addChild(createText);

createJoinButton(app, 365, 220);

const joinText = new PIXI.Text({text: "Join Game", style: textStyle})
joinText.x= 420;
joinText.y=235;
app.stage.addChild(joinText);

const quickbutton = new PIXI.Graphics();
quickbutton.roundRect(365,290,240, 60);
quickbutton.fill(0xd9f2d0);
quickbutton.stroke({width:1,color:0x000000});
quickbutton.eventMode = 'static';
quickbutton.cursor = 'pointer';
    //adding the create Private Session Listener
quickbutton.on('pointerdown', startClick);
app.stage.addChild(quickbutton);

const quickText = new PIXI.Text({text: "Quick Play", style: textStyle})
quickText.x= 420;
quickText.y=305;
app.stage.addChild(quickText);

createHowButton(app, 365, 360);

const howText = new PIXI.Text({text: "How To Play", style: textStyle})
howText.x= 406;
howText.y=375;
app.stage.addChild(howText);


createAboutButton(app, 365,430);

const aboutText = new PIXI.Text({text: "About", style: textStyle})
aboutText.x= 445;
aboutText.y=445;
app.stage.addChild(aboutText);

// Create a text style (you can customize it)
const VVStyle = new PIXI.TextStyle({
    fontFamily: 'Lexend',
    fontSize: 60,
    fill: '#000000', //black text color
    align: 'center'
});


const VVText = new PIXI.Text({text: "Violent Victory", style: VVStyle})
VVText.x= 310;
VVText.y=45;
VVText.zIndex=105;
app.stage.addChild(VVText);


const VVCredit = new PIXI.Text({text: "by Rachel McVicker and Sebastian J. Bae", style: new PIXI.TextStyle({fontFamily:'Legend',fontSize : 30, fill: 0x000000, align: 'center'})})
VVCredit.x= 255;
VVCredit.y=112;
VVCredit.zIndex=105;
app.stage.addChild(VVCredit);


const VVCredit2 = new PIXI.Text({text: "Digitized By:\nJoshua Clark\nLauren Leckelt\nCaleb Walker", style: new PIXI.TextStyle({fontFamily:'Legend',fontSize : 20, fill: 0x000000, align: 'left'})})
VVCredit2.x = 10;
VVCredit2.y = 430;
VVCredit2.xIndex = 105;
app.stage.addChild(VVCredit2);

const startInst = new PIXI.Text({text: "", style: new PIXI.TextStyle({fontFamily:'Legend', fontSize:25,fill:0x000000, align: 'left'})});
startInst.x = 635;
startInst.y = 180;
startInst.text = 'To start a game, click Create Game.\nGive the code that appears to your \nopponent. Your opponent then\ntypes the code into the text box and\npresses enter and the game starts.';
app.stage.addChild(startInst);



//quickplay. currently not on any of the buttons but it is useful. 
function startClick(){
    this.isdown=true;
    this.alpha=1;
    socket.emit('play','quick');
    background.zIndex=100;
}



// Create a div to display the gameID (Initially invisible)
    let gameIDDiv = document.createElement('div');
    gameIDDiv.id = 'gameIDBox';
    gameIDDiv.style.position = 'absolute';
    gameIDDiv.style.top = `165px`;
    gameIDDiv.style.left = '175px';
    gameIDDiv.style.padding = '10px';
    gameIDDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    gameIDDiv.style.color = '#fff';
    gameIDDiv.style.fontSize = '24px';
    gameIDDiv.style.textAlign = 'center';
    gameIDDiv.style.userSelect = 'text'; // Make the text selectable
    gameIDDiv.style.display = 'none'; // Initially hidden
    document.body.appendChild(gameIDDiv);

socket.on('game-id', (gameID) => {
    console.log(gameID);
    //added from chatgpt should help with updating the variables of the game state.
    // displayID(gameID); 
    gameIDDiv.textContent = `Game ID: ${gameID}`;  // Update the div with the game ID
    gameIDDiv.style.display = 'block'; // Make the div visible once the gameID is received
});


function dev(){
    socket.emit('play','dev');
}


