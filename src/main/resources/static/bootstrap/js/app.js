import * as product from './productApp.js';

$(window).on('load', function(e) {
		alert('load  event:' + e);
});
product.init();
