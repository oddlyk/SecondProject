let map;
let marker;
let waitingAreas = [];
let waitingAreaPolygons = [];
let terminal_markers = [];
let con_markers = [];
// 초기 지도 설정
function initMap() {
	const defaultLocation = { lat: 35.1028, lng: 129.0403 }; // 기본 위치 (부산항)

	// 기본 지도 생성
	map = new google.maps.Map(document.getElementById('map'), {
		center: defaultLocation,
		zoom: 12,
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
	});


	console.log("Google Maps 초기화 완료");

	// 항구 코드를 기준으로 변경 요청 
	loadAccidentStats(document.getElementById('portcode').value);
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
		document.getElementById('weather-text').textContent = `${weatherData.main.temp}°C`;
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
			//console.log("AJAX 응답 데이터:", resp);

			// 1. 항구 정보 업데이트
			const portInfo = resp.port;
			const portCoordinates = { lat: portInfo.portLat, lng: portInfo.portLon };
			// 지도 관련
			map.setCenter(portCoordinates);

			if (marker) {
				marker.setMap(null);  // 기존 마커 제거
			}
			marker = new google.maps.Marker({
				position: portCoordinates,
				map: map
			});

			// 항구 정보 관련
			// 항구 이름
			$('#port-name').text(portInfo.portName);
			// 날씨
			updateWeather(portInfo.portLat, portInfo.portLon);
			// 전화번호
			$('#contact').text(portInfo.portContact);
			// 홈페이지 이벤트
			document.getElementById('homepage-link').onclick = function () {
				window.open(portInfo.portUrl, "_blank");
			};

			// 3. 사고 통계 업데이트
			//console.log("Accident Status: ", resp.accidentStatus);
			const accidentStatus = resp.accidentStatus;
			if (accidentStatus.length > 0) {
				// HTML 요소에 사고 통계 데이터 반영
				//console.log(accidentStatus);  // 데이터를 로그로 확인
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
				$("#count1").text("00%");
				$("#rank2").text("-");
				$("#count2").text("00%");
				$("#rank3").text("-");
				$("#count3").text("00%");
				$("#no-data-message").show();  // 데이터 없을 때 메시지 표시
			}

			// 대기지 정보 업데이트
			waitingAreas = resp.waitingAreas; // waitingAreas 전역 변수에 저장
			//console.log("대기지 정보: ", waitingAreas);

			// 지도에 대기지 폴리곤을 표시
			updateWaitingAreas(waitingAreas);


			// 터미널 마커 표시
			//console.log("Container Terminals: ", resp.containerTerminals);
			updateTerminals(resp.containerTerminals);

			// 혼잡지역 표시
			//console.log("Congestion Areas: ", resp.congestionAreas);
			updateCong(resp.congestionAreas);
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
	//console.log("대기지 데이터를 업데이트합니다:", waitingAreas);

	// 기존 폴리곤 제거
	waitingAreaPolygons.forEach(polygon => polygon.setMap(null));
	waitingAreaPolygons.length = 0;

	// 대기지 데이터로 폴리곤 생성
	waitingAreas.forEach(area => {
		// 문자열을 JSON 형식으로 변환 (lat, lng에 쌍따옴표 추가)
		let validJsonString = area.location.replace(/lat/g, '"lat"').replace(/lng/g, '"lng"');
		let coordinates = JSON.parse(validJsonString);

		// area.location이 배열인지 확인하고, 아니라면 배열로 변환
		//const paths = Array.isArray(area.location) ? area.location : [area.location];
		//alert(paths);
		const polygon = new google.maps.Polygon({
			paths: coordinates, // 배열로 전달
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


// 터미널 마커 표시
function updateTerminals(terminals) {
	terminal_markers.forEach(marker => marker.setMap(null)); // 모든 마커를 지도에서 제거
	terminal_markers = []; // 배열 초기화
	terminals.forEach(one => {
		let coordsString = one.location;
		let validJsonString = coordsString.replace(/lat/g, '"lat"').replace(/lng/g, '"lng"');
		let markerImage = { url: `https://s3-alpha-sig.figma.com/img/0db5/8747/69876bd6bf8142eb1b067cbb367ed0c2?Expires=1730678400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=c4MscJRt6Rf9rAhupxyAFoiuyA~B1tQilCjB-TW0xvilcYdUN8R5eIDTFLC6aHTKn0zoeK1gfrbRMZM7UFtAGEm~A~c5W0OPCi0Mvzdg70PJ5lz43kGb3nE3MgwU5LkzAoxy5bx2gXjz8OUoRNW5DpDX97lfaOPooA1iNyaYxR6IMGMJ3Ie3areJEU5uy8xQIKuWsiN5g1a2L55byD4-m6Eu2TxkdJayRyzEbugP3fwpGPFFPNR-KdZreGiCNas9YyVMxWbrhtCixtIhGqQRlSkmunSdU8QIAcR04ZFTOhv2FHmaiG7EW7NAVwTpOnVIc2E~V3a5IKcnWYDQP-kolw__`, scaledSize: new google.maps.Size(30, 30) };
		const Tmarker = new google.maps.Marker({
			position: JSON.parse(validJsonString),
			map: map,
			title: one.info,
			icon: markerImage
		})
		terminal_markers.push(Tmarker);

		const infoWindow = new google.maps.InfoWindow({
			content: one.info
		});

		Tmarker.addListener("click", () => {
			infoWindow.open({
				anchor: Tmarker,
				map,
				shouldFocus: false,
			});
		});
	})
}

// 혼잡지역 표시
function updateCong(congestionAreas) {
	con_markers.forEach(marker => marker.setMap(null)); // 모든 마커를 지도에서 제거
	con_markers = []; // 배열 초기화
	congestionAreas.forEach(one => {
		let coordsString = one.location;
		let validJsonString = coordsString.replace(/lat/g, '"lat"').replace(/lng/g, '"lng"');
		let markerImage = { url: `https://s3-alpha-sig.figma.com/img/c0ca/57b2/887639de4999a48a1902801c6dc0eb81?Expires=1730678400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=pSy04T2dhocCC9gIy3mM1HMUs535K7S0MA0QDDjaG8D6WABn0ABTmwf9m~eDQr-nwyVYVyPsPr7EL5h8ioZ7P1-tDbJcyFOYzK3h02quJv38o9pOKbxmFkuZ4l2v6JqL0fGgw2o6urxckievtw~cO15HJHSGnc5c6A8sHtyKERlDtd1F6~8O2GF8VTP3ZKsNDnIKU1WyRFtjyu979p4RhQ-lLUKADGImH9A8q8W3svrt0SPUxOYu1UrBxUMgkF5DKwIkgIoBHEIzodH9LxhhZOD6BQAGvjQOtxqTwCiteQ8G7kRM8siu8K0CSLNAutSjbgTPJURHdMk-YKE8KgojJQ__`, scaledSize: new google.maps.Size(30, 30) };
		const Cmarker = new google.maps.Marker({
			position: JSON.parse(validJsonString),
			map: map,
			icon: markerImage
		})
		con_markers.push(Cmarker);
	})
}


// 토글 이벤트 리스너 추가
// 대기지
document.getElementById('toggleInfoWaiting').addEventListener('change', function () {
	if (this.checked) {
		//console.log(waitingAreas);
		// 토글이 켜져 있을 때, 폴리곤을 지도에 다시 표시
		waitingAreaPolygons.forEach(polygon => polygon.setMap(map));
	} else {
		// 토글이 꺼져 있을 때, 폴리곤을 지도에서 제거
		waitingAreaPolygons.forEach(polygon => polygon.setMap(null));
	}
});

// 터미널
document.getElementById('toggleInfoTerminal').addEventListener('change', function () {
	if (this.checked) {
		// 토글이 켜져 있을 때, 마커들을 지도에 다시 표시
		terminal_markers.forEach(marker => marker.setMap(map));
	} else {
		// 토글이 꺼져 있을 때, 마커들을 지도에서 제거
		terminal_markers.forEach(marker => marker.setMap(null));
	}
});

// 혼잡지역
document.getElementById('toggleInfoCongestion').addEventListener('change', function () {
	if (this.checked) {
		// 토글이 켜져 있을 때, 마커들을 지도에 다시 표시
		con_markers.forEach(marker => marker.setMap(map));
	} else {
		// 토글이 꺼져 있을 때, 마커들을 지도에서 제거
		con_markers.forEach(marker => marker.setMap(null));
	}
});

