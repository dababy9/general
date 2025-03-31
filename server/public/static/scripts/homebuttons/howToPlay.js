// aboutButton.js

// Function to create the About button and add the event listeners
export function createHowButton(app, x, y) {
    // Create the About button sprite
    
    const howButton = new PIXI.Graphics();
    howButton.roundRect(x,y,240, 60);
    howButton.fill(0xd9f2d0);
    howButton.stroke({width:1,color:0x000000});
    
    howButton.eventMode = 'static';
    howButton.cursor = 'pointer';

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
        <h2>To start the game</h2>
        <p>Have one player click the start game button and copy the given code. Share that 
        code with the second player. The second player should press the join game button and then paste the code into the box and press enter.</p>
        <h2>Game Rules</h2>
        <p><b>Objective:</b> Players are Red and Blue armies aiming to seize and control a region, while maintaining support of the populace. The winner is the player
            with the most victory points (VPs) at the end of 6 turns. Each city under your control (devoid of enemy units) is worth 4 VP, and each village is worth 2
            VP, and the level of one’s Support Tracker also gives VPs.</p>
            
        <p>
            <b>Turns:</b> During each turn, players roll for Initiative, conduct Actions by spending CP, conduct Close Combat, and then Reset. For every two Civilian
            casualties a player inflicts, at any time, their Support Marker moves left by 1 space. If either player lands on the “LOSE” square the other player
            immediately wins the game.<br>
        <i>Initiative:</i> Each player rolls a d6, the player that rolls higher will take their turn first. If tied, reroll.<br>
        <i>Actions:</i> Each player can spend up to 6 CPs per turn to conduct Actions (in the blue box). Resolve an Action immediately then conduct the next
        Action. Players may conduct the same Action multiple times in one turn. Track CP spent on the CP tracker.</p>
                <p><b>How to Resolve Combat:</b> For all Combat Actions (Long-Range and Close Combat), the player rolls twice. The first roll determines casualties for the
        opposing Armies, the second determines Civilian casualties. Each casualty removes one Army or subtracts 1 from the CIV. Track Civilian casualties
        inflicted with a CIV dice in each teams Civilian Casualties box. For every pair of Civilian casualties caused, the Support Marker is reduced by one.</p>

        <p><i>Support Marker:</i> After losing support, combat increases in difficulty and players will have less CP to spend each turn as denoted on the Support Tracker.</p>
        <p><i>Close Combat. </i>Once all CP are spent, if there are any opposing Armies in any one node, roll for Close Combat. In Close Combat, each player may roll up to
        one die for every Army in the contested node. Each player inflicts casualties on the opposing Armies on rolls of 5-6. Then reroll the same
        number of dice for Civilian casualties which occur on rolls of 6. The player whose turn it is rolls for Civilian causalities first, the second player
        only rolls if there are Civilians remaining in the node. During a player’s turn, only roll for Close Combat once per node.</p>
        <p><i>Reset.</i> At the end of every turn reset will happen in two parts<br>

        <i>Civilian Movement.</i> The active player rolls the d10, the node with the corresponding number will gain or civilians. To deter the change in
        population in this node, roll a d6 3 times, for every even roll add 1 and for every odd number subtract 1. Conduct this process twice.<br>

        <i>Civilians Return from Haven.</i> If a player used CHMR, roll to move the Civilians. Each Temporary Haven has numbers near each connecting node
        which denote the dice roll corresponding to Civilians moving to that node. Roll a d6 for every Civilian in the Temporary Haven, then add one to the CIV
        of the corresponding node for each dice rolled, remove the gray cubes. The Armies that escorted the civilians may be returned to any friendly adjacent
        node.</p>

        <p>Reset CP tracker back to 6. Any remaining CP do NOT carry over to the next turn. Then the next player conducts their turn.<br>

        After both player completes a turn increase the number on the die in the Turn Tracker by 1 and then reroll for initiative for the next turn. Players alternate
        turns until 6 turns have been played or one player's entire army is eliminated. After 6 turns, if there is any unresolved combat, repeat Close Combat
        until each node is controlled by one player. Both players must roll at least one d6 in this case. Then add up the VPs of all controlled nodes and VP granted
        by your position on the Support Tracker. Whoever has more VP, wins the game.<p>`;

    // Add close button
    const closeButton = document.createElement('button');
    closeButton.id = 'close-button';
    closeButton.textContent = 'X';
    // closeButton.style.marginTop = '10px';
    closeButton.style.padding = '5px 10px';
    closeButton.style.fontSize = '16px';
    closeButton.style.backgroundColor = 'red';
    closeButton.style.color = 'white';
    closeButton.style.border = 'none';
    closeButton.style.cursor = 'pointer';
    closeButton.style.display = 'block'; // Makes it take the full width of its container
    closeButton.style.margin = '10px auto'; // Centers it horizontally
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
    howButton.on('pointerdown', showInstructions);

    // Add About button to the stage
    app.stage.addChild(howButton);
}