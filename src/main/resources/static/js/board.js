let hash = window.location.hash
let path = hash.substring(1);
let html = '';

function init() {
	registEvent();
	navigate();
}

