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

4. (Optional) If you would like to change the port mapping for the container, simply alter the following code in the `docker-compose.yml` file:
```
    ports:
        - "9000:9000"
```

#### Without Docker

1. Ensure that you have access to a Redis database.
    - The program assumes a local Redis server running at `localhost` on port `6379`.
    - If you don't know how to start a local Redis server, [this](https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/) might be helpful.
    - If you intend on using an external database or any other host/port in general, you can edit the `src/RedisInterface.js` file to change the top two variables, `redisHost` and `redisPort`, to your liking.

2. Ensure that you have `node` and `npm` installed on your machine.
    - Running `node -v` should print something like `vXX.XX.X` to the terminal. If not, [install node](https://nodejs.org/en/download/package-manager).
    - Installing `node` should include `npm` by default. You can check that `npm` is also installed by running `npm -v` - it should print out something like `X.XX.XX`.

3. Navigate to the server's root directory. This should be the directory that contains the `package.json` file.

4. Start the server by running `npm start`. This should start the server, and it should indicate that it is listening on a specific port (`9000` by default).

5. (Optional) If you would like to change the port mapping for the container, change the `PORT` variable at the top of `src/server.js`.

## Server Architecture

Placeholder

## API

Placeholder