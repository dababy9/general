// import{ createPS} from './home.js';

export function createStartButton(app, x, y) {

    const w = 990 - 20;
const h = w * (495 / 899);
    //START BUTTON
    const startbutton = new PIXI.Graphics();
    startbutton.roundRect(x,y,240, 60);
    startbutton.fill(0xd9f2d0);
    startbutton.stroke({width:1,color:0x000000});
    startbutton.eventMode = 'static';
    startbutton.cursor = 'pointer';
    //adding the create Private Session Listener
    startbutton.on('pointerdown', createPS);
    app.stage.addChild(startbutton);

}

//creates a private session
function createPS(){
//sends back a game id
//display the id happens with the socket on
    this.isdown=true;
    this.alpha=1;
    console.log("Button Clicked");
    socket.emit('play','create');

}