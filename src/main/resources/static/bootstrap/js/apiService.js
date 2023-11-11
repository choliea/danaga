import {API_BASE_URL,REMOVE_RECENT_VIEW,ADD_WISHLIST,REMOVE_WISHLIST,
    TAP_HEART,UNTAP_HEART,CHILD_CATEGORY,SHOW_OPTIONS,SEARCH} from "./api-config.js";
import {render} from "./view.js";
function call(api, method,request){
    let headers = new Headers({
        "Content-Type": "application/json",
    })
    // const accessToken = localStorage.getItem("ACCESS_TOKEN");
    // if( accessToken &&accessToken !==null){
    //     headers.append("Authorization","Bearer " + accessToken);
    // }
    let options = {
        headers: headers,
        url: API_BASE_URL +api,
        method: method, 
    }; 
    if(request){
        //GET method
        options.body = JSON.stringify(request);
    }
    return fetch(options.url, options).then((response) => {
        if(response.status === 200){
            return response.json();
        }else if(response.status===403){
            window.location.href="/member/login";// redirect
        }else{
            Promise.reject(response);
            throw Error(response);
        }
    }).catch((error) => {
        console.log("http error");
        console.log(error);
    });
}

export function tapHeart(optionSetId){//디테일에서//클릭이벤트핸들러에서 이미지로 어떤 함수 쓸건지 결정
    return call(TAP_HEART.url.replace('@optionSetId',optionSetId),TAP_HEART.method,null)
    .then((response) =>{
       //하트 이미지 바꾸는 코드
    });
}
export function untapHeart(optionSetId){//디테일에서//그리고 애초에 서버에서 이미지 뿌릴때 좋아요 여부 확인해서 이미지 알맞게 뿌려야함
    return call(UNTAP_HEART.url.replace("@optionSetId",optionSetId),UNTAP_HEART.method,null)
    .then((response) =>{
       //하트 이미지 바꾸는 코드
    });//그리고 하트 누르는 서비스에서 반환값은 없어도 됨 있으려면 차라리 하트이미지 경로를 주던가
}
export function addwish(optionSetId){//마이페이지에서//클릭이벤트핸들러에서 이미지로 어떤 함수 쓸건지 결정
    return call(ADD_WISHLIST.url.replace('@optionSetId',optionSetId),ADD_WISHLIST.method,null)
    .then((response) =>{
       //하트 이미지 바꾸는 코드
    });
}
export function removewish(optionSetId){//마이페이지에서//그리고 애초에 서버에서 이미지 뿌릴때 좋아요 여부 확인해서 이미지 알맞게 뿌려야함
    return call(REMOVE_WISHLIST.url.replace("@optionSetId",optionSetId),REMOVE_WISHLIST.method,null)
    .then((response) =>{
       //하트 이미지 바꾸는 코드
    });//그리고 하트 누르는 서비스에서 반환값은 없어도 됨 있으려면 차라리 하트이미지 경로를 주던가
}
export function removeRecentView(optionSetId){//최근본상품 하나 삭제
    return call(REMOVE_RECENT_VIEW.url.replace("@optionSetId",optionSetId),REMOVE_RECENT_VIEW.method,null)
    .then((response) =>{
       //
    });
}

export function subCategory(categoryId){//대분류 선택하면 발생할 api
    return call(CHILD_CATEGORY.url.replace("@categoryId",categoryId),CHILD_CATEGORY.method,null)
    .then((response)=>{
        //옆 섹션에 자식카테고리들 뿌리기 (templateId="#guest-main-template",jsonResult={},contentId="#content")
        render("#subcategory-template",response,"#subcategory-template");
        render("#option-choice-template",{},"option-choice-template");
    });
}

export function showOptions(categoryId){
    return call(SHOW_OPTIONS.url.replace("@categoryId",categoryId),SHOW_OPTIONS.method,null)
    .then((response)=>{
        //옆 섹션에 선택 가능한 옵션들 뿌리기
        render("#option-choice-template",response,"option-choice-template");
    });
}
export function searchResult(filterDto){//검색결과 보여주기
    return call(SEARCH.url,SEARCH.method,filterDto)
    .then((response)=>{
        //검색 결과 뿌리기
    });
}