socket.on('game-start', () => {
    sessionStorage.setItem('sessionID', sessionID);
    alert(sessionStorage.getItem('sessionID'));
    window.location.replace('/game');
});

socket.on('game-id', (id) => {
    console.log(id);
});

document.getElementById("quickplay").addEventListener('click', () => {
    socket.emit('play', 'quick');
});

document.getElementById("creategame").addEventListener('click', () => {
    socket.emit('play', 'create');
});

document.getElementById("joingame").addEventListener('click', () => {
    socket.emit('play', 'join', document.getElementById('gameid').value);
});

document.getElementById("devbutton").addEventListener('click', () => {
    socket.emit('play', 'dev');
});