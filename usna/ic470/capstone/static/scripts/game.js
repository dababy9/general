const btn = document.getElementById('btn');

btn.addEventListener('click', (e) => {
    socket.emit('fetch', 'messages');
});

socket.on('message-log', (messages) => {
    console.log(messages)
});

socket.on('game-state', (game) => {
    console.log(game);
})