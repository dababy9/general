// Simple class to map sessionIDs to sessions
class SessionStore {

    // Create the map
    #sessions = new Map();

    // Find the session based on the given sessionID
    findSession(id) { return this.#sessions.get(id); }

    // Save a new session with correlated sessionID
    saveSession(id, session) { this.#sessions.set(id, session); }
}

// Set up export to be used in server.js
module.exports = { SessionStore };