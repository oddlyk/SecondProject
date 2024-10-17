let map;
let terminalMarker, congestionMarker;

window.initMap = function () {
    // 지도 초기화
	console.log("initMap 함수 호출됨");
	map = new google.maps.Map(document.getElementById("map"), {
	    center: { lat: 37.5665, lng: 126.9780 },
	    zoom: 12
	});

    // 마커 이미지 설정
    const terminalImage = {
        url: '/images/terminal.png',
        scaledSize: new google.maps.Size(50, 50)
    };

    const congestionImage = {
        url: '/images/warning.png',
        scaledSize: new google.maps.Size(50, 50)
    };

    // 토글 버튼 제어
    const togglewaiting = document.getElementById("toggle-waiting");
    const toggleTerminal = document.getElementById("toggle-terminal");
    const toggleCongestion = document.getElementById("toggle-congestion");

    // 인근 대기지 표시

    // 컨테이너 터미널 마커
    toggleTerminal.addEventListener('change', function () {
        if (this.checked) {
            terminalMarker = new google.maps.Marker({
                position: { lat: 37.5700, lng: 126.9768 },
                map: map,
                icon: terminalImage,
            });
        } else {
            if (terminalMarker) terminalMarker.setMap(null);  // 마커 제거
        }
    });

    // 혼잡 주의 지역 마커
    toggleCongestion.addEventListener('change', function () {
        if (this.checked) {
            congestionMarker = new google.maps.Marker({
                position: { lat: 37.5600, lng: 126.9810 },
                map: map,
                icon: congestionImage,
            });
        } else {
            if (congestionMarker) congestionMarker.setMap(null);  // 마커 제거
        }
    });



    // 줌 인, 아웃 버튼 추가
    document.getElementById('zoom-in').addEventListener('click', function () {
        map.setZoom(map.getZoom() + 1);  // 지도 확대
    });

    document.getElementById('zoom-out').addEventListener('click', function () {
        map.setZoom(map.getZoom() - 1);  // 지도 축소
    });

}

// 항구 정보에 따른 데이터 업데이트
const portSelect = document.getElementById("portchoose");
const portName = document.getElementById("port-name");
const phoneNum = document.getElementById("phone-num");
const homepageLink = document.getElementById("homepage-link");

// 데이터 업데이트 함수
portSelect.addEventListener('change', function() {
    const selectedPort = portSelect.value;
	if (selectedPort === "KRPUS") {
	        portName.textContent = "부산항";
	        phoneNum.textContent = "051-999-3000"; 
	        homepageLink.setAttribute('href', "http://www.busanpa.com");
	    } else if (selectedPort === "KRBNP") {
	        portName.textContent = "부산신항";
	        phoneNum.textContent = "051-941-6400"; 
	        homepageLink.setAttribute('href', "http://www.busanpa.com");
	    } else if (selectedPort === "KRICN") {
	        portName.textContent = "인천항";
	        phoneNum.textContent = "032-890-8000"; 
	        homepageLink.setAttribute('href', "http://www.icpa.or.kr");
	    }
});

// 페이지 로드 시 기본 선택 항구로 초기화
document.addEventListener("DOMContentLoaded", function() {
    portSelect.dispatchEvent(new Event('change'));
});


const API_KEY = '13c20983e47bfa6ec46e8491cea98b88'; // OpenWeatherMap API 키

// 날씨 정보 업데이트 함수
async function getWeatherByPort(latitude, longitude) {
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=${API_KEY}&units=metric`;

    try {
        const response = await axios.get(url);
        const weatherData = response.data;

        // 날씨 상태와 정보 업데이트
        const description = weatherData.weather[0].description;
        const temperature = weatherData.main.temp;

        // 날씨 텍스트 업데이트 (날씨 이미지 대신)
        document.querySelector('.weather').innerHTML = `<p>${temperature}°C, ${description}</p>`;
    } catch (error) {
        console.error('날씨 정보를 가져오는 중 오류가 발생했습니다:', error);
    }
}


// 항구 선택 시 날씨 정보 업데이트
portSelect.addEventListener('change', function () {
    const selectedPort = portSelect.value;

    // 선택된 항구에 따라 날씨 좌표 가져오기
    if (selectedPort === "KRPUS") {
        getWeatherByPort(35.1028, 129.0403); // 부산항 좌표
    } else if (selectedPort === "KRBNP") {
        getWeatherByPort(35.0664, 128.8358); // 부산신항 좌표
    } else if (selectedPort === "KRICN") {
        getWeatherByPort(37.4483, 126.5975); // 인천항 좌표
    }
});

// 페이지 로드 시 기본 항구의 날씨 정보 가져오기
document.addEventListener("DOMContentLoaded", function () {
    portSelect.dispatchEvent(new Event('change')); // 기본 항구 선택 이벤트 발생
});

// DOMContentLoaded를 통해 지도가 초기화되도록 보장
document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');
});