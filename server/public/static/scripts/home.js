const quickPlayBtn = document.getElementById('quick-play');

quickPlayBtn.addEventListener('click', (e) => {
    socket.emit('quick-play');
});

socket.on('game-start', () => {
    window.location.replace('/game');
});