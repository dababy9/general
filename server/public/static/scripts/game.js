socket.on('message-log', (messages) => {
    console.log(messages);
});

var gameState;

socket.on('game-state', (game) => {
    console.log(game);
    gameState = game;
    console.log(gameState);
});

socket.on('new-message', (message) => {
    console.log(message.from);
    console.log(message.data);
});

socket.emit('fetch', 'game-state');



const app = new PIXI.Application();
await app.init({ resizeTo: window, background: '#DFE8F3'});
document.body.appendChild(app.canvas);
// load the PNG asynchronously
await PIXI.Assets.load(['/content/backgroundVV.png', 
                        '/content/Board_Circles.png',
                        '/content/button.png',
                        '/content/grave.png',
                        '/content/RedCP.png',
                        '/content/BlueCP.png',
                        '/content/blueSurge.png',
                        '/content/redSurge.png',
                        '/content/support_tracker.png']);

const background = PIXI.Sprite.from('/content/backgroundVV.png');

// make the width of the game the size of the screen and 
// then make the height proportional. 
const w = 990 - 20;
const h = w * (495 / 899);

background.width = w;
background.height = h;

app.stage.addChild(background);

// add board to the canvas

let board = PIXI.Sprite.from('/content/Board_Circles.png');

const bw = board.width;
const bh = board.height;

board.width = w-220;
board.height = bh * board.width / bw

board.x = 110;
board.y = 40;

app.stage.addChild(board);

//add the left gravestone
let grave1 = PIXI.Sprite.from('/content/grave.png');
grave1.width = 100
grave1.height = 100;
grave1.x = 10;
grave1.y = 80;
app.stage.addChild(grave1);

//add the right gravestone
let grave2 = PIXI.Sprite.from('/content/grave.png');
grave2.width = 100
grave2.height = 100;
grave2.x = 950;
grave2.y = 475;
grave2.rotation = Math.PI;
app.stage.addChild(grave2);

//add the blue CP Tracker
let blueCP = PIXI.Sprite.from('/content/BlueCP.png');
blueCP.height = 250;
blueCP.width = 150;
blueCP.x = 20;
blueCP.y = 160;
app.stage.addChild(blueCP);

//add the red CP Tracker
let redCP = PIXI.Sprite.from('/content/RedCP.png');
redCP.height = 270;
redCP.width = 140;
redCP.x = 820;
redCP.y = 135;
app.stage.addChild(redCP);

//add the blue Surge
let blueSur = PIXI.Sprite.from('/content/blueSurge.png');
blueSur.height = 85;
blueSur.width = 100;
blueSur.x = 10;
blueSur.y = 400;
app.stage.addChild(blueSur);

//add the red Surge
let redSur = PIXI.Sprite.from('/content/redSurge.png');
redSur.height = 85;
redSur.width = 104;
redSur.x = 850;
redSur.y = 80;
app.stage.addChild(redSur);

//add the support tracker
let suppTrack = PIXI.Sprite.from('/content/support_tracker.png');
suppTrack.height = 115;
suppTrack.width = 500;
suppTrack.x = 230;
suppTrack.y = 490;
app.stage.addChild(suppTrack);


const menubutton = new PIXI.Sprite(PIXI.Texture.from('/content/button.png'));
const buttonw = menubutton.width;
menubutton.width = 25;
menubutton.height = 25 * buttonw / menubutton.height;
menubutton.x = 15;
menubutton.y = 15;
menubutton.eventMode = 'static';
menubutton.cursor = 'pointer';
menubutton.on('pointerdown',goToBoard);
app.stage.addChild(menubutton);



//////////////THIS IS GOING TO BE DELETED AND INSTEAD SENT BY SERVER

const player = {
    cp: 6,
    support: 7,
    surgeArmies: 8,
    casualties: 0
}

// Node definitions (only used in initialState)
const city = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 4
}
const village = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 2
}
const haven = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 0
}

// Initial game state
const initialState = {
    messages: [{from: "server", message: "Game Initialized!"}],
    bluePlayer: { ...player },
    redPlayer: { ...player },
    nodes: {
        'blueBase': {
            blueArmies: 16,
            redArmies: 0,
            civilians: 2
        },
        'redBase': {
            blueArmies: 10,
            redArmies: 16,
            civilians: 2
        },
        'city1': { ...city },
        'city2': { ...city },
        'city3': { ...city },
        'city4': { blueArmies: 5,
            redArmies: 16,
            civilians: 2},
        'village1': { ...village },
        'village2': { ...village },
        'haven1': { ...haven },
        'haven2': { ...haven },
        'haven3': { ...haven },
        'haven4': { ...haven },
        'haven5': {blueArmies: 5,
            redArmies: 7,
            civilians: 2 },
        'haven6': { ...haven },
        'haven7': { ...haven },
        'haven8': { ...haven }
    }
}

//////////////END OF DELETED SECTION

const locations = {
        'blueBase': {x: 162, y: 240},
        'redBase': {x: 770, y: 160},
        'city1': {x: 380, y: 240},
        'city2': {x: 470, y: 400},
        'city3': {x: 520, y: 80},
        'city4': {x: 675, y: 380},
        'village1': {x: 340, y: 80},
        'village2': {x: 600, y: 240},
        'haven1': {x: 280, y: 190},
        'haven2': {x: 280, y: 280},
        'haven3': {x: 430, y: 150},
        'haven4': {x: 490, y: 190},
        'haven5': {x: 490, y: 280},
        'haven6': {x: 570, y: 350},
        'haven7': {x: 650, y: 150},
        'haven8': {x: 690, y: 240}
    }

// Create a text style (you can customize it)
const redArmy = new PIXI.TextStyle({
    fontFamily: 'Arial',
    fontSize: 30,
    fill: '#c30010', // red text color
    align: 'center'
});

const blueArmy = new PIXI.TextStyle({
    fontFamily: 'Arial',
    fontSize: 30,
    fill: '#000080', // blue text color
    align: 'center'
});

const civStyle = new PIXI.TextStyle({
    fontFamily: 'Arial',
    fontSize: 30,
    fill: '#383838', // gray text color
    align: 'center'
});



let i = 0
for(const data in initialState.nodes){

   
    //add the amount of red armies to each location
    let redtext = initialState.nodes[data].redArmies
    if (redtext != '0'){
        const redText = new PIXI.Text({text: redtext, style: redArmy});
        
        // Set its position
        redText.x = locations[data].x; 
        redText.y = locations[data].y; 

        // Add the text to the stage
        app.stage.addChild(redText);
    }

    let bluetext = initialState.nodes[data].blueArmies
    if (bluetext!= '0'){
        const blueText = new PIXI.Text({text: bluetext, style: blueArmy}); 

        let x = 25;
        if (redtext > 9)
        {
            x = 35
        }
        blueText.x = locations[data].x + x; // Centered horizontally
        blueText.y = locations[data].y; // Centered vertically

        app.stage.addChild(blueText);
    }

    let civtext = initialState.nodes[data].civilians 
    if (civtext != '0'){
        const civText = new PIXI.Text({text: civtext, style: civStyle});
        

        const back = PIXI.Sprite.from('/content/backgroundVV.png');

        back.width = 20;
        back.height = 25;
        let x = 13;
        if (redtext > 9)
        {
            x = 20
        }
        back.x = locations[data].x + x
        back.y = locations[data].y + 30;

        app.stage.addChild(back);


        civText.x = locations[data].x + x; // Centered horizontally
        civText.y = locations[data].y + 25; // Centered vertically
        app.stage.addChild(civText);
    }
}

////////////////FOR TEXTING PURPOSES

// // Create text
// const text = new PIXI.Text('Drag me!', { fontSize: 24, fill: 'black' });
// text.x = 100;
// text.y = 100;
// text.interactive = true;
// text.buttonMode = true;

// app.stage.addChild(text);

// // Enable dragging
// let dragging = false;

// text.on('pointerdown', (event) => {
//     dragging = true;
//     text.data = event.data;
// });

// text.on('pointerup', () => {
//     dragging = false;
//     text.data = null;
//     text.alpha = 1;
// });

// text.on('pointermove', () => {
//     if (dragging) {
//         const newPosition = text.data.getLocalPosition(app.stage);
//         text.x = newPosition.x;
//         text.y = newPosition.y;
//         text.text = `x: ${Math.round(text.x)}, y: ${Math.round(text.y)}`;
//     }
// });

/////////////END OF TESTING PURPOSES


function goToBoard(){
    this.isdown=true;
    this.alpha=1;
    window.location.replace('/');
}