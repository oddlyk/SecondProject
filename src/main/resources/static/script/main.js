
/*
	메인 화면과 연결되는 JS 코드
 */
// 화면 시작 시...

// 검색 연관 이벤트
let searchBtn = document.getElementById('search_icon');
let searchForm = document.getElementById('SearchVoyage');

if (searchBtn != null) searchBtn.addEventListener('click', gotoSearch);

function gotoSearch() {
	// 검색 단어 추출
	let searchWord = document.getElementById('search_ship').value.trim();
	// 아무것도 적지 않고 검색한 경우 return
	if (searchWord == '') {
		alert('선박의 Call Sign, MMSI, IMO를 통해서 항해를 검색해 주세요.');
		return;
	}
	// 단어가 존재하는 경우 subimit!
	searchForm.submit();
}

// 검색 결과 출력

//검색 결과를 받아왔는가?
let isItSearch = document.getElementById('isItsearch').value;

// 검색을 통한 접근이 아님
if (isItSearch == 0) {
}

// 검색 결과 존재
if (isItSearch == 1) {
	document.getElementById('search_ship').value = document.getElementById('searched_ship').value;

	// 회원 여부에 따른 상세 페이지 이동 여부
	let user = document.getElementById('userId').value;
	// 회원이면 등록 버튼 활성화
	if (user === 'true') {
		document.getElementById('saveBtn').disabled = false; // 버튼 활성화
		// 저장버튼 클릭 시...
		$('#saveBtn').on('click', function () {
			let sendData = { "vNumber": $('#vNumber').val() };
			$.ajax({
				url: "save",
				method: "POST",
				asyns: false, //비동기 처리를 막음 
				data: sendData,
				success: function (resp) {
					if (resp === "OK") alert("관심 항해로 등록되었습니다.");
					if (resp === "over") alert("등록된 항해가 10개가 넘어 저장하지 못했습니다. 마이페이지에서 다른 항해를 제거 후 등록을 시도해 주세요.");
					if (resp === "exist") alert("이미 등록된 항해입니다.");
				},
				error: function (resp) {
					alert("등록에 실패하였습니다.");
				}
			});
		});
	}

	// 각 요소에 대해 반복하며 이벤트 리스너를 추가
	//회원 연관 이벤트 : 로그인된 회원이라면 요청 전송. 아니라면 로그인 화면으로.
	// 선박 상세 정보 전달
	document.querySelectorAll('.service_box1').forEach(element => {
		element.addEventListener('click', goto1);
	});
	function goto1() {
		if (user === 'true') {
			document.getElementById('getship').click();
		} else {
			document.getElementById('tologin').click();
		}
	}

	// 목적지 상세 정보 전달
	document.querySelectorAll('.service_box2').forEach(element => {
		element.addEventListener('click', goto2);
	});
	function goto2() {
		if (user === 'true') {
			document.getElementById('getPort').click();
		} else {
			document.getElementById('tologin').click();
		}
	}
	// 계산 상세 정보 전달
	document.querySelectorAll('.service_box3').forEach(element => {
		element.addEventListener('click', goto3);
	});
	function goto3() {
		if (user === 'true') {
			document.getElementById('getCalc').click();
		} else {
			document.getElementById('tologin').click();
		}
	}

}

if (isItSearch == 2) {
	alert("현재 항해 중인 선박이 아닙니다. 다른 단어로 검색해 주세요.");
}



//날씨 api연결
// OpenWeather API 키
let API_KEY = '13c20983e47bfa6ec46e8491cea98b88'; // OpenWeather API 키


// 각 항구의 좌표
let ports = {
	'인천항': { latitude: 37.4483, longitude: 126.5975 },
	'부산항': { latitude: 35.1028, longitude: 129.0403 },
	'부산신항': { latitude: 35.0664, longitude: 128.8358 },
};

// 날씨 데이터를 가져오는 비동기 함수
async function getWeather() {
	if (isItSearch == 1) {
		// 항구 이름
		let portName = document.getElementById('port').value;
		let latitude = null;
		let longitude = null;

		// 선택된 항구의 좌표 설정
		if (portName === '인천항') {
			latitude = ports['인천항'].latitude;
			longitude = ports['인천항'].longitude;
		} else if (portName === '부산항') {
			latitude = ports['부산항'].latitude;
			longitude = ports['부산항'].longitude;
		} else if (portName === '부산신항') {
			latitude = ports['부산신항'].latitude;
			longitude = ports['부산신항'].longitude;
		}

		// 좌표값 확인 (테스트 용도)
		console.log(`선택된 항구: ${portName}, 위도: ${latitude}, 경도: ${longitude}`);

		// OpenWeather API 요청 URL 생성
		let url = `https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=${API_KEY}&units=metric`;

		// 날씨 정보를 요청하고 처리
		try {
			let response = await axios.get(url);  // CDN에서 불러온 axios 사용
			let weatherData = response.data;

			// 날씨 정보 출력
			console.log(`\n${portName}의 현재 날씨:`);
			console.log(`온도: ${weatherData.main.temp}°C`);
			console.log(`날씨: ${weatherData.weather[0].main}`);
			console.log(`습도: ${weatherData.main.humidity}%`);
			console.log(`풍속: ${weatherData.wind.speed} m/s`);
			document.getElementById('tempa').innerText = weatherData.main.temp;
			let weatherIcon = document.getElementById('weatherIcon');
			weatherIcon.src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;
			weatherIcon.alt = `${weatherData.weather[0].main}`;


		} catch (error) {
			console.error('날씨 정보를 가져오는 중 오류가 발생했습니다:', error);
		}
	}
}

// 페이지 로딩 시 getWeather 함수 호출
document.addEventListener('DOMContentLoaded', function () {
	getWeather();
});