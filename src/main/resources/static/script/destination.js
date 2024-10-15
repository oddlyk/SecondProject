let map;
let terminalMarker, congestionMarker;

window.initMap = function () {
    // 지도 초기화
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 37.5665, lng: 126.9780 },  // 서울 중심 좌표
        zoom: 12
    });

    // 마커 이미지 설정
    const terminalImage = {
        url: '/images/terminal.png',
        scaledSize: new google.map.Size(50, 50)
    };

    const congestionImage = {
        url: '/images/warning.png',
        scaledSize: new google.map.Size(50, 50)
    };

    // 토글 버튼 제어
    const togglewaiting = document.getElementById("toggle-waiting");
    const toggleTerminal = document.getElementById("toggle-terminal");
    const toggleCongestion = document.getElementById("toggle-congestion");

    // 인근 대기지 표시

    // 컨테이너 터미널 마커
    toggleTerminal.addEventListener('change', function () {
        if (this.checked) {
            terminalMarker = new google.map.Marker({
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


// DOMContentLoaded를 통해 지도가 초기화되도록 보장
document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');
});