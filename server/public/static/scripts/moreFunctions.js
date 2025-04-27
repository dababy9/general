// Function to take in four key style components and return a PIXI TextStyle
export function makeTextStyle(fontSize, fill, align){
    return new PIXI.TextStyle({ fontSize, fill, align });
}