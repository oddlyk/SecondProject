let map;
let terminalMarker, congestionMarker;
let waitingAreaPolygon;

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
	const toggleTerminal = document.getElementById("toggle-terminal");
	const toggleCongestion = document.getElementById("toggle-congestion");
	
	// 컨테이너 터미널 마커
	toggleTerminal.addEventListener('change', function () {
		if (this.checked) {
			terminalMarker = new google.maps.Marker({
				position: { lat : 37.5700, lng : 126.9768 },
				map: map,
				icon: terminalImage,
			});
		} else {
			if (terminalMarker) terminalMarker.setMap(null);
		}
	});
	
	// 혼잡 주의 지역 마커
	toggleCongestion.addEventListener('change', function () {
		if (this.checked) {
			congestionMarker = new google.maps.Marker({
				position: { lat : 37.5600, lng : 126.9810},
				map: map,
				icon: congestionImage,
			});
		} else {
			if (congestionMarker) congestionMarker.setMap(null);
		}
	});
};

