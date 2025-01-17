/*
    This script should be loaded with each webpage to sustain a session within a browser tab.
    It must loaded AFTER the socket.io.js file.
*/



// Attempt to get sessionID from sessionStorage (local to each browser tab)
const sessionID = sessionStorage.getItem('sessionID');

// Create the socket and include sessionID (will be empty if no sessionID was found in sessionStorage)
const socket = io({
    auth: { sessionID: sessionID }
});

// This event will be fired by server upon a new session being started
// Message will include sessionID
socket.on('session', (sessionID) => {

    // Store the new sessionID in sessionStorage (locally in browser tab)
    sessionStorage.setItem('sessionID', sessionID);
});

// This event is fired whenever an error occurs server-side regarding session status
// For example, if a user somehow sends a message to update game state before joining a game
socket.on('status-error', () => {
    window.location.replace('/status-error');
});

// This event is fired whenever an error occurs server-side regarding an unrecognized or malformed client message
// For example, if a client sends the server a 'fetch' message with no resource specified
socket.on('message-error', () => {
    window.location.replace('/message-error')
});