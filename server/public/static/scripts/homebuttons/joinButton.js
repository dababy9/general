

export function createJoinButton(app, x, y) {

    //Create the join button

    const joinbutton = new PIXI.Graphics();
    joinbutton.roundRect(x,y,240, 60);
    joinbutton.fill(0xd9f2d0);
    joinbutton.stroke({width:1,color:0x000000});
    joinbutton.eventMode = 'static';
    joinbutton.cursor = 'pointer';

    joinbutton.eventMode = 'static';
    joinbutton.cursor = 'pointer';
    //listener that shows the input text box when necessary
    joinbutton.on('pointerdown', showInputField);
    app.stage.addChild(joinbutton);

    //creates a textbox for the player to enter with the joinbutton. 
    const inputField = document.createElement('input');
    inputField.id = 'text-box';
    inputField.placeholder = 'Enter a value';
    inputField.style.position = 'absolute';
    inputField.style.top = '240px';
    inputField.style.left = '90px';
    inputField.style.padding = '8px';
    inputField.style.fontSize = '18px';
    inputField.style.display = 'none';
    document.body.appendChild(inputField);

    // Listener for when the user presses Enter in the textbox
inputField.addEventListener('keypress', function (event) {
    if (event.key === 'Enter') {
        const inputValue = inputField.value; // Get value from the input
        joinPS(inputValue); // Call the listener function
        inputField.value = ''; // Clear the input after use (optional)
        inputField.style.display = 'none'; // Hide input field after Enter press
    }
});

}


//listener for join button. Shows the textbox
function showInputField() {
    document.getElementById('text-box').style.display = 'inline'; // Make input field visible
    inputField.focus();
}



function joinPS(gameID) { //needs a parameter with the message. 
    console.log(gameID);
    socket.emit('play', 'join', gameID);
}