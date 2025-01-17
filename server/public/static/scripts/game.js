const btn = document.getElementById('btn');
const input = document.getElementById('input');
const submit = document.getElementById('message');

btn.addEventListener('click', (e) => {
    socket.emit('fetch', 'messages');
});

submit.addEventListener('click', (e) => {
    socket.emit('send-message', input.value);
});

socket.on('message-log', (messages) => {
    console.log(messages);
});

socket.on('game-state', (game) => {
    console.log(game);
});

socket.on('new-message', (message) => {
    console.log(message.from);
    console.log(message.data);
});