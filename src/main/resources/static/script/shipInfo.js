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


// DOMContentLoaded를 통해 지도가 초기화되도록 보장
document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');
});