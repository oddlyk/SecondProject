
/*
	메인 화면과 연결되는 JS 코드
 */

// 선박 검색
$(function(){
	init();
	$('#search_icon').on('click',goToSearch);
})

// 검색된 결과를 받아오지 못한경우, isItsearch가 0이 됨.
function init(){
	let isItSearch = $('#isItsearch').val();
	if (isItSearch == 2){
		alert("현재 항해 중인 선박이 아닙니다. 다른 단어로 검색해 주세요.");
	}
}

function goToSearch(){
	let searchWord = $("#search_ship").val();

	if(searchWord==''){
		alert("선박의 Call Sign, MMSI, IMO를 통해서 항해를 검색해 주세요.")
		return;
	}
	let SearchVoyage = document.getElementById("SearchVoyage")
	SearchVoyage.action = `/ship=${searchWord}`;
	SearchVoyage.submit();
}
	
