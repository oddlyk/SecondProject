/**
 * 
 * 
// get Loader class from package
import { Loader } from 'https://cdn.jsdelivr.net/npm/@googlemaps/js-api-loader@1.14.3/+esm';

// create apiOptions obj
const apiOptions = {
apiKey: "AIzaSyCcTJ2Vc3GipTS3tfRuyvMAgMNyyTu45LA",
};

const loader = new Loader(apiOptions);

// Promise
loader.load().then(() => {
console.log("Mas JS API Loaded!");
const map = displayMap();
});

function displayMap() {
const mapOptions = {
  center: { lat: -33.860664, lng: 151.208138 },
  zoom: 14,
};
const mapDiv = document.getElementById("map");

// Create instance of Google.maps.Map
// - To create new map to mark something
const map = new google.maps.Map(mapDiv, mapOptions);
return map;
}
 */

function myMap() {
	let nowLoc = JSON.parse('{ "lat": 36.5, "lng": 127.5 }'); //현위치 좌표가 없을 때
	// 현위치 좌표
	let GetnowLoc = document.getElementById('GetnowLoc');
	if (GetnowLoc != null) {
		if (GetnowLoc.value != "") {
			nowLoc = JSON.parse(GetnowLoc.value);
		}
	}

	//현위치를 중심으로 지도 띄우기 (현위치가 없다면 대한민국 정중앙 좌표)
	let map = new google.maps.Map(
		document.getElementById("map"), // 'map' ID를 가진 div를 사용
		{
			center: nowLoc,
			zoom: 7,
			zoomControl: true,  // 확대/축소 버튼 활성화
			zoomControlOptions: {
				position: google.maps.ControlPosition.TOP_RIGHT // 버튼 위치 설정
			},
			mapTypeControl: false, // 지형 버튼 제거
			streetViewControl: false,  // 스트리트 뷰 버튼 제거
			fullscreenControl: false,    // 전체 화면 보기 버튼 제거
			styles: [
				{
					featureType: "poi", // 모든 POI (상점, 관광지, 공원 등)
					elementType: "labels.icon", // POI의 아이콘만 숨기기
					stylers: [{ visibility: "off" }] // 아이콘 숨기기
				}
			]
		}
	);

	// 선박의 실시간 정보 전달
	// (GetnowLoc가 null이 아닐때만) = 항해가 시작도 안한 선박은 선박의 실시간 정보를 띄우지 않는다.
	if (GetnowLoc != null) {
		if (GetnowLoc.value != "") {
			//마커 이미지 변경
			let markerImage = { url: `https://cdn-icons-png.flaticon.com/512/6041/6041892.png`, scaledSize: new google.maps.Size(50, 50) };

			//지도에 마커 추가
			let marker = new google.maps.Marker({
				position: nowLoc,
				map: map,
				icon: markerImage,
				title: "현재 위치"
			});

			//선박 바로 위 팝업에 들어갈 HTML
			// div 요소 가져오기
			let overTheMap = document.getElementById('overTheMap');
			// 선박 바로 위 팝업 추가 
			let shipInfo = new google.maps.InfoWindow({
				content: overTheMap
			});
			shipInfo.open(map, marker);
			// 선박을 클릭하면  열림
			marker.addListener("click", () => {
				shipInfo.open(map, marker);
			});
		}
	}

	//항해 정보 띄우기 : CSS
}

document.addEventListener('DOMContentLoaded', function () {
	myMap();
})

