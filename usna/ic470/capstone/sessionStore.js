// Simple interface that uses redis to manage sessions

// Import redis and create client
const redis = require('redis');
const redisClient = redis.createClient();

// Print any errors that occur with redis client
redisClient.on('error', (err) => {
    console.error('Redis client error:', err);
});

// Connect to Redis client
redisClient.connect().then(() => {
    console.log("Redis client connected");
}).catch((err) => {
    console.error("Error connecting to Redis client:", err);
});

// Exported interface
const sessionStore = {

    // Retrieve a session from redis given the sessionID
    async findSession(sessionID) {

        // Search the database for sessionID
        const session = await redisClient.hGetAll(sessionID);

        // If the result is empty, return null
        if (Object.keys(session).length === 0) return null;

        // Otherwise, parse the 'connected' field to a boolean
        // We are storing the boolean as a '1' or '0' to save space
        session.connected = session.connected === "1";
        return session;
    },

    // Save or overwrite a session given the sessionID and sessionData
    async saveSession(sessionID, sessionData) {

        // Copy data from sessionData to a new data object
        const data = {
            ...sessionData,

            // Need to translate 'connected' field from boolean to '1' or '0'
            connected: sessionData.connected ? "1" : "0",
        };

        // Update the database
        await redisClient.hSet(sessionID, data);
    },

    // Update a specific field of a session given a sessionID, the field to update, and the new value to save
    async updateSessionField(sessionID, field, value) {

        // If updating the 'connected' field, translate boolean to '1' or '0'
        const formattedValue = field === "connected" ? (value ? "1" : "0") : value;

        // Update the database
        await redisClient.hSet(sessionID, field, formattedValue);
    },

    // Delete a session given the sessionID
    async deleteSession(sessionID) {

        // Delete entry from database
        await redisClient.del(sessionID);
    },
};

// Set up export to be used in server.js
module.exports = sessionStore;