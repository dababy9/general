socket.on('message-log', (messages) => {
    console.log(messages);
});

socket.on('game-state', (game) => {
    console.log(game);
});

socket.on('new-message', (message) => {
    console.log(message.from);
    console.log(message.data);
});

document.getElementById('btn').addEventListener('click', () => {
    socket.emit('send-message', 'hello');
});



const app = new PIXI.Application();
await app.init({ resizeTo: window, background: '#DFE8F3'});
document.body.appendChild(app.canvas);
// load the PNG asynchronously
await PIXI.Assets.load(['/content/backgroundVV.png', 
                        '/content/Board_Circles.png',
                        '/content/button.png',
                        '/content/grass.png']);

const background = PIXI.Sprite.from('/content/backgroundVV.png');

// make the width of the game the size of the screen and 
// then make the height proportional. 
const w = 990 - 20;
const h = w * (495 / 899);

background.width = w;
background.height = h;

app.stage.addChild(background);

//add grass to the canvas
let grass = PIXI.Sprite.from('/content/grass.png');

const gw = grass.width;
const gh = grass.height;

grass.width = w-75;
grass.height = gh * grass.width / gw

grass.x = 70;
grass.y = 5;

app.stage.addChild(grass);

// add board to the canvas

let board = PIXI.Sprite.from('/content/Board_Circles.png');

const bw = board.width;
const bh = board.height;

board.width = w-220;
board.height = bh * board.width / bw

board.x = 110;
board.y = 40;

app.stage.addChild(board);


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

function goToBoard(){
    this.isdown=true;
    this.alpha=1;
    window.location.replace('/');
}