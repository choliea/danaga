/**
 * view.js
 */

function render(templateId="#guest-main-template",jsonResult={},contentId="#content"){
	addCustomFunctionHandlebars();
	let templateHtml = document.querySelector(templateId).innerHTML;
	let bindTemplate = Handlebars.compile(templateHtml);
	let resultTemplate = bindTemplate(jsonResult);
	document.querySelector(contentId).innerHTML=resultTemplate;
}

function addCustomFunctionHandlebars(){
	/*****Handlebars 함수등록 */
	Handlebars.registerHelper('substring',function(str,start,end){
		return str.substring(start,end);
	});
	Handlebars.registerHelper('toUpper',function(str){
		return str.toUpperCase();
	});
	
}


export {render}
