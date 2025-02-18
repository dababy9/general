/*
    This script should be loaded with each webpage to sustain a session within a browser tab.
    It must loaded AFTER the socket.io.js file.
*/



// Attempt to get sessionID from sessionStorage (local to each browser tab)
sessionID = sessionStorage.getItem('sessionID');

// Create the socket and include sessionID (will be empty if no sessionID was found in sessionStorage)
const socket = io({
    auth: { sessionID: sessionID }
});

// This event will be fired by server upon a new session being started
// Message will include sessionID and the status of the session (used for redirection)
socket.on('session', ({id, stat}) => {

    // Save the included sessionID
    sessionID = id;
});

// This event is fired when the server puts the client into a game.
socket.on('game-start', () => {

    // First, save the sessionID in the browser tab's sessionStorage to maintain the session
    sessionStorage.setItem('sessionID', sessionID);

    // Then, redirect to the game board page
    window.location.replace('/game');
});