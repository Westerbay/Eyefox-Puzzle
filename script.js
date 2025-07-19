var sprite = document.getElementById("sprite");
var indexSprite = 0;

function nextSprite() {
    indexSprite = (indexSprite + 1) % 5;
    sprite.src = `images/awake${indexSprite + 1}.png`;
}

setInterval(nextSprite, 160);