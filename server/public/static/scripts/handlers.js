
export function getRedPhotos(){
    const redPhotos = {
    '1': '/content/red_one_die.png',
    '2': '/content/red_two_die.png',
    '3': '/content/red_three_die.png',
    '4': '/content/red_four_die.png',
    '5': '/content/red_five_die.png',
    '6': '/content/red_six_die.png'
}
return redPhotos;
}

export function getBluePhotos(){
    const bluePhotos = {
    '1': '/content/blue_one_die.png',
    '2': '/content/blue_two_die.png',
    '3': '/content/blue_three_die.png',
    '4': '/content/blue_four_die.png',
    '5': '/content/blue_five_die.png',
    '6': '/content/blue_six_die.png'
}
return bluePhotos;
}

export function getBlackPhotos() {
    const blackPhotos = {
    '1': '/content/black_one_die.png',
    '2': '/content/black_two_die.png',
    '3': '/content/black_three_die.png',
    '4': '/content/black_four_die.png',
    '5': '/content/black_five_die.png',
    '6': '/content/black_six_die.png'
}
return blackPhotos;
}

let redPhotos = getRedPhotos();
let bluePhotos = getBluePhotos();
let blackPhotos = getBlackPhotos();

export async function loadAssets(){
    //all of the photos that are used in the game 
await PIXI.Assets.load(['/content/backgroundVV.png', //background blue picture
    '/content/Board_Circles.png', //board picture with the nodes
    '/content/button.png', //menu button in top left of screen
    '/content/blueGrave.png',//blue grave
    '/content/redGrave.png',//red grave
    '/content/redCPTracker.png', //tracker for the red command points
    '/content/blueCPTracker.png', //tracker for the blue command points
    '/content/IFV_Blue.png', //Blue tank
    '/content/IFV_Red.png',  //red tank
    '/content/blueSurgeNew.png', //blue surge photo
    '/content/redSurgeNew.png', //red surge photo
    '/content/civilians2.png', //civilians photo
    '/content/VVInstructions.jpg', //powerpoint instructions that appear from menu button.
    redPhotos['1'],//dice photos being loaded
    redPhotos['2'],
    redPhotos['3'],
    redPhotos['4'],
    redPhotos['5'],
    redPhotos['6'],
    bluePhotos['1'],
    bluePhotos['2'],
    bluePhotos['3'],
    bluePhotos['4'],
    bluePhotos['5'],
    bluePhotos['6'],
    blackPhotos['1'],
    blackPhotos['2'],
    blackPhotos['3'],
    blackPhotos['4'],
    blackPhotos['5'],
    blackPhotos['6'],
    '/content/support_tracker.png']); //support tracker (bottom of screen)

}