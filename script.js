var awake = document.getElementById("awake");
var indexAwake = 0;

var sleeping = document.getElementById("sleeping");
var indexSleeping = 0;

var stand = document.getElementById("stand");
var indexStand = 0;

function nextSprite() {
    indexAwake = (indexAwake + 1) % 5;
    awake.src = `images/awake${indexAwake + 1}.png`;

    indexSleeping = (indexSleeping + 1) % 6;
    sleeping.src = `images/sleeping${indexSleeping + 1}.png`;

    indexStand = (indexStand + 1) % 5;
    stand.src = `images/stand${indexStand + 1}.png`;
}

setInterval(nextSprite, 160);