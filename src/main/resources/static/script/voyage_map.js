async function myMap() {
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
			let weatherIconLink = 'https://openweathermap.org/img/wn/01d@2x.png';
			// 현위치 좌표
			let GetnowLoc = document.getElementById('GetnowLoc').value;
			// div 요소 가져오기
			weatherIconLink = await getWeather(GetnowLoc);
			// 선박 바로 위 팝업 추가 
			let shipInfo = new google.maps.InfoWindow({
				content: weatherIconLink
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

// 날씨 데이터를 가져오는 비동기 함수
async function getWeather(GetnowLoc) {
	if(GetnowLoc=="") return "현위치 파악 불가";
    let nowLoc = JSON.parse(GetnowLoc);
    console.log(nowLoc);
    // OpenWeather API 키
    let API_KEY = '13c20983e47bfa6ec46e8491cea98b88'; // OpenWeather API 키
    // OpenWeather API 요청 URL 생성
    let url = `https://api.openweathermap.org/data/2.5/weather?lat=${nowLoc.lat}&lon=${nowLoc.lng}&appid=${API_KEY}&units=metric`;

    // 날씨 정보를 요청하고 처리
    try {
        let response = await axios.get(url);  // CDN에서 불러온 axios 사용
        let weatherData = response.data;

        // 날씨 정보 출력
        console.log(`\n${GetnowLoc}의 현재 날씨:`);
        console.log(`온도: ${weatherData.main.temp}°C`);
        console.log(`날씨: ${weatherData.weather[0].main}`);
        console.log(`습도: ${weatherData.main.humidity}%`);
        console.log(`풍속: ${weatherData.wind.speed} m/s`);
//        document.getElementById('tempa').innerText = weatherData.main.temp;
//        let weatherIcon = document.getElementById('weatherIcon');
//        weatherIcon.src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;
//        weatherIcon.alt = `${weatherData.weather[0].main}`;
//        document.getElementById('windSpeed').innerText = weatherData.wind.speed;
	return		`
	       <div style="width: 300px; height: 100px;">
	           <div class="over1">
	               <p class="overinfo">현재 날씨</p>
	               <p class="weather overinfo" style="height: 40px;">
	                   <img src="${`https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`}" alt="Weather Icon" id="weatherIcon">
	               </p>
	               <p class="temparature overinfo">
	                   <span id="tempa">${weatherData.main.temp}</span>
	                   <span class="celsius minsize">°C</span>
	               </p>
	               <div class="Line2"></div>
	               <p class="speed overinfo">
	                   <span id="windSpeed">${weatherData.wind.speed}</span>
	                   <span class="minsize">m/s</span>
	               </p><br>
	           </div>
	           <div class="over2">
	               <p class="overinfo">속도: <span id="shipSpeed">${document.getElementById("shipSpeed").value || '0.0'}</span> kn</p>
	               <div class="Line2"></div>
	               <p class="overinfo">이동 방향: <span id="nowCor">${document.getElementById("nowCor").value || '0.0'}</span> °</p>
	           </div>
       </div>
		   `

    } catch (error) {
        console.error('날씨 정보를 가져오는 중 오류가 발생했습니다:', error);
		return "현위치 파악 불가";
    }
}

document.addEventListener('DOMContentLoaded', function () {
	myMap();
})

