

export function getRedPhotos(){
    const redPhotos = {
    '1': '/content/red_one_die.png',
    '2': '/content/red_two_die.png',
    '3': '/content/red_three_die.png',
    '4': '/content/red_four_die.png',
    '5': '/content/red_five_die.png',
    '6': '/content/red_six_die.png'
}
return redPhotos;
}

export function getBluePhotos(){
    const bluePhotos = {
    '1': '/content/blue_one_die.png',
    '2': '/content/blue_two_die.png',
    '3': '/content/blue_three_die.png',
    '4': '/content/blue_four_die.png',
    '5': '/content/blue_five_die.png',
    '6': '/content/blue_six_die.png'
}
return bluePhotos;
}

export function getBlackPhotos() {
    const blackPhotos = {
    '1': '/content/black_one_die.png',
    '2': '/content/black_two_die.png',
    '3': '/content/black_three_die.png',
    '4': '/content/black_four_die.png',
    '5': '/content/black_five_die.png',
    '6': '/content/black_six_die.png'
}
return blackPhotos;
}

let redPhotos = getRedPhotos();
let bluePhotos = getBluePhotos();
let blackPhotos = getBlackPhotos();

export async function loadAssets(){
    //all of the photos that are used in the game 
await PIXI.Assets.load(['/content/backgroundVV.png', //background blue picture
    '/content/Board_Circles.png', //board picture with the nodes
    '/content/button.png', //menu button in top left of screen
    '/content/blueGrave.png',//blue grave
    '/content/redGrave.png',//red grave
    '/content/redCPTracker.png', //tracker for the red command points
    '/content/blueCPTracker.png', //tracker for the blue command points
    '/content/IFV_Blue.png', //Blue tank
    '/content/IFV_Red.png',  //red tank
    '/content/blueSurgeNew.png', //blue surge photo
    '/content/redSurgeNew.png', //red surge photo
    '/content/civilians2.png', //civilians photo
    '/content/VVInstructions.jpg', //powerpoint instructions that appear from menu button.
    redPhotos['1'],//dice photos being loaded
    redPhotos['2'],
    redPhotos['3'],
    redPhotos['4'],
    redPhotos['5'],
    redPhotos['6'],
    bluePhotos['1'],
    bluePhotos['2'],
    bluePhotos['3'],
    bluePhotos['4'],
    bluePhotos['5'],
    bluePhotos['6'],
    blackPhotos['1'],
    blackPhotos['2'],
    blackPhotos['3'],
    blackPhotos['4'],
    blackPhotos['5'],
    blackPhotos['6'],
    '/content/support_tracker.png']); //support tracker (bottom of screen)

}

export function makeBackground() {

    //create the sprite for the background. Set size that doesn't change. 
    const background = PIXI.Sprite.from('/content/backgroundVV.png');
    // width and height for the background
    background.width = 970;
    background.height = 634;
    return background;
}

export function makeGameBoard() {
    // add game board to the canvas
    let board = PIXI.Sprite.from('/content/Board_Circles.png');

    const bw = board.width;
    const bh = board.height;

    //make board proportional to background
    board.width = 970 - 220;
    board.height = bh * board.width / bw

    //when uncommented gives the location of the click in console
    // board.eventMode='static';

    // board.on('pointerdown',(e)=>{
    //     console.log(e.global);
    // })

    board.x = 110;
    board.y = 40;
    return board;
}

export function makeBoardPiece(x, y, width, height, name, z = 0) {
    let piece = PIXI.Sprite.from(name);
    piece.width = width;
    piece.height = height;
    piece.x = x;
    piece.y = y;
    piece.zIndex = z;
    return piece;
}

export function makeCircle(x, y, size, fill, stroke, strokefill, alpha) {
    const circle = new PIXI.Graphics();
    circle.circle(x, y, size);
    circle.fill(fill);
    circle.stroke(stroke, strokefill);
    circle.alpha = alpha;
    return circle;
}

export function makeBoardText(x, y, text, style) {
    const texts = new PIXI.Text({ text: text, style: style });
    texts.x = x;
    texts.y = y;
    return texts;
}

export function makeRoundRect(x, y, width, height, corner, fill, stroke, strokefill, z, vis = true) {
    const rect = new PIXI.Graphics();
    rect.roundRect(x, y, width, height, corner);
    rect.fill(fill);
    rect.stroke({ width: stroke, color: strokefill });
    rect.zIndex = z;
    rect.visible = vis;
    return rect;
}

export function drawDice(dx, dy, dz, number, color) {

    let colors = {
        'red': redPhotos,
        'blue': bluePhotos,
        'black': blackPhotos
    }

    let diceString = colors[color][number];
    const dice = PIXI.Sprite.from(diceString);
    dice.x = dx;
    dice.y = dy;
    dice.zIndex = dz;
    dice.height = 75;
    dice.width = 75;
    return dice;
}

const cpButtonText = new PIXI.Text({ text: "Open CP Menu", style: new PIXI.TextStyle({ fontSize: 20, fill: '#ffffff', align: 'center' }) });
cpButtonText.x = 790;
cpButtonText.y = 45;
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

    app.stage.addChild(cpButtonText,cpButton);
    
    const cpQueryText = new PIXI.Text({ text: "Spend CP?", style: new PIXI.TextStyle({ fontSize: 30, fill: '#FFFFFF', align: 'center' }) });
    cpQueryText.x = 420;
    cpQueryText.y = 130;
    cpQuery.addChild(cpQueryText);

    // Action buttons
    const actions = [
        "Move [1]", "CHMR[2]", "Humanitarian Aid [2]", "Surge [3]", "Influence Operation [3]", "Artillery Fires [1]", "Air Strike [2]"
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

        actionButton.eventMode = 'static';
        actionButton.on("pointerdown", () => {
            actionFunctions[i]();
            closeCPQuery();
        });

        buttons.push(actionButton);
        cpQuery.addChild(actionButton);
        cpQuery.addChild(buttonText);
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
    cpButtonText.text="Open CP Menu";
}

export function openCPQuery() {
    console.log("open cp query");
    cpQuery.visible = true;
    cpButton.visible = true;
    cpButtonText.visible=true;
    cpButtonText.text = "Close CP Menu";
}