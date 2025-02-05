// Initial session
const initialSession = {
    stat: 'base',
    connected: true,
    gameID: ''
}

// Player definition (only used in initialState)
const player = {
    cp: 6,
    support: 7,
    surgeArmies: 8,
    casualties: 0
}

// Node definitions (only used in initialState)
const tile = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 0
}

// Initial game state
const initialState = {
    bluePlayer: { ...player },
    redPlayer: { ...player },
    turnPlayer: '',
    turnCounter: 1,
    nodes: {
        'blueBase': {
            blueArmies: 16,
            redArmies: 0,
            civilians: 0
        },
        'redBase': {
            blueArmies: 0,
            redArmies: 16,
            civilians: 0
        },
        'city1': { ...tile },
        'city2': { ...tile },
        'city3': { ...tile },
        'city4': { ...tile },
        'village1': { ...tile },
        'village2': { ...tile },
        'village3': { ...tile },
        'village4': { ...tile },
        'haven1': { ...tile },
        'haven2': { ...tile },
        'haven3': { ...tile },
        'haven4': { ...tile },
        'haven5': { ...tile },
        'haven6': { ...tile },
        'haven7': { ...tile },
        'haven8': { ...tile },
        'haven9': { ...tile },
        'haven10': { ...tile }
    }
}

// Graph of the game map
const gameMap = {

}

module.exports = {
    initialSession,
    initialState,
    gameMap
}