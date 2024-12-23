# Overview

This is the developer documentation, intended for those attempting to host their own verion of Violent Victory. If you are simply looking for documentation on how to play the game, you can check that out [here](../User).

This documentation provides the following:

- A [brief tutorial](#starting-the-server) on how to start the server. Includes a section on using Docker and `docker-compose`, as well as a section on how to run without Docker.
- A [breakdown](#server-architecture) of the architecture behind the server.
- A [detailed explanation](#api) on the API that server uses to communicate with the client.

## Starting the server

The server can either be run in a Docker container(s), or can just be run with `node`/`npm`. Please note that if you plan to run the server without Docker, you will need to have access to a Redis database.

#### Using Docker

1. Ensure that you have `docker` and `docker-compose` installed on your machine.
    - Running `docker -v` should print something like `Docker version ...` to the terminal. If not, [install docker](https://docs.docker.com/engine/install/).
    - Furthermore, running `docker-compose -v` should print something like `Docker Compose version ...` to the terminal. If not, [install docker-compose](https://docs.docker.com/compose/install/).

2. Navigate to the server's root directory. This should be the directory that contains the `Dockerfile`.

3. Start the server by running `docker-compose up`. This should start the server, and it should indicate that it is listening on a specific port (`9000` by default).

4. (Optional) If you would like to change any of the internal ports and URLs, or change the external port mapping for the docker container, you can modify the variables in the `docker-compose.yml` file.

#### Without Docker

1. Ensure that you have access to a Redis database.
    - The program assumes a local Redis server running at `localhost` on port `6379`.
    - If you don't know how to start a local Redis server, [this](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/) might be helpful.
    - If you intend on using an external database or any other redis host/port in general, you can edit the `.env` file to change the `REDIS_HOST`/`REDIS_PORT` variables as necessary.

2. Ensure that you have `node` and `npm` installed on your machine.
    - Running `node -v` should print something like `vXX.XX.X` to the terminal. If not, [install node](https://nodejs.org/en/download/package-manager).
    - Installing `node` should include `npm` by default. You can check that `npm` is also installed by running `npm -v` - it should print out something like `X.XX.XX`.

3. Navigate to the server's root directory. This should be the directory that contains the `package.json` file.

4. Start the server by running `npm start`. This should start the server, and it should indicate that it is listening on a specific port (`9000` by default).

5. (Optional) If you would like to change the port number that the server binds to (default is `9000`), you can edit the `.env` file to change the `PORT` variable as necessary.

## Server Architecture

The back-end is written in [node.js](https://nodejs.org/en). The server itself is an [express](https://expressjs.com/) app serves html pages to the different endpoints and also serves any static files (images, client-side scripts, etc.) There is also a [socket.io](https://socket.io/) server mounted on top of the app that handles sessions as well as the dynamic game updates and messaging.

#### `server.js`

This is the main driver, and is the script that handles all the connections and messages. It imports all of the other scripts in the `/src` directory, with the intent of making the program more modular.

This script is where the `express` app is fully defined. All of the endpoints are defined and serve a specific html page. Additionally, the app is configured to serve all static files relative to the `/static` directory. The app is also passed to an `http` server that listens on port `9000` (by default). The app handles all regular `http` requests, but has no concept of sessions/session state.

The other main section of this script is the `socket.io` server. `io` is the variable that represents the `socket.io` server, which mounts on top of the `express` app. There are only two definitions for the `socket.io` server: `io.use()` and `io.on('connection')`. The `io.use()` definition is called middleware, and is executed right before any `socket.io` connection. The middleware function for this server just handles session retrieval/generation. The `io.on('connection')` definition contains initial code followed by many `socket.on()` definitions that handle various client-side messages. The initial code stores the session given by the middleware in the database, among other things. Then, each client-side message is passed off to the corresponding function from the `handler.js` script.

Finally, the end of the script just handles various shutdown signals to have the app close out database connections and exit gracefully.

#### `handlers.js`

This script is just a module that provides a minimalistic interface for each `socket.io` client-side message. It is a bunch of asynchronous functions that take any necessary objects/data and process the message that they handle. For example, the bottom function `handleDisconnect` only requires `socket` (which holds the `sessionID`) and `sessionStore`, which is another interface intended for storing and retrieving sessions/session data. It is directly called when the client sends the `disconnect` message.

#### `redisInterface.js`

This script provides an interface containing a couple of useful methods for interacting with the database. All of the redis operations are atomic, meaning there are no concerns for race conditions (even though the functions are asynchronous).

#### `gameState.js`

This script contains all of the game logic, and is the middle layer that validates/processes any incoming moves before making changes to the game state stored in the database. Most of the exported methods are used at some point in the handlers that process client-side methods: for example, the `newMessage` method is called from within the `handleMessage` function in `handler.js`.

## API

#### `auth` from client (for sessions)

The `auth` object is an option that the client can send to the server with the initial connection request. In this case, we attach a single variable to the object called `sessionID`.

Upon the first connection, the client will not have a `sessionID` to send, so the variable will be undefined. The server will then generate a new `sessionID` for the client and send it.

The client will receive the `sessionID` and store it in the browser's `sessionStorage`, which is a non-persistent browser storage local to the current tab. Thus, the `sessionID` is saved within the tab across multiple pages (but it is deleted when the tab is closed).

Then, on every new page load (and thus a new `socket.io` connection), the client will attach the `sessionID` from the `sessionStorage` to the `auth` object and send it to the server. This is how the client can maintain a persistent `socket.io` session over multiple pages.

#### `socket.io` messages from client

| Message | Description |
| :-----: | ----------- |