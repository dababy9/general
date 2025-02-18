// Initialize in-memory 'storages'
const sessionStore = new Map();
const gameStore = new Map();
const quickPlayQueue = new Array();
const privateGameTable = new Map();

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
        turnCounter: 0,
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

// Graph representing connections directly between nodes
const nodeMap = {
    'blueBase': ['village2', 'city6'],
    'redBase': ['village8', 'city4', 'city10'],
    'city4': ['redBase', 'village3', 'village8'],
    'city6': ['blueBase', 'village2', 'village3', 'village7', 'city9'],
    'city9': ['village7', 'city6', 'city10'],
    'city10': ['redBase', 'village7', 'village8', 'city9'],
    'village2': ['blueBase', 'village3', 'city6'],
    'village3': ['village2', 'village7', 'village8', 'city4', 'city6'],
    'village7': ['village3', 'village8', 'city6', 'city9', 'city10'],
    'village8': ['redBase', 'village3', 'village7', 'city4', 'city10']
}

// Graph representing connections between nodes and havens
const fullMap = {
    'blueBase': ['haven1'],
    'redBase': ['haven9', 'haven10'],
    'city4': ['haven5', 'haven9'],
    'city6': ['haven1', 'haven2', 'haven3', 'haven4'],
    'city9': ['haven4', 'haven8'],
    'city10': ['haven7', 'haven8', 'haven10'],
    'village2': ['haven1', 'haven2'],
    'village3': ['haven2', 'haven3', 'haven5', 'haven6'],
    'village7': ['haven3', 'haven4', 'haven6', 'haven7', 'haven8'],
    'village8': ['haven5', 'haven6', 'haven7', 'haven9', 'haven10'],
    'haven1': ['blueBase', 'city6', 'village2'],
    'haven2': ['village3', 'city6', 'village2'],
    'haven3': ['village3', 'city6', 'village7'],
    'haven4': ['city9', 'city6', 'village7'],
    'haven5': ['village3', 'village8', 'city4'],
    'haven6': ['village3', 'village8', 'village7'],
    'haven7': ['city10', 'village8', 'village7'],
    'haven8': ['city9', 'city10', 'village7'],
    'haven9': ['redBase', 'village8', 'city4'],
    'haven10': ['redBase', 'village8', 'city10']
}

// Set up export to be used in server.js and handlers.js
module.exports = {
    sessionStore,
    gameStore,
    quickPlayQueue,
    privateGameTable,
    createInitialSession,
    createInitialState,
    nodeMap,
    fullMap
}