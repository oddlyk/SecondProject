let map;
let terminalMarker, congestionMarker;
let waitingAreaPolygon;


document.addEventListener('DOMContentLoaded', () => {
  console.log("DOM이 완전히 로드되었습니다.");

  // 포트 선택 요소를 DOM이 로드된 후에 초기화
  const portSelect = document.getElementById("portchoose");
  const portName = document.getElementById("port-name");
  const phoneNum = document.getElementById("phone-num");
  const homepageLink = document.getElementById("homepage-link");

  // Google Maps API 초기화
  window.initMap = async function () {
    const { Map } = await google.maps.importLibrary("maps");

    map = new Map(document.getElementById("map"), {
      center: { lat: 35.1028, lng: 129.0403 }, // 초기 중심 위치
      zoom: 12,
    });

    console.log("initMap 함수 호출됨~~~");
    console.log("Google Maps API가 로드되었나요?~~~", typeof google);
    
    initializeToggleButtons(); // 토글 버튼 초기화
    portSelect.dispatchEvent(new Event('change')); // 기본 포트 선택으로 초기화
  }
});



// 마커 이미지 설정
const terminalImage = {
  url: '/images/terminal.png',
  scaledSize: new google.maps.Size(50, 50),
};

const congestionImage = {
  url: '/images/warning.png',
  scaledSize: new google.maps.Size(50, 50),
};

// 토글 버튼 초기화 함수
function initializeToggleButtons() {
  const toggleTerminal = document.getElementById("toggleInfoTerminal");
  const toggleCongestion = document.getElementById("toggleInfoCongestion");
  
  if (!toggleTerminal) {
      console.error("토글 터미널 요소가 존재하지 않습니다.");
    }

    if (!toggleCongestion) {
      console.error("토글 혼잡 요소가 존재하지 않습니다.");
    }

  // 컨테이너 터미널 마커
  toggleTerminal.addEventListener('change', function () {
    if (this.checked) {
      terminalMarker = new google.maps.Marker({
        position: { lat: 37.5700, lng: 126.9768 }, // 실제 위치로 변경
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
        position: { lat: 37.5600, lng: 126.9810 }, // 실제 위치로 변경
        map: map,
        icon: congestionImage,
      });
    } else {
      if (congestionMarker) congestionMarker.setMap(null);
    }
  });
}

// 포트 선택 요소
const portSelect = document.getElementById("portchoose");
const portName = document.getElementById("port-name");
const phoneNum = document.getElementById("phone-num");
const homepageLink = document.getElementById("homepage-link");

// 포트 좌표 가져오기
function fetchPortData(portCode) {
	
	try {
	    const response = axios.get(`/api/ports/${portCode}`);
		
		alert("===================" + JSON.stringify(response))
		
	    if (!response.data) {
	      console.error(`포트 데이터가 null 또는 undefined 입니다. PortCode: ${portCode}`);
	      return null;
	    }
	    return response.data;
	  } catch (error) {
	    console.error("항구 데이터를 가져오는 중 오류 발생:", error);
	    return null;
	  }
}

// 인근 대기지 데이터 가져오기
async function fetchWaitingAreas() {
  try {
    const response = await axios.get('/api/waiting-areas');
    return response.data;
  } catch (error) {
    console.error("인근 대기지 데이터를 가져오는 중 오류 발생:", error);
    return [];
  }
}

// 인근 대기지 표시
async function showWaitingAreas() {
  const waitingAreas = await fetchWaitingAreas();
  console.log('Waiting Areas:', waitingAreas);

  const polygonCoords = waitingAreas.map(area => ({
    lat: area.port.portLat,
    lng: area.port.portLon,
  }));

  if (waitingAreaPolygon) {
    waitingAreaPolygon.setMap(null);
  }

  waitingAreaPolygon = new google.maps.Polygon({
    paths: polygonCoords,
    strokeColor: '#FF0000',
    strokeOpacity: 0.8,
    strokeWeight: 2,
    fillColor: '#FF0000',
    fillOpacity: 0.35,
  });

  waitingAreaPolygon.setMap(map);
}

// 전년도 동일 월 사고 정보를 가져오는 함수
async function fetchAccidentStatus(portCode) {
	try {
	    const response = await axios.get(`/api/accident-status/${portCode}`);
	    if (!response.data || response.data.length === 0) {
	      console.error(`사고 통계 데이터가 존재하지 않습니다. PortCode: ${portCode}`);
	      return null;
	    }
	    return response.data;
	  } catch (error) {
	    console.error("전년도 동일 월 사고 정보를 가져오는 중 오류 발생:", error);
	    return null;
	  }
}

// 사고 정보를 화면에 표시하는 함수
function displayAccidentStatus(accidentData) {
  const accidentStatusContainer = document.getElementById("accident-status");
  if (!accidentStatusContainer) {
    console.error("accident-status 요소를 찾을 수 없습니다.");
    return;
  }

  accidentStatusContainer.innerHTML = ''; // 기존 내용을 지웁니다.

  if (accidentData && accidentData.length > 0) {
    accidentData.forEach(accident => {
      const accidentItem = document.createElement('li');
      accidentItem.textContent = `${accident.firstRank}: ${accident.firstPer}%`;
      accidentStatusContainer.appendChild(accidentItem);
    });
  } else {
    accidentStatusContainer.innerHTML = '<li>사고 통계 데이터를 불러올 수 없습니다.</li>';
  }
}

// 포트 선택에 따른 데이터 업데이트 함수
portSelect.addEventListener('change', async function () {

  const selectedPort = portSelect.value;


  // 선택된 항구 정보 가져오기 (기존 fetchPortCoordinates -> fetchPortData로 변경)
  const portData = await fetchPortData(selectedPort);

  if (portData) {
    // 항구 이름 및 정보 업데이트
    portName.textContent = portData.portName;
    phoneNum.textContent = portData.portContact;
    homepageLink.setAttribute('href', portData.portUrl);
  }

  // 전년도 동일 월 사고 정보 업데이트 (추가된 부분)
  const accidentData = await fetchAccidentStatus(selectedPort);
  displayAccidentStatus(accidentData); // 사고 정보 화면 업데이트

  // 날씨 정보 업데이트
  await updateWeather(selectedPort);
});

// 날씨 정보 업데이트 함수
async function updateWeather(selectedPort) {
  let latitude, longitude;
  if (selectedPort === "KRPUS") {
    latitude = 35.1028;
    longitude = 129.0403;
  } else if (selectedPort === "KRBNP") {
    latitude = 35.0664;
    longitude = 128.8358;
  } else if (selectedPort === "KRICN") {
    latitude = 37.4483;
    longitude = 126.5975;
  }

  const weatherData = await getWeatherByCoordinates(latitude, longitude);
  if (weatherData) {
    const weatherIcon = document.getElementById("weatherIcon");
    weatherIcon.src = `https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@2x.png`;
    weatherIcon.alt = `${weatherData.weather[0].main}`;
    document.querySelector('.weather').innerHTML = `${weatherData.main.temp}°C`;
  }
}

// 날씨 API로부터 데이터 가져오기
async function getWeatherByCoordinates(latitude, longitude) {
  const url = `https://api.openweathermap.org/data/2.5/weather?lat=${latitude}&lon=${longitude}&appid=13c20983e47bfa6ec46e8491cea98b88&units=metric`; // YOUR_API_KEY를 실제 API 키로 대체
  try {
      const response = await axios.get(url);
      if (!response.data) {
        console.error("날씨 데이터가 null입니다.");
        return null;
      }
      console.log("날씨 데이터:", response.data);
      return response.data;
    } catch (error) {
      console.error('날씨 정보를 가져오는 중 오류가 발생했습니다:', error);
      return null;
    }
}

document.addEventListener('DOMContentLoaded', () => {
  console.log("DOM이 완전히 로드되었습니다.");
  initMap();  // Google Maps 초기화
});



// DOMContentLoaded를 통해 지도가 초기화되도록 보장
// document.addEventListener('DOMContentLoaded', initMap);
