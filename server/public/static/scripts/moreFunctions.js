// Function to take in four key style components and return a PIXI TextStyle
export function makeTextStyle(fontFamily, fontSize, fill, align){
    return new PIXI.TextStyle({ fontFamily, fontSize, fill, align });
}