// X-coordinates for different support levels
export const supportCoords = [330, 390, 440, 490, 540, 590, 640, 695];

// Coordinates for all nodes
export const locations = {
    blueBase: { x: 122, y: 222 },
    redBase: { x: 756, y: 140 },
    city4: { x: 547, y: 66 },
    city6: { x: 255, y: 299 },
    city9: { x: 376, y: 406 },
    city10: { x: 590, y: 371 },
    village2: { x: 260, y: 116 },
    village3: { x: 408, y: 123 },
    village7: { x: 441, y: 270 },
    village8: { x: 599, y: 213 },
    haven1: { x: 220, y: 200 },
    haven2: { x: 325, y: 186 },
    haven3: { x: 369, y: 232 },
    haven4: { x: 366, y: 317 },
    haven5: { x: 518, y: 138 },
    haven6: { x: 492, y: 205 },
    haven7: { x: 554, y: 287 },
    haven8: { x: 474, y: 358 },
    haven9: { x: 648, y: 131 },
    haven10: { x: 707, y: 237 }
};

// Color mapping
export const colors = {
    blue: 0x0000FF,
    red: 0xFF0000
};

// Uppercase color names
export const upperCaseColor = {
    blue: "Blue",
    red: "Red"
};

// Define game over background colors for each winner type
export const backgroundColors = {
    blue: 0xC8E7F1,
    red: 0xFFCCCC,
    tie: 0x808080
};

// Define game over texts for each winner type
export const winnerTexts = {
    blue: "Blue Player Wins!",
    red: "Red Player Wins!",
    tie: "It's a tie!"
};

// Define game over reason texts for each reason type
export const reasonTexts = {
    vp: 'Victory Points',
    support: 'Low Support',
    disconnection: 'Player Disconnection'
};