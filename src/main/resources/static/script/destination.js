let map; // 지도를 저장할 변수
let marker; // 마커를 저장할 변수

// 항구별 항만공사 웹사이트 URL을 저장한 객체
const portUrls = {
    "KRPUS": "https://www.busanpa.com",  // 부산항 항만공사
    "KRBNP": "https://www.busanpa.com",  // 부산신항 항만공사 (같은 사이트 사용)
    "KRINC": "https://www.icpa.or.kr"    // 인천항 항만공사
};

// 초기 지도 설정
function initMap() {
    const defaultLocation = { lat: 35.1028, lng: 129.0403 }; // 기본 위치 (부산항)

    // 기본 지도 생성
    map = new google.maps.Map(document.getElementById('map'), {
        center: defaultLocation,
        zoom: 12
    });

    // 기본 마커 생성
    marker = new google.maps.Marker({
        position: defaultLocation,
        map: map
    });

    console.log("Google Maps 초기화 완료");
	
	// 기본 위치의 날씨 정보 업데이트
	updateWeather(defaultLocation.lat, defaultLocation.lng);
	
	// 기본 항구의 홈페이지 버튼 클릭 이벤트 설정
	document.getElementById('homepage-link').onclick = function() {
	    window.open(portUrls["KRPUS"], "_blank"); // 새로운 탭으로 홈페이지 열기
	   };
}

// 항구 선택 이벤트 리스너
/**
 *  document.addEventListener("DOMContentLoaded", function() {
     const portSelect = document.getElementById("portchoose");

     // 항구 선택이 변경될 때마다 지도를 업데이트
     portSelect.addEventListener("change", async function() {
         const selectedPort = portSelect.value;

         let portCoordinates;

         // 선택된 항구에 따라 좌표 설정
         if (selectedPort === "KRPUS") {
             portCoordinates = { lat: 35.1028, lng: 129.0403 }; // 부산항 좌표
         } else if (selectedPort === "KRBNP") {
             portCoordinates = { lat: 35.0800, lng: 128.8583 }; // 부산신항 좌표
         } else if (selectedPort === "KRICN") {
             portCoordinates = { lat: 37.4485, lng: 126.6003 }; // 인천항 좌표
         }

         // 지도 중앙을 선택된 항구로 이동
         map.setCenter(portCoordinates);

         // 기존 마커가 있으면 제거
         if (marker) {
             marker.setMap(null);
         }

         // 새로운 마커 추가
         marker = new google.maps.Marker({
             position: portCoordinates,
             map: map
         });

         console.log(`${selectedPort}로 지도 위치가 변경되었습니다.`);
 		
 		// 선택된 항구의 날씨 정보 업데이트
 		await updateWeather(portCoordinates.lat, portCoordinates.lng);
 		
 		// 선택된 항구에 맞는 홈페이지 버튼 클릭 이벤트 설정
 		document.getElementById('homepage-link').onclick = function() {
 		    window.open(portUrls[selectedPort], "_blank"); // 새로운 탭으로 홈페이지 열기
 		        };
     });
 });
 * 
 */

// 날씨 정보를 OpenWeather API로부터 받아오는 함수
async function updateWeather(lat, lng) {
    // OpenWeather API 키를 함수 내부에 포함
    const apiKey = '13c20983e47bfa6ec46e8491cea98b88';  // API 키를 여기에 직접 넣습니다.
    const weatherUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lng}&appid=${apiKey}&units=metric`;

	try {
	    const response = await axios.get(weatherUrl);
	    const weatherData = response.data;

	    // 날씨 정보를 HTML에 반영
	    document.getElementById('weather-text').textContent = `${weatherData.main.temp}°C, ${weatherData.weather[0].description}`;
	    document.getElementById('weatherIcon').src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;

	    console.log("날씨 정보:", weatherData);
	} catch (error) {
	    console.error("날씨 정보를 불러오는 중 오류 발생:", error);
	}
}

$("#portchoose").on('change',function(){
	$.ajax({
		url: "/port/changePort",
		method: "GET",
		data: {"port":$('#portchoose').val()},
		success: function(resp){
			console.log(resp);
            // 응답에서 필요한 데이터를 사용할 수 있습니다
            console.log("Port Info: ", resp.port);
            console.log("Accident Status: ", resp.accidentStatus);
            console.log("Waiting Areas: ", resp.waitingAreas);
            console.log("Container Terminals: ", resp.containerTerminals);
            console.log("Congestion Areas: ", resp.congestionAreas);
		}
	})
})


