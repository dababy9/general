socket.on('game-start', () => {
    window.location.replace('/game');
});

const app = new PIXI.Application();
await app.init({ resizeTo: window, background: '#DFE8F3'});
document.body.appendChild(app.canvas);
// load the PNG asynchronously
await PIXI.Assets.load(['content/backgroundVV.png', 
                        'content/StartGameVV.png',
                        'content/joingame.png',
                        'content/howtoplay.png',
                        'content/about.png']);

const background = PIXI.Sprite.from('content/backgroundVV.png');

// make the width of the game the size of the screen and 
// then make the height proportional. 
const w = 990 - 20;
const h = w * (495 / 899);

background.width = w;
background.height = h;

// add background to the canvas
app.stage.addChild(background);

// 
// const startButton = PIXI.Texture.from('static/content/StartGameVV.png');

// const buttons = [];

// const buttonPositions = [];
//START BUTTON
const startbutton = new PIXI.Sprite(PIXI.Texture.from('content/StartGameVV.png'));
startbutton.anchor.set(0.5);
startbutton.x = w/2;
startbutton.y = h/2 - 150;
startbutton.eventMode = 'static';
startbutton.cursor = 'pointer';
startbutton.on('pointerdown',goToBoard);
app.stage.addChild(startbutton);

const joinbutton = new PIXI.Sprite(PIXI.Texture.from('content/joingame.png'));
joinbutton.anchor.set(0.5);
joinbutton.width = startbutton.width;
joinbutton.height = startbutton.height;
joinbutton.x = w/2;
joinbutton.y = h/2 - 50;
joinbutton.eventMode = 'static';
joinbutton.cursor = 'pointer';
joinbutton.on('pointerdown',startClick);
app.stage.addChild(joinbutton);

const howbutton = new PIXI.Sprite(PIXI.Texture.from('content/howtoplay.png'));
howbutton.anchor.set(0.5);
howbutton.width = startbutton.width;
howbutton.height = startbutton.height;
howbutton.x = w/2;
howbutton.y = h/2 + 50;
howbutton.eventMode = 'static';
howbutton.cursor = 'pointer';
howbutton.on('pointerdown',startClick);
app.stage.addChild(howbutton);

const aboutbutton = new PIXI.Sprite(PIXI.Texture.from('content/about.png'));
aboutbutton.anchor.set(0.5);
aboutbutton.width = startbutton.width;
aboutbutton.height = startbutton.height;
aboutbutton.x = w/2;
aboutbutton.y = h/2 + 150;
aboutbutton.eventMode = 'static';
aboutbutton.cursor = 'pointer';
aboutbutton.on('pointerdown',startClick);
app.stage.addChild(aboutbutton);


////NEED TO FIND HOW TO FIX ERROR. PROBLEM WITH LINE 89
// const violentText = new Text({ text: 'Violent Victory' });

// violentText.x = w/2;
// violentText.y = h/2 - 200;

// app.stage.addChild(violentText);

// buttons.push(button);

function startClick(){
    this.isdown=true;
    this.alpha=1;
    socket.emit('quick-play');
}
function goToBoard(){
    this.isdown=true;
    this.alpha=1;
    socket.emit('private-play', 1234);
}