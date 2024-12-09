// Import ioredis
const Redis = require('ioredis');

// Define interface for interacting with database
class RedisInterface {

    // In constructor, just create new redis client
    constructor() {
        this.client = new Redis();
    }

    // Store or overwrite an object given an ID
    async set(id, data) {

        // If the data isn't an object, throw an error
        if (typeof data !== 'object')
            throw new Error("Data must be of type 'object'");

        // Otherwise, stringify it and store in database
        return await this.client.set(id, JSON.stringify(data));
    }

    // Get the object given an ID
    async get(id) {

        // Attempt to retrieve json string from database
        const jsonString = await this.client.get(id);

        // If entry existed, return parsed object, otherswise return null
        return jsonString ? JSON.parse(jsonString) : null;
    }

    // Update a specific field of an object given an id
    async updateField(id, field, value) {

        // Retrieve object using 'get' method
        const obj = await this.get(id);

        // If the object is null, throw an error
        if (!obj)
            throw new Error("Could not find entry with ID: " + id);

        // Update the given field with the given value
        obj[field] = value;

        // Update entry in database using 'set' method
        return await this.set(id, obj);
    }

    // Delete an object given an ID
    async del(id) {
        return await this.client.del(id);
    }

    // Close connection to redis database
    close() {
        this.client.disconnect();
    }
}

// Set up export to be used in server.js and gameState.js
module.exports = RedisInterface;