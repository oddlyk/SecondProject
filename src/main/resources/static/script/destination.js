let map;
let marker;
let waitingAreas = [];
let waitingAreaPolygons = [];

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
	document.getElementById('homepage-link').onclick = function () {
		window.open(portUrls["KRPUS"], "_blank"); // 새로운 탭으로 홈페이지 열기
	};

	// 첫 화면에서 기본 항구의 사고 통계 불러오기 (부산항)
	loadAccidentStats("KRPUS");  // 첫 화면 로딩 시 호출
}

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

// 새로운 함수로 사고 통계 불러오는 코드 분리
function loadAccidentStats(portCode) {
	$.ajax({
		url: "/port/changePort",
		method: "GET",
		data: { "port": portCode },
		success: function (resp) {
			console.log("AJAX 응답 데이터:", resp);

			// 1. 항구 정보 업데이트 (지도와 마커)
			const portInfo = resp.port;
			const portCoordinates = { lat: portInfo.portLat, lng: portInfo.portLon };
			map.setCenter(portCoordinates);

			if (marker) {
				marker.setMap(null);  // 기존 마커 제거
			}
			marker = new google.maps.Marker({
				position: portCoordinates,
				map: map
			});

			// 2. 날씨 정보 업데이트
			updateWeather(portCoordinates.lat, portCoordinates.lng); // 날씨 업데이트도 유지

			// 3. 사고 통계 업데이트
			const accidentStatus = resp.accidentStatus;
			if (accidentStatus.length > 0) {
				// HTML 요소에 사고 통계 데이터 반영
				console.log(accidentStatus);  // 데이터를 로그로 확인
				$("#rank1").text(accidentStatus[0].firstRank || 'N/A');
				$("#count1").text(accidentStatus[0].firstPer ? accidentStatus[0].firstPer + "%" : 'N/A');
				$("#rank2").text(accidentStatus[0].secondRank || 'N/A');
				$("#count2").text(accidentStatus[0].secondPer ? accidentStatus[0].secondPer + "%" : 'N/A');
				$("#rank3").text(accidentStatus[0].thirdRank || 'N/A');
				$("#count3").text(accidentStatus[0].thirdPer ? accidentStatus[0].thirdPer + "%" : 'N/A');
				$("#no-data-message").hide();  // 데이터가 있으면 메시지 숨김
			} else {
				// 사고 통계가 없는 경우 기본값 유지 및 메시지 표시
				$("#rank1").text("-");
				$("#count1").text("- 회");
				$("#rank2").text("-");
				$("#count2").text("- 회");
				$("#rank3").text("-");
				$("#count3").text("- 회");
				$("#no-data-message").show();  // 데이터 없을 때 메시지 표시
			}

			// 대기지 정보 업데이트
			waitingAreas = resp.waitingAreas; // waitingAreas 전역 변수에 저장
			console.log("대기지 정보: ", waitingAreas);

			// 지도에 대기지 폴리곤을 표시
			updateWaitingAreas(waitingAreas);

			// 기타 항구 관련 정보 로그 확인
			console.log("Accident Status: ", resp.accidentStatus);
			console.log("Waiting Areas: ", resp.waitingAreas);
			console.log("Container Terminals: ", resp.containerTerminals);
			console.log("Congestion Areas: ", resp.congestionAreas);
		},
		error: function (error) {
			console.error("항구 정보를 불러오는 중 오류 발생:", error);
		}
	});
}

// 항구 선택 변경 이벤트 리스너에서도 사고 통계 정보를 업데이트
$("#portchoose").on('change', function () {
	const selectedPort = $('#portchoose').val();
	loadAccidentStats(selectedPort);  // 항구 선택 시 사고 통계 업데이트
});



// 대기지 폴리곤 업데이트 함수
function updateWaitingAreas(waitingAreas) {
    console.log("대기지 데이터를 업데이트합니다:", waitingAreas);

    // 기존 폴리곤 제거
    waitingAreaPolygons.forEach(polygon => polygon.setMap(null));
    waitingAreaPolygons = [];

    // 대기지 데이터로 폴리곤 생성
    waitingAreas.forEach(area => {
        // area.location이 배열인지 확인하고, 아니라면 배열로 변환
        const paths = Array.isArray(area.location) ? area.location : [area.location];
        
        const polygon = new google.maps.Polygon({
            paths: paths, // 배열로 전달
            strokeColor: '#FF0000',
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: '#FF0000',
            fillOpacity: 0.35
        });
        polygon.setMap(map); // 지도에 폴리곤 추가
        waitingAreaPolygons.push(polygon); // 생성된 폴리곤을 배열에 추가
    });
    
    console.log("폴리곤이 업데이트되었습니다.");
}


// 토글 이벤트 리스너 추가
document.getElementById('toggleInfoWaiting').addEventListener('change', function() {
    if (this.checked) {
        console.log(waitingAreas);
        // 토글이 켜져 있을 때, 폴리곤을 지도에 다시 표시
        waitingAreaPolygons.forEach(polygon => polygon.setMap(map));
    } else {
        // 토글이 꺼져 있을 때, 폴리곤을 지도에서 제거
        waitingAreaPolygons.forEach(polygon => polygon.setMap(null));
    }
});
