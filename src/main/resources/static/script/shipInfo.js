
/*
let map;

window.initMap = function () {
    // 지도 초기화
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.5665, lng: 126.9780 },  // 서울 중심 좌표
        zoom: 12
    });

    // 마커 이미지 변경
    const markerImage = {
        url: '/static/images/vessel.png', // 마커로 사용할 이미지 경로
        scaledSize: new google.maps.Size(50, 50), // 마커 크기 조절
    };

    // 마커 추가
    const marker = new google.maps.Marker({
        position: { lat: 37.5665, lng: 126.9780 },
        map: map,
        icon: markerImage,  // 마커 이미지 설정
        title: "클릭하세요!"
    });

    // 팝업에 들어갈 HTML 내용
    const popupContent = `
        <div class="popup">
            <div class="Weather">현재 날씨 <img src="" alt="날씨 그림"></div>
            <div class="temparature">n<div class="celsius">°C</div></div>
            <div class="wind">n<div class="m/s">m/s</div></div>
            <div class="speed">속도: <div class="ship-speed">n<div class="k/n">k/n</div></div></div>
            <div class="Line2" style="width: 0px; height: 50px; border: 2px #B1AEAE solid"></div>
            <div class="direction">이동 방향: <div class="ship-direct">n <div class="angle">º</div></div></div>
        </div>`;


    // 정보 창 추가
    const infoWindow = new google.maps.InfoWindow({
        content: popupContent
    });

    // 마커 클릭 이벤트 추가
    marker.addListener("click", () => {
        infoWindow.open(map, marker);
    });

    // 줌 인, 아웃 버튼 추가
    document.getElementById('zoom-in').addEventListener('click', function () {
        map.setZoom(map.getZoom() + 1);  // 지도 확대
    });

    document.getElementById('zoom-out').addEventListener('click', function () {
        map.setZoom(map.getZoom() - 1);  // 지도 축소
    });

}
*/
/*
function initMap() {
            // 지도 중심 좌표 설정 (부산항 예시)
            const center = { lat: 35.1028, lng: 129.0403 };
            const map = new google.maps.Map(document.getElementById("map"), {
                zoom: 10,
                center: center,
            });

            // 마커 추가
            const marker = new google.maps.Marker({
                position: center,
                map: map,
                title: "부산항",
            });

            // 여러 좌표를 선으로 연결하기
            const coordinates = [
                { lat: 35.1028, lng: 129.0403 }, // 부산항
                { lat: 35.0664, lng: 128.8358 }, // 부산신항
                { lat: 37.4483, lng: 126.5975 }, // 인천항
            ];

            const flightPath = new google.maps.Polyline({
                path: coordinates,
                geodesic: true,
                strokeColor: '#FF0000',
                strokeOpacity: 1.0,
                strokeWeight: 2,
            });

            flightPath.setMap(map);
        }
 */

// DOMContentLoaded를 통해 지도가 초기화되도록 보장
document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');
    let weatherIcon = document.getElementById('weatherIcon');
    weatherIcon.src = 'https://openweathermap.org/img/wn/01d@2x.png';

    // 현위치 좌표
    let GetnowLoc = document.getElementById('GetnowLoc').value;
    if (GetnowLoc != "") {
        getWeather(GetnowLoc);
    }

    let voyagePer = document.getElementById('voyagePer').textContent;
    if (voyagePer == 0) {
        alert("아직 시작하지 않은 항해입니다.");
    }
    if (voyagePer == 100) {
        alert("목적항에 도착하여 종료된 항해입니다.");
    }

});
//항해 출발일도착일의 시간을 버림
document.getElementById('depTime').innerText = document.getElementById('depTime').textContent.split('T')[0];
document.getElementById('arrTime').innerText = document.getElementById('arrTime').textContent.split('T')[0];


// 날씨 데이터를 가져오는 비동기 함수
async function getWeather(GetnowLoc) {
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
        document.getElementById('tempa').innerText = weatherData.main.temp;
        let weatherIcon = document.getElementById('weatherIcon');
        weatherIcon.src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;
        weatherIcon.alt = `${weatherData.weather[0].main}`;
        document.getElementById('windSpeed').innerText = weatherData.wind.speed;


    } catch (error) {
        console.error('날씨 정보를 가져오는 중 오류가 발생했습니다:', error);
    }
}