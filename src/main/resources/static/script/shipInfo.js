// DOMContentLoaded를 통해 지도가 초기화되도록 보장
document.addEventListener('DOMContentLoaded', function () {
    console.log('페이지가 로드되었습니다.');

    let voyagePer = document.getElementById('voyagePer').textContent;
    if (voyagePer == 0) {
		document.getElementById('shipPerInfo').innerText = "※아직 시작하지 않은 항해입니다.";
    }
    if (voyagePer == 100) {
		document.getElementById('shipPerInfo').innerText = "※목적항에 도착하여 종료된 항해입니다.";
    }

});
//항해 출발일도착일의 시간을 버림
document.getElementById('depTime').innerText = document.getElementById('depTime').textContent.split('T')[0];
document.getElementById('arrTime').innerText = document.getElementById('arrTime').textContent.split('T')[0];