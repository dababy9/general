socket.on('message-log', (messages) => {
    console.log(messages);
});

socket.on('game-state', (game) => {
    console.log(game);
});

socket.on('new-message', (message) => {
    message = JSON.parse(message);
    console.log(message.from + ": " + message.data);
});

socket.on('move-lists', (data) => {
    console.log('MOVE LISTS');
    console.log(data);
});

socket.on('move', (data) => {
    console.log('MOVE SUCCESS');
    console.log(data);
});

socket.on('humanitarianAid', (data) => {
    console.log('humAID');
    console.log(data);
});

document.getElementById('fetchmessages').addEventListener('click', () => {
    socket.emit('game', 'fetch-message-log');
});

document.getElementById('fetchgamestate').addEventListener('click', () => {
    socket.emit('game', 'fetch-game-state');
});

document.getElementById('sendmessage').addEventListener('click', () => {
    socket.emit('game', 'send-message', document.getElementById('message').value);
});

document.getElementById('moveselect').addEventListener('click', () => {
    socket.emit('game', 'action', {type: 'move-select', data: [document.getElementById('move1').value, document.getElementById('move2').value]})
});

document.getElementById('moveconfirm').addEventListener('click', () => {
    socket.emit('game', 'action', {type: 'move-confirm', data: [document.getElementById('move1').value, document.getElementById('move2').value]})
});

document.getElementById('humaid').addEventListener('click', () => {
    socket.emit('game', 'action', { type: 'humanitarian-aid'});
});

document.getElementById('initiative').addEventListener('click', () => {
    socket.emit('game', 'initiative');
});

document.getElementById('endturn').addEventListener('click', () => {
    socket.emit('game', 'end-turn');
});