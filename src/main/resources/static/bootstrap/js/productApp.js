import * as View from "./view.js";
import * as api from "./p_apiService.js"


let hash = window.location.hash
let path = hash.substring(1);
let html = '';
/*
초기실행메쏘드
*/
function init() {
	registEvent();
	navigate();
	//common_header_user_cart();
}

function registEvent() {
	
	$(window).on('hashchange', function(e) {
		alert('hashchange event:' + e);
		hash = window.location.hash
		path = hash.substring(1);
		navigate();
	});
	$(document).on('click', function(e) {
		console.log(e.target);
		if ($(e.target).attr('categoryId')) {
			let categoryId= e.target.categoryId.value;
			console.log(categoryId);
			api.subCategory(categoryId);
		}
	});
 	

}





//1.하트 눌렀을때 위시리스트 추가하는 이벤트 
$('.btn btn-outline-secondary btn-sm btn-wishlist').on("click",function(e){
	let optionSetId = e.target.data-no.value;
	api.tapHeart(optionSetId);
});
//$('.btn btn-outline-secondary btn-sm btn-wishlist active').click(api.untapHeart($(this[data-no])));

//2. sort by  눌렀을 때 정렬 바뀌게 select 다시하는 쿼리
$('.form-control > #sorting').click(function() {
    var selectedValue = $('.form-control > #sorting option:selected').val();
    var jsonData = { "orderType": selectedValue };
    api.searchResult(jsonData);
});


//3. 필터 카테고리, 옵션 쿼리 
$('.list-group-item category').on("click",function(e){
let categoryId= e.target.categoryId.value;
api.subCategory(categoryId);
});
$('.sub-category-item').click(function(e){
	let categoryId= e.target.data-subcategory-id.value;
	api.showOptions(categoryId);
	});
//4. x 눌렀을때 관심상품, 최근 상품 삭제하는 쿼리 
$('.remove-from-wish').click(function(e){
	let optionSetId=e.target.href.substring(1);
	api.removewish(optionSetId);
});
$('.remove-from-recent').click(function(e){
	let optionSetId=e.target.href.substring(1);
	api.removeRecentView(optionSetId);
});

//5. 클리어 눌렀을 때 전체 삭제할 수 있게 url 요청 


/*
메뉴객체이벤트등록
*/
//const menuGuestHome = document.querySelector("#menu_guest_home");
//const menuGuestList = document.querySelector("#menu_guest_list");
//const menuGuestWriteForm = document.querySelector("#menu_guest_write_form");
/*
window.addEventListener('hashchange',function(e){
	console.log(location.hash);
});
*/
/*$('#menu_guest_home').click(function(e){
	View.render('#guest-main-template');
	e.preventDefault();
});*/
//menuGuestHome.addEventListener('click', function(e) {
//	View.render('#guest-main-template');
//	e.preventDefault();
//});
/*$('#menu_guest_list').click(function(e){
	$.ajax({method:RequestURL.GUEST_LIST.method,
			url:RequestURL.GUEST_LIST.url,
			successs:function(resopnseJsonObject){
							View.render('#guest-list-template',
										resopnseJsonObject);
						}
						});	
	
	e.preventDefault();
});*/
//menuGuestWriteForm.addEventListener('click', function(e) {
//	View.render('#guest-write-form-template');
//	e.preventDefault();
//});
/*$('#menu_guest_write_form').click(function(e){
	View.render('#guest-write-form-template');
	e.preventDefault();
})*/
//menuGuestList.addEventListener('click', function(e) {
	
//	Request.ajaxRequest(RequestURL.GUEST_LIST.method,
//						RequestURL.GUEST_LIST.url,
//						function(resopnseJsonObject){
//							View.render('#guest-list-template',
//										resopnseJsonObject);
//						}
//						);	
	
//	e.preventDefault();
//});

//document.addEventListener('click', function(e) {
	/*
	event target Element 속성
	
	console.log("Event객체:" + e);
	console.log("Event Target 객체:" + e.target);
	console.log("Event Target 객체 id:" + e.target.id);
	console.log("Event Target 객체 className:" + e.target.className);
	console.log("Event Target 객체 classList[0]:" + e.target.classList[0]);
	console.log("Event Target 객체 classList[1]:" + e.target.classList[1]);
	*/
	/*********************************************************
					guest_write_action
	/*********************************************************/
	/*if (e.target.id == 'btn_guest_write_action') {
		if (document.f.guest_name.value == "") {
			alert("이름을 입력하십시요.");
			document.f.guest_name.focus();
			return false;
		}
		if (document.f.guest_email.value == "") {
			alert("이메일을 입력하십시요.");
			document.f.guest_email.focus();
			return false;
		}
		if (document.f.guest_homepage.value == "") {
			alert("홈페이지를 입력하십시요.");
			document.f.guest_homepage.focus();
			return false;
		}

		if (document.f.guest_title.value == "") {
			alert("제목을 입력하십시요.");
			document.f.guest_title.focus();
			return false;
		}
		if (document.f.guest_content.value == "") {
			alert("내용을 입력하십시요.");
			document.f.guest_content.focus();
			return false;
		}
			
		let sendJsonObject={
			guest_no:0,
			guest_date:"",
			guest_name:document.f.guest_name.value,
			guest_email:document.f.guest_email.value,
			guest_homepage:document.f.guest_homepage.value,
			guest_title:document.f.guest_title.value,
			guest_content:document.f.guest_content.value
		};
		
		Request.ajaxRequest(
					RequestURL.GUEST_WRITE_ACTION.method,
					RequestURL.GUEST_WRITE_ACTION.url,
					function(responseJsonObject){
						if(responseJsonObject.status==1){
							//쓰기성공시 리스트클릭이벤트발생
							menuGuestList.click();
						}else{
							alert(responseJsonObject.msg);
						}
					},
					sendJsonObject);
		
		
		
	}*/
	/*********************************************************
					  guest_detail
	/*********************************************************/
	/*if (e.target.classList.contains('guest_item_a')) {
		
		console.log("href    --> "+e.target.href);
		console.log("# 의위치--> "+e.target.href.indexOf('#'))
		
		if(e.target.href.indexOf('#')>=0){
			let guest_no = e.target.href.substring(e.target.href.indexOf('#')+1);
			console.log('guest_no',guest_no);
			Request.ajaxRequest(RequestURL.GUEST_DETAIL.method,
								RequestURL.GUEST_DETAIL.url.replace('@guest_no',guest_no),
								function(responseJsonObject){
									View.render('#guest-view-template',responseJsonObject);
								}
								);
		}
		//e.preventDefault();
	}*/
	/*********************************************************
					  guest_delete_action
	/*********************************************************/
	/*if(e.target.id=='btn_guest_remove_action'){
		let guest_no=document.f.guest_no.value;
		Request.ajaxRequest(RequestURL.GUEST_REMOVE_ACTION.method,
							RequestURL.GUEST_REMOVE_ACTION.url.replace('@guest_no',guest_no),
							function(responseJsonObject){
								if(responseJsonObject.status==1){
									menuGuestList.click();
								}else{
									alert(responseJsonObject.msg);
								}
							});	
	}*/
	/*********************************************************
					  guest_modify_action_form
	/*********************************************************/
	/*if(e.target.id=='btn_guest_modify_form'){
		let guest_no=document.f.guest_no.value;
		Request.ajaxRequest(RequestURL.GUEST_MODIFY_FORM.method,
							RequestURL.GUEST_MODIFY_FORM.url.replace('@guest_no',guest_no),
							function(responseJsonObject){
								if(responseJsonObject.status==1){
									View.render('#guest-modify-form-template',responseJsonObject);
								}else{
									alert(responseJsonObject.msg);
								}
							});	
	}*/
	/*********************************************************
					  guest_modify_action
	/*********************************************************/
	/*if (e.target.id == 'btn_guest_modify_action') {
		if (document.f.guest_name.value == "") {
			alert("이름을 입력하십시요.");
			document.f.guest_name.focus();
			return false;
		}
		if (document.f.guest_email.value == "") {
			alert("이메일을 입력하십시요.");
			document.f.guest_email.focus();
			return false;
		}
		if (document.f.guest_homepage.value == "") {
			alert("홈페이지를 입력하십시요.");
			document.f.guest_homepage.focus();
			return false;
		}

		if (document.f.guest_title.value == "") {
			alert("제목을 입력하십시요.");
			document.f.guest_title.focus();
			return false;
		}
		if (document.f.guest_content.value == "") {
			alert("내용을 입력하십시요.");
			document.f.guest_content.focus();
			return false;
		}
			
		let sendJsonObject={
			guest_no:document.f.guest_no.value,
			guest_date:"",
			guest_name:document.f.guest_name.value,
			guest_email:document.f.guest_email.value,
			guest_homepage:document.f.guest_homepage.value,
			guest_title:document.f.guest_title.value,
			guest_content:document.f.guest_content.value
		};
		
		Request.ajaxRequest(
					RequestURL.GUEST_MODIFY_ACTION.method,
					RequestURL.GUEST_MODIFY_ACTION.url.replace('@guest_no',sendJsonObject.guest_no),
					function(responseJsonObject){
						if(responseJsonObject.status==1){
							//수정성공시 수정한내용보여주기
							Request.ajaxRequest(RequestURL.GUEST_DETAIL.method,
								RequestURL.GUEST_DETAIL.url.replace('@guest_no',sendJsonObject.guest_no),
								function(responseJsonObject){
									View.render('#guest-view-template',responseJsonObject);
								}
								);
						}else{
							alert(responseJsonObject.msg);
						}
					},
					sendJsonObject);
		
		
		
	}*/
	/*********************************************************
					  guest_list
	/*********************************************************/
	/*if(e.target.id=='btn_guest_list'){
		menuGuestList.click();
	}*/
	/*********************************************************
					  guest_write_form
	/*********************************************************/
	/*if(e.target.id=='btn_guest_write_form'){
		menuGuestWriteForm.click();
	}
	
});*/


/*
	#guest-view-template
	#guest-main-template
	#guest-write-form-template
	#guest-modify-form-template
	#guest-list-template
*/

/*
초기로딩시에 home anchor click event trigger
*/
//menuGuestHome.click();












