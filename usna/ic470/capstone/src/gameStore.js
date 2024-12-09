// Simple interface that uses redis to manage game states

// Import redis client
const redisClient = require('./redisClient');

// Exported interface
const gameStore = {

    // Retrieve a game state from redis given the gameID 
    async findGame(gameID) {

        // Search the database forgameID 
        const game = await redisClient.hGetAll(gameID);

        // If the result is empty, return null
        if (Object.keys(game).length === 0) return null;

        // Otherwise, parse the 'connected' field to a boolean
        // We are storing the boolean as a '1' or '0' to save space
        session.connected = session.connected === "1";
        return game;
    },

    // Save or overwrite a game given the gameID and gameData
    async saveGame(gameID, gameData) {

        // Copy data from gameData to a new data object
        const data = {
            ...gameData,

            // Need to translate 'connected' field from boolean to '1' or '0'
            connected: sessionData.connected ? "1" : "0",
        };

        // Update the database
        await redisClient.hSet(gameID, data);
    },

    // Update a specific field of a game given a gameID, the field to update, and the new value to save
    async updateGameField(gameID, field, value) {

        // If updating the 'connected' field, translate boolean to '1' or '0'
        const formattedValue = field === "connected" ? (value ? "1" : "0") : value;

        // Update the database
        await redisClient.hSet(gameID, field, formattedValue);
    },

    // Delete a game given the gameID 
    async deleteGame(gameID) {

        // Delete entry from database
        await redisClient.del(gameID);
    },
};

// Set up export to be used in gameState.js
module.exports = gameStore;