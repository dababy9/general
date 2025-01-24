// Import ioredis
const Redis = require('ioredis');

// Define interface for interacting with database
class RedisInterface {

    // Constructor can take host and port to create Redis client
    // Will default to 'localhost' and 6379 if host and port aren't provided
    constructor(redisHost = 'localhost', redisPort = 6379) {
        this.client = new Redis({
            host: redisHost,
            port: redisPort
        });
    }

    // Store or overwrite an object given an ID
    async set(id, data) {

        // Try to map id to object in database, return true if successful
        try {
            await this.client.set(id, JSON.stringify(data));
            return true;

        // If an error was caught, print it out and return false
        } catch (err) {
            console.error(err);
            return false;
        }
    }

    // Push a value to the end of a list given an ID
    async append(id, value) {

        // Try to append value to list in database, return true if successful
        try {
            await this.client.rpush(id, value);
            return true;

        // If an error was caught, print it out and return false
        } catch (err) {
            console.error(err);
            return false;
        }
    }

    // Get the object given an ID
    async get(id) {

        // Try to retrieve the object from the database, return it if successful
        try {

            // Attempt to retrieve json string from database
            const jsonString = await this.client.get(id);

            // If the entry existed, return parsed object, otherwise return null
            return jsonString ? JSON.parse(jsonString) : null;

        // If an error was caught, print it out and return null
        } catch (err) {
            console.error(err);
            return null;
        }
    }

    // Remove and return the element at the start of a list given an ID
    async poll(id) {

        // Try to return the first item in the list
        try {
            return await this.client.lpop(id);

        // If an error was caught, print it out and return null
        } catch (err) {
            console.error(err);
            return null;
        }
    }

    // Retrieves an entire list given an ID
    async getList(id) {

        // Try to return the list
        try {
            return await this.client.lrange(id, 0, -1);

        // If an error was caught, print it out and return null
        } catch (err) {
            console.error(err);
            return null;
        }
    }

    // Update a specific field of an object given an id
    async updateField(id, field, value) {

        // Try to update field in database, return true if successful
        try {
            
            // Attempt to retrieve object using 'get' method
            const obj = await this.get(id);

            // If the object is null, throw an error
            if (!obj)
                throw new Error();

            // Update the given field with the given value
            obj[field] = value;

            // Update entry in database using 'set' method
            return await this.set(id, obj);

        // If an error was caught, return false
        } catch (err) {
            return false;
        }
    }

    // Delete an object given an ID
    async del(id) {

        // Try to delete object from database, return true if successful
        try {
            await this.client.del(id);
            return true;

        // If an error was caught, print it out and return false
        } catch (err) {
            console.error(err);
            return false;
        }
    }

    // Close connection to redis database
    close() {
        this.client.disconnect();
    }
}

// Set up export to be used in server.js and gameState.js
module.exports = RedisInterface;