// Initialize in-memory 'storages'
const sessionStore = new Map();
const gameStore = new Map();
const quickPlayQueue = new Array();

// Initial session
const createInitialSession = () => {
    return {
        stat: 'base',
        connected: true,
        gameID: '',
        color: ''
    }
}

// Function to return a node on the game map (only used in creation of the initial game state)
const createTile = (civ, blue = 0, red = 0) => {
    return {
        civilians: civ,
        blueArmies: blue,
        redArmies: red
    }
}

// Returns a number 1-6 (only used in creation of the initial game state)
const d6 = () => { return Math.floor(Math.random()*6)+1; }

// Initial game state
const createInitialState = () => {

    const player = {
        cp: 6,
        support: 7,
        surgeArmies: 8,
        casualties: 0
    }

    const gameState = {
        bluePlayer: { ...player },
        redPlayer: { ...player },
        turnPlayer: '',
        turnCounter: 1,
        nodes: {
            'blueBase': createTile(d6(), 16, 0),
            'redBase': createTile(d6(), 0, 16),
            'city4': createTile(d6()+d6()),
            'city6': createTile(d6()+d6()),
            'city9': createTile(d6()+d6()),
            'city10': createTile(d6()+d6()),
            'village2': createTile(d6()),
            'village3': createTile(d6()),
            'village7': createTile(d6()),
            'village8': createTile(d6()),
            'haven1': createTile(0),
            'haven2': createTile(0),
            'haven3': createTile(0),
            'haven4': createTile(0),
            'haven5': createTile(0),
            'haven6': createTile(0),
            'haven7': createTile(0),
            'haven8': createTile(0),
            'haven9': createTile(0),
            'haven10': createTile(0)
        }
    };

    return gameState;
}

// Graph of the game map
const gameMap = {
    
}

module.exports = {
    sessionStore,
    gameStore,
    quickPlayQueue,
    createInitialSession,
    createInitialState,
    gameMap
}