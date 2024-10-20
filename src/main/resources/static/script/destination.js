let map;
let terminalMarker, congestionMarker;
let waitingMarkers = [];
let terminalMarkers = [];
let congestionPolygons = [];

// 마커 이미지 설정을 전역 변수로 이동
const terminalImage = {
    url: '/images/terminal.png',
    // scaledSize: new google.maps.Size(50, 50) // 크기 조절이 필요하면 활성화
};

const congestionImage = {
    url: '/images/warning.png',
    // scaledSize: new google.maps.Size(50, 50) // 크기 조절이 필요하면 활성화
};

// 구글 맵스 기동 및 초기화
document.addEventListener('DOMContentLoaded', function() {
	
})
window.initMap = function() {
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 35.1028, lng: 129.0403 },
        zoom: 12,
    });

    console.log("구글 맵스 지도 초기화 확인");

    // 기본 마커 추가
    terminalMarker = addMarker(35.1055, 129.0455, terminalImage); // 기본 터미널 위치
    congestionMarker = addMarker(35.1080, 129.0420, congestionImage); // 혼잡 지역 위치

    // 토글 이벤트 리스너 추가
    initializeToggleListeners();
}

// 마커 표시 함수
function addMarker(lat, lng, iconImage) {
    return new google.maps.Marker({
        position: { lat: lat, lng: lng },
        map: map,
        icon: iconImage // 마커 이미지 사용
    });
}

// 토글 이벤트 리스너 초기화 함수
function initializeToggleListeners() {
    document.getElementById("toggleInfoWaiting").addEventListener("change", async function (event) {
        if (event.target.checked) {
            // 인근 대기지 정보 가져와서 마커 추가
            const waitingData = await fetchWaitingArea();
            waitingData.forEach(area => {
                const marker = addMarker(area.lat, area.lng, terminalImage); // terminalImage 사용
                waitingMarkers.push(marker);
            });
        } else {
            // 마커 제거
            waitingMarkers.forEach(marker => marker.setMap(null));
            waitingMarkers = [];
        }
    });

    document.getElementById("toggleInfoTerminal").addEventListener("change", async function (event) {
        if (event.target.checked) {
            // 터미널 정보 가져와서 마커 추가
            const terminalData = await fetchTerminalArea();
            terminalData.forEach(area => {
                const marker = addMarker(area.lat, area.lng, terminalImage); // terminalImage 사용
                terminalMarkers.push(marker);
            });
        } else {
            // 마커 제거
            terminalMarkers.forEach(marker => marker.setMap(null));
            terminalMarkers = [];
        }
    });

    document.getElementById("toggleInfoCongestion").addEventListener("change", async function (event) {
        if (event.target.checked) {
            // 혼잡 지역 폴리곤 추가
            const congestionData = await fetchCongestionArea();
            congestionData.forEach(area => {
                const polygon = addPolygon(area.coords, "#FF0000"); // 폴리곤 빨간색
                congestionPolygons.push(polygon);
            });
        } else {
            // 폴리곤 제거
            congestionPolygons.forEach(polygon => polygon.setMap(null));
            congestionPolygons = [];
        }
    });
}

// 데이터베이스에서 인근 대기지 데이터를 가져오는 함수
async function fetchWaitingArea() {
    try {
        const response = await axios.get('/api/waitingArea');
        return response.data; // [{lat, lng}, ...]
    } catch (error) {
        console.error('인근 대기지 데이터를 가져오는 중 오류 발생:', error);
        return [];
    }
}

// 데이터베이스에서 터미널 데이터를 가져오는 함수
async function fetchTerminalArea() {
    try {
        const response = await axios.get('/api/terminalArea');
        return response.data; // [{lat, lng}, ...]
    } catch (error) {
        console.error('터미널 데이터를 가져오는 중 오류 발생:', error);
        return [];
    }
}

// 데이터베이스에서 혼잡 지역 데이터를 가져오는 함수
async function fetchCongestionArea() {
    try {
        const response = await axios.get('/api/congestionArea');
        return response.data; // [{coords: [...], ...}]
    } catch (error) {
        console.error('혼잡 지역 데이터를 가져오는 중 오류 발생:', error);
        return [];
    }
}

// 폴리곤 추가 함수
function addPolygon(coords, color) {
    return new google.maps.Polygon({
        paths: coords,
        strokeColor: color,
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: color,
        fillOpacity: 0.35,
        map: map
    });
}

// 데이터베이스에서 항구 좌표 정보를 가져오는 함수
async function fetchPortCoordinates(portCode) {
    try {
        const response = await axios.get(`/api/ports/${portCode}`);
        return response.data; // { lat: ..., lng: ... } 형태의 데이터를 반환
    } catch (error) {
        console.error(`항구 좌표 정보를 가져오는 중 오류 발생: ${error}`);
        return null;
    }
}

// 날씨 정보 업데이트 함수
async function updateWeather(lat, lng) {
    const apiKey = '13c20983e47bfa6ec46e8491cea98b88'; // OpenWeather API 키
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lng}&appid=${apiKey}&units=metric`;

    try {
        const response = await axios.get(url);
        const weatherData = response.data;

        // 날씨 데이터를 HTML에 반영
        document.querySelector('.weather').innerHTML = `${weatherData.main.temp}°C`;
        document.getElementById('weatherIcon').src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;
        document.getElementById('weatherIcon').alt = weatherData.weather[0].description;

    } catch (error) {
        console.error('날씨 정보를 가져오는 중 오류 발생:', error);
    }
}

// 항구 선택이 변경될 때 날씨 정보를 업데이트
document.addEventListener("DOMContentLoaded", function() {
    const portSelect = document.getElementById("portchoose");
    if (portSelect) {
        portSelect.addEventListener("change", async function() {
            const selectedPortCode = portSelect.value;
            const portData = await fetchPortCoordinates(selectedPortCode);

            if (portData) {
                // 포트 좌표를 설정하고 지도를 업데이트
                map.setCenter(new google.maps.LatLng(portData.lat, portData.lng));
                if (terminalMarker) {
                    terminalMarker.setMap(null);
                }
                terminalMarker = addMarker(portData.lat, portData.lng, terminalImage);
            }
        });
    }
});


// 전년도 동일 월 사고 통계 정보를 가져오는 함수
async function fetchAccidentStats(portCode) {
    try {
        const response = await axios.get(`/api/accident-status/${portCode}`);
        return response.data; // { rank1: {...}, rank2: {...}, rank3: {...} } 형태의 데이터
    } catch (error) {
        console.error('사고 통계 데이터를 가져오는 중 오류 발생:', error);
        return null;
    }
}

// 사고 통계를 화면에 표시하는 함수
function displayAccidentStats(stats) {
    if (stats) {
        document.querySelector('.reason1').textContent = stats.rank1.reason;
        document.querySelector('.count1').textContent = `${stats.rank1.count} 회`;

        document.querySelector('.reason2').textContent = stats.rank2.reason;
        document.querySelector('.count2').textContent = `${stats.rank2.count} 회`;

        document.querySelector('.reason3').textContent = stats.rank3.reason;
        document.querySelector('.count3').textContent = `${stats.rank3.count} 회`;
    } else {
        console.error('사고 통계 데이터가 존재하지 않습니다.');
    }
}

// 항구 선택이 변경될 때 사고 통계 정보 업데이트
document.getElementById("portchoose").addEventListener("change", async function () {
    const selectedPortCode = this.value;
    const accidentStats = await fetchAccidentStats(selectedPortCode); // 사고 통계 가져오기
    displayAccidentStats(accidentStats); // 화면에 표시
});
