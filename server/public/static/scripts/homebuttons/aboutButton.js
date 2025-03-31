// aboutButton.js

// Function to create the About button and add the event listeners
export function createAboutButton(app, x, y) {
    // Create the About button sprite
    const aboutButton = new PIXI.Graphics();
    aboutButton.roundRect(x,y,240, 60);
    aboutButton.fill(0xd9f2d0);
    aboutButton.stroke({width:1,color:0x000000});
    
    aboutButton.eventMode = 'static';
    aboutButton.cursor = 'pointer';

    // Create the instructions modal
    const instructionsModal = document.createElement('div');
    instructionsModal.id = 'instructions';
    instructionsModal.style.display = 'none';
    instructionsModal.style.position = 'fixed';
    instructionsModal.style.top = '0';
    instructionsModal.style.left = '0';
    instructionsModal.style.right = '0';
    instructionsModal.style.bottom = '0';
    instructionsModal.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    // instructionsModal.style.display = 'flex';
    instructionsModal.style.justifyContent = 'center';
    instructionsModal.style.alignItems = 'center';
    instructionsModal.style.zIndex = '1000';

    // Modal content
    const instructionsContent = document.createElement('div');
    instructionsContent.style.backgroundColor = 'white';
    instructionsContent.style.padding = '20px';
    instructionsContent.style.borderRadius = '10px';
    instructionsContent.style.textAlign = 'left'; // Left-align text
    instructionsContent.style.width = '40vw'; // Set width to 40% of viewport width
    instructionsContent.style.maxHeight = '80vh'; // Set max height to 80% of viewport height
    instructionsContent.style.overflowY = 'auto'; // Enable vertical scrolling
    instructionsContent.style.position = 'absolute';
    instructionsContent.style.top = '10%'; // Position near the top
    instructionsContent.style.left = '5%'; // Align to the left
    instructionsContent.innerHTML = `
        <h2>About Violent Victory</h2>
        <p>2 players, 20 - 30 minutes</p>
        <p>Violent Victory is an educational microgame designed to introduce players to the importance of civilian harm mitigation and
response (CHMR) in achieving long-term victory in an armed conflict. Limiting civilian casualties in conflict is not only the moral
and ethical imperative, but a critical element in determining success after the battle ends. Military success on the battlefield in the
absence of popular support rarely translates to strategic success. Admittedly, this game is a simplified representation of the complex
dynamics and interactions of civilians in the horrors of armed conflict. For instance, the movement of civilians to a Temporary
Haven and their subsequent movement to another node aims to reflect the civilian displacement common in conflict. The difficulty
of keeping strategic nodes civilian free demonstrates the lack of control that militaries have in keeping civilians out of their homes if
they wish to stay.

Within the game, players will have to balance expending resources on combat actions versus committing resources to increase the
safety of civilians. If players disregard the safety of civilians, they may control all the cities, however their local support will be too
low to maintain stability after the conflict. Players will have to resist the temptation to win battles at any cost.

Victory in war requires violence, but the degree of the violence determines the nature of the victory.</p>
    `;

    // Add close button
    const closeButton = document.createElement('button');
    closeButton.id = 'close-button';
    closeButton.textContent = 'X';
    closeButton.style.display = 'block'; // Makes it take the full width of its container
    closeButton.style.margin = '10px auto'; // Centers it horizontally
    closeButton.style.padding = '5px 10px';
    closeButton.style.fontSize = '16px';
    closeButton.style.backgroundColor = 'red';
    closeButton.style.color = 'white';
    closeButton.style.border = 'none';
    closeButton.style.cursor = 'pointer';
    instructionsContent.appendChild(closeButton);

    // Append content to modal
    instructionsModal.appendChild(instructionsContent);
    document.body.appendChild(instructionsModal);

    // Function to show the instructions modal
    function showInstructions() {
        instructionsModal.style.display = 'flex';
    }

    // Close button functionality
    closeButton.addEventListener('click', () => {
        instructionsModal.style.display = 'none';
    });

    // Add listener to the About button to show instructions
    aboutButton.on('pointerdown', showInstructions);

    // Add About button to the stage
    app.stage.addChild(aboutButton);
}