/*
    This script should be loaded with each webpage to sustain a session within a browser tab.
    It must loaded AFTER the socket.io.js file.
*/



// Attempt to get sessionID from sessionStorage (local to each browser tab)
const sessionID = sessionStorage.getItem("sessionID");

// Create the socket and include sessionID (will be empty if no sessionID was found in sessionStorage)
const socket = io({
    auth: { sessionID: sessionID }
});

// This event will be fired by server upon a new session being started
// Message will include sessionID and a userID
socket.on("session", ({ sessionID, userID }) => {

    // Store the new sessionID in sessionStorage (locally in browser tab)
    sessionStorage.setItem("sessionID", sessionID);

    // Set the userID in the socket
    socket.userID = userID;
});

// This event is fired whenever an error occurs server-side regarding session status
// For example, if a user somehow sends a message to update game state before even joining a game
socket.on("statusError", (msg) => {
    window.location.replace("/");
});