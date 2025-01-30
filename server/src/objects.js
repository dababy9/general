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
const city = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 4
}
const village = {
    blueArmies: 0,
    redArmies: 0,
    civilians: 2
}
const haven = {
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
            civilians: 2
        },
        'redBase': {
            blueArmies: 0,
            redArmies: 16,
            civilians: 2
        },
        'city1': { ...city },
        'city2': { ...city },
        'city3': { ...city },
        'city4': { ...city },
        'village1': { ...village },
        'village2': { ...village },
        'haven1': { ...haven },
        'haven2': { ...haven },
        'haven3': { ...haven },
        'haven4': { ...haven },
        'haven5': { ...haven },
        'haven6': { ...haven },
        'haven7': { ...haven },
        'haven8': { ...haven }
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