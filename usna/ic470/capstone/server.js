const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer();

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});

server.listen(9000, () => {
    console.log('listening on port 9000');
});