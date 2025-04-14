
export function makeTextStyle(font, size, fill, align){
    const style = new PIXI.TextStyle({
        fontFamily: font,
        fontSize: size,
        fill: fill,
        align: align
    });
    return style;
}

