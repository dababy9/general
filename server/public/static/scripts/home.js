// Create PIXI canvas and add to page
const app = new PIXI.Application();
await app.init({ width: 1000, height: 650, background: '#DFE8F3' });
document.body.appendChild(app.canvas);

// Position the canvas correctly
Object.assign(app.canvas.style, { position: 'absolute', left: '0', top: '0' });

// Load image
await PIXI.Assets.load('content/backgroundVV.png');

// Create background image
const background = Object.assign(PIXI.Sprite.from('content/backgroundVV.png'), { x: 0, y: 0, width: 1000, height: 650 });

// Add background to canvas
app.stage.addChild(background);

// Retrieve necessary HTML elements
const mainBanner = document.getElementById('main-banner');
const joinField = document.getElementById('join-field');
const waitingBanner = document.getElementById('waiting-banner');
const instructions = document.getElementById('instructions');
const closeInstructions = document.getElementById('close-instructions');
const about = document.getElementById('about');
const closeAbout = document.getElementById('close-about');

// Event listener for join game input field
joinField.addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
        socket.emit('play', 'join', joinField.value);
        joinField.style.display = 'none';
    }
});

// Event listener for close instructions button
closeInstructions.addEventListener('click', () => {
    instructions.style.display = 'none';
});

// Event listener for close about button
closeAbout.addEventListener('click', () => {
    about.style.display = 'none';
});

// Function to return a common Pixi Text Style of a given font size
function pixiText (fontSize) {
    return new PIXI.TextStyle({
        fontFamily: 'Lexend',
        fontSize,
        fill: '#000000',
        align: 'center'
    });
}

// Define function to create and return button with text that calls given function when clicked
let buttonY = 110;
function makeButton (callback, buttonText, textOffset) {
    const y = buttonY += 70;
    const reset = (x, color) => { x.clear().roundRect(380, y, 240, 60).fill(0xd9f2d0).stroke({ width: 2, color }) };
    const button = Object.assign(new PIXI.Graphics(), { eventMode: 'static', cursor: 'pointer' })
        .roundRect(380, y, 240, 60)
        .fill(0xd9f2d0)
        .stroke({ width: 2, color: 0x000000 })
        .on('pointerdown', function () { reset(button, 0x000000); callback(); })
        .on('pointerover', () => reset(button, 0xf1e3a4))
        .on('pointerout', () => reset(button, 0x000000));

    const text = Object.assign(new PIXI.Text({ text: buttonText, style: pixiText(30), x: 500 - textOffset, y: buttonY + 15 }));
    return [button, text];
}

// Array to hold main menu items
const mainMenu = []

// Create Button
mainMenu.push(...makeButton(() => {
    socket.emit('play', 'create');
    startWaitAnimation();
}, "Create Game", 79));

// Join Button
mainMenu.push(...makeButton(() => {
    joinField.style.display = 'inline';
    setTimeout(() => joinField.focus(), 0);
    mainBanner.textContent = 'Have a code? Type it in and hit enter to join!';
    mainBanner.style.display = 'block';
    app.stage.removeChild(...mainMenu);
    app.stage.addChild(cancelButton, cancelText);
}, "Join Game", 65));

// Quick Play Button
mainMenu.push(...makeButton(() => {
    socket.emit('play', 'dev');
    startWaitAnimation();
}, "Quick Play", 68));

// How To Button
mainMenu.push(...makeButton(() => {
    instructions.style.display = 'flex';
}, "How To Play", 80));

// About Button
mainMenu.push(...makeButton(() => {
    about.style.display = 'flex';
}, "About", 38));

// Add all main menu items to canvas initially
app.stage.addChild(...mainMenu);

// Create title
const titleText = Object.assign(new PIXI.Text({ text: "Violent Victory", style: pixiText(60) }), { x: 314, y: 40 });

// Create main credit
const mainCreditText = Object.assign(new PIXI.Text({ text: "by Rachel McVicker and Sebastian J. Bae", style: pixiText(30) }), { x: 249, y: 120 });

// Create secondary credit
const secondaryCreditText = Object.assign(new PIXI.Text({ text: "Digitized By: Joshua Clark, Lauren Leckelt, Caleb Walker", style: pixiText(20) }), { x: 265, y: 580 });

// Add all texts to the canvas
app.stage.addChild(titleText, mainCreditText, secondaryCreditText);

// Create cancel button
const cancelButton = Object.assign(new PIXI.Graphics(), { eventMode: 'static', cursor: 'pointer' })
    .roundRect(440, 400, 120, 40)
    .fill(0xe36e66)
    .stroke({ width: 2, color: 0x000000 })
    .on('pointerdown', () => {
        socket.emit('leave');
        app.stage.addChild(...mainMenu);
        app.stage.removeChild(cancelButton, cancelText, waitingDots);
        waitingBanner.style.display = mainBanner.style.display = joinField.style.display = 'none';
    });

// Create cancel text
const cancelText = Object.assign(new PIXI.Text({ text: 'Cancel', style: pixiText(20), x: 472, y: 410 }));

// Create waiting dots
const waitingDots = Object.assign(new PIXI.Container(), { x: 500, y: 310 });
for (let i = 0; i < 8; i++) {
    const currentAngle = Math.PI / 4 * i;
    const dot = new PIXI.Graphics()
        .circle(40 * Math.cos(currentAngle), 40 * Math.sin(currentAngle), 8)
        .fill([0, 0, 0, 1 - 0.15*i]);
    waitingDots.addChild(dot);
}

// Function to switch to the 'waiting' animation
function startWaitAnimation () {
    app.stage.removeChild(...mainMenu);
    app.stage.addChild(cancelButton, cancelText, waitingDots);
    waitingDots.rotation = 0;
    waitingBanner.style.display = 'block';
}

// Circular animation for waiting dots
app.ticker.add((time) => {
    waitingDots.rotation -= 0.1 * time.deltaTime;
    if (waitingDots.rotation < -4) waitingDots.rotation += Math.PI * 2;
});

// Socket event handler to display game ID in a small box for the user
socket.on('game-id', (gameID) => {
    mainBanner.textContent = 'Game ID: ' + gameID + '. Share this code to start!';
    mainBanner.style.display = 'block';
});

// const startInst = new PIXI.Text({ text: "", style: new PIXI.TextStyle({ fontFamily: 'Legend', fontSize: 25, fill: 0x000000, align: 'left' }) });
// startInst.x = 635;
// startInst.y = 180;
// startInst.text = 'To start a game, click Create Game.\nGive the code that appears to your \nopponent. Your opponent then\ntypes the code into the text box and\npresses enter and the game starts.';
// app.stage.addChild(startInst);