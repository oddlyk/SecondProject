
/*
	메인 화면과 연결되는 JS 코드
 */
// 이벤트 소스
let searchBtn = document.getElementById('search_icon');
let searchForm = document.getElementById('SearchVoyage');

// 이벤트 핸들러와 연결
if(searchBtn!=null) searchBtn.addEventListener('click', gotoSearch);

// 이벤트 핸들러
	// 검색으로!
function gotoSearch(){
	// 검색 단어 추출
	let searchWord = document.getElementById('search_ship').value;
	// 아무것도 적지 않고 검색한 경우
	if (searchWord==''){
		alert('선박의 Call Sign, MMSI, IMO를 통해서 항해를 검색해 주세요.');
		return;
	}
	// 단어가 존재하는 경우 subimit!
	searchForm.submit();
}

// 검색 결과 출력

//검색 결과를 받아오지 못한경우...
let isItSearch = document.getElementById('isItsearch').value;
if(isItSearch==2){
	alert("현재 항해 중인 선박이 아닙니다. 다른 단어로 검색해 주세요.");
} else{

}

