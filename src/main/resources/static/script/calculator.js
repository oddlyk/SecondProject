// 페이지 로드 시 초기 값 설정
document.addEventListener('DOMContentLoaded', function () {
    const currentPath = window.location.pathname;

    // calc/calculation일 때만 자동 계산 실행
    if (currentPath === '/calc/calcdetail') {
        calculation();  // 자동 계산 실행
        saveInitialValues();  // 계산 후 초기 값을 저장
        saveInitialChartValues();  // 계산 후 초기 차트 값을 저장
    }
});

// 초기 값을 저장하기 위한 객체
let initialResults = {};
let initialChartValues = {};

// 계산 결과를 저장하는 함수
function saveInitialValues() {
    initialResults = {
        berthing: document.getElementById('berthing').innerText,
        anchorage: document.getElementById('anchorage').innerText,
        mooring: document.getElementById('mooring').innerText,
        entryExit: document.getElementById('entryExit').innerText,
        security: document.getElementById('security').innerText,
        totalResult: document.getElementById('totalResult').innerText,
        berthingRatio: document.getElementById('berthingRatio').innerText,
        anchorageRatio: document.getElementById('anchorageRatio').innerText,
        mooringRatio: document.getElementById('mooringRatio').innerText,
        entryExitRatio: document.getElementById('entryExitRatio').innerText,
        securityRatio: document.getElementById('securityRatio').innerText
    };
}

// 초기 차트 데이터를 저장하는 함수
function saveInitialChartValues() {
    initialChartValues = {
        berthing: Number(document.getElementById('berthing').innerText.replace(/,/g, '')),
        anchorage: Number(document.getElementById('anchorage').innerText.replace(/,/g, '')),
        mooring: Number(document.getElementById('mooring').innerText.replace(/,/g, '')),
        entryExit: Number(document.getElementById('entryExit').innerText.replace(/,/g, '')),
        security: Number(document.getElementById('security').innerText.replace(/,/g, ''))
    };

    // 초기 차트 그리기
    createChart(
        initialChartValues.berthing,
        initialChartValues.anchorage,
        initialChartValues.mooring,
        initialChartValues.entryExit,
        initialChartValues.security
    );
}


// 1. 계산 함수 정의
function calculate_berthing_fee() {
    let tonnage = document.getElementById('tonnage').value;
    let hours = document.getElementById('workingHour').value;
    let base_rate = 358;
    let extra_rate = 29.9;

    // 톤수를 10톤 단위 올림 처리
    let tonnage_ceil = Math.ceil(tonnage / 10);

    // 기본 요금 (12시간 기준)
    let base_fee = tonnage_ceil * base_rate;

    // 12시간 초과 할증료
    let extra_fee = 0;

    if (hours > 12) {
        let extra_hours = Math.ceil(hours - 12)  // 초과 시간 올림 처리
        extra_fee = tonnage_ceil * extra_hours * extra_rate;
    }

    let total_fee = base_fee + extra_fee

    return total_fee;
};

function calculate_anchorage_fee() {
    let tonnage = document.getElementById('tonnage').value;
    let hours = document.getElementById('waitingHour').value;
    let base_rate = 187;
    let extra_rate = 15.7;

    // 외해 정박 체크 확인
    let isChecked = document.getElementById('openSea').checked;

    if (isChecked) {
        return 0;
    }

    // 톤수를 10톤 단위 올림 처리
    let tonnage_ceil = Math.ceil(tonnage / 10);

    // 기본 요금 (12시간 기준)
    let base_fee = tonnage_ceil * base_rate;

    // 12시간 초과 할증료
    let extra_fee = 0;

    if (hours > 12) {
        let extra_hours = Math.ceil(hours - 12)  // 초과 시간 올림 처리
        extra_fee = tonnage_ceil * extra_hours * extra_rate;
    }

    let total_fee = base_fee + extra_fee;

    return total_fee;
};

function calculate_mooring_fee() {
    let tonnage = document.getElementById('tonnage').value;
    let hours = document.getElementById('workingHour').value;
    let base_mooring_rate = 28.5;
    let min_days = 15;

    // 시간을 일로 변환
    let days = hours / 24;

    // 15일 이상 정박했는지 확인
    if (days < min_days) return 0;

    // 계선료 계산 (12시간 기준)
    let base_fee = (tonnage / 10) * (hours / 12) * base_mooring_rate;

    return base_fee;
};

function entry_exit_fee() {
    let tonnage = document.getElementById('tonnage').value;
    let entry_exit_rate = 135;
    let entry_exit_fee = tonnage * entry_exit_rate * 2;
    return entry_exit_fee;
}

function security_fee() {
    let tonnage = document.getElementById('tonnage').value;
    let security_rate = 3;

    return tonnage * security_rate;
}



function calculation() {
    let tonnage = parseFloat(document.getElementById('tonnage').value) || 0;
    let workingHour = parseFloat(document.getElementById('workingHour').value) || 0;
    let workingMinute = parseFloat(document.getElementById('workingMinute').value) || 0;
    let waitingHour = parseFloat(document.getElementById('waitingHour').value) || 0;
    let waitingMinute = parseFloat(document.getElementById('waitingMinute').value) || 0;

    let berthing_fee = calculate_berthing_fee();
    let anchorage_fee = calculate_anchorage_fee();
    let mooring_fee = calculate_mooring_fee();
    let entryExitFee = entry_exit_fee();
    let securityFee = security_fee();

    let total_fee = berthing_fee + anchorage_fee + mooring_fee + entryExitFee + securityFee;

    // 각각의 금액 표시
    document.getElementById('berthing').innerText = Number(berthing_fee.toFixed(0)).toLocaleString();
    document.getElementById('anchorage').innerText = Number(anchorage_fee.toFixed(0)).toLocaleString();
    document.getElementById('mooring').innerText = Number(mooring_fee.toFixed(0)).toLocaleString();
    document.getElementById('entryExit').innerText = Number(entryExitFee.toFixed(0)).toLocaleString();
    document.getElementById('security').innerText = Number(securityFee.toFixed(0)).toLocaleString();
    document.getElementById('totalResult').innerText = Number(total_fee.toFixed(0)).toLocaleString();

    // 각각의 비율 표시
    document.getElementById('berthingRatio').innerText = (berthing_fee / total_fee * 100).toFixed(1);
    document.getElementById('anchorageRatio').innerText = (anchorage_fee / total_fee * 100).toFixed(1);
    document.getElementById('mooringRatio').innerText = (mooring_fee / total_fee * 100).toFixed(1)
    document.getElementById('entryExitRatio').innerText = (entryExitFee / total_fee * 100).toFixed(1);
    document.getElementById('securityRatio').innerText = (securityFee / total_fee * 100).toFixed(1);

    // 차트 업데이트
    createChart(berthing_fee, anchorage_fee, mooring_fee, entryExitFee, securityFee);

};

// 차트 업데이트
// 차트 생성 함수
function createChart(berthing, anchorage, mooring, entryExit, security) {
    // 모든 금액의 총합을 계산
    const totalFees = berthing + anchorage + mooring + entryExit + security;

    // 각각의 금액을 총합에서 차지하는 비율로 계산
    const berthingRatio = (berthing / totalFees) * 100;
    const anchorageRatio = (anchorage / totalFees) * 100;
    const mooringRatio = (mooring / totalFees) * 100;
    const entryExitRatio = (entryExit / totalFees) * 100;
    const securityRatio = (security / totalFees) * 100;

    // 차트가 이미 생성되어 있으면 먼저 삭제
    if (window.myChart) {
        window.myChart.destroy();
    }

    // Chart.js를 사용하여 horizontal stacked bar 차트 생성
    const ctx = document.getElementById('bar').getContext('2d');
    window.myChart = new Chart(ctx, {
        type: 'bar', // 차트 타입: 막대 그래프
        data: {
            labels: ['총 금액'], // 가로 막대 하나만 표시할 것이므로 label은 '총 금액'으로 설정
            datasets: [{
                label: '입출항료',
                data: [entryExitRatio], // 비율로 표현
                backgroundColor: '#F8CB8A',
                borderColor: '#F8CB8A',
                borderWidth: 1,
                borderRadius: 50 // 막대에 border-radius 추가
            }, {
                label: '접안료',
                data: [berthingRatio],
                backgroundColor: '#B4E3B3',
                borderColor: '#B4E3B3',
                borderWidth: 1,
                borderRadius: 50
            }, {
                label: '정박료',
                data: [anchorageRatio],
                backgroundColor: '#82A4F9',
                borderColor: '#82A4F9',
                borderWidth: 1,
                borderRadius: 50
            }, {
                label: '계선료',
                data: [mooringRatio],
                backgroundColor: '#F98284',
                borderColor: '#F98284',
                borderWidth: 1,
                borderRadius: 50
            }, {
                label: '보안료',
                data: [securityRatio],
                backgroundColor: '#D582F9',
                borderColor: '#D582F9',
                borderWidth: 1,
                borderRadius: 50
            }]
        },
        options: {
            responsive: false, // 크기 고정
            indexAxis: 'y', // 가로형 스택 차트를 위해 y축으로 설정
            plugins: {
                legend: {
                    display: false // 범례 숨김
                },
                tooltip: {
                    enabled: false // 툴팁 비활성화
                }
            },
            scales: {
                x: {
                    display: false, // X축 숨김 (눈금선 제거)
                    stacked: true
                },
                y: {
                    display: false, // Y축 숨김 (눈금선 제거)
                    stacked: true
                }
            }
        }
    });

    // 캔버스 크기 조정
    document.getElementById('bar').style.width = '350px';
    document.getElementById('bar').style.height = '15px';
}


// 1. 초기화
function resetToDefault() {

    // 초기 값 저장
    const defaultValues = {
        portName: '[[${ portName }]]',
        tonnage: '[[${ tonnage }]]',
        importDate: document.getElementById('importDate').value,
        exportDate: document.getElementById('exportDate').value,
        workingHour: parseInt(calcForm.getAttribute('data-working-hour'), 10),
        workingMinute: parseInt(calcForm.getAttribute('data-working-minute'), 10),
        waitingHour: parseInt(calcForm.getAttribute('data-waiting-hour'), 10),
        waitingMinute: parseInt(calcForm.getAttribute('data-waiting-minute'), 10),
        shipName: '[[${ shipName }]]',
        callSign: '[[${ callSign }]]',
    };

    document.getElementById('tonnage').value = parseFloat(defaultValues.tonnage) || 0;
    document.getElementById('workingHour').value = parseInt(defaultValues.workingHour, 10) || 0;
    document.getElementById('workingMinute').value = parseInt(defaultValues.workingMinute, 10) || 0;
    document.getElementById('waitingHour').value = parseInt(defaultValues.waitingHour, 10) || 0;
    document.getElementById('waitingMinute').value = parseInt(defaultValues.waitingMinute, 10) || 0;

    // 날짜 값이 유효한지 확인하고 처리
    if (defaultValues.importDate && !isNaN(new Date(defaultValues.importDate))) {
        document.getElementById('importDate').value = defaultValues.importDate;
    } else {
        document.getElementById('importDate').value = ''; // 기본값 설정
    }

    if (defaultValues.exportDate && !isNaN(new Date(defaultValues.exportDate))) {
        document.getElementById('exportDate').value = defaultValues.exportDate;
        userEnteredExportDate = new Date(defaultValues.exportDate);
    } else {
        document.getElementById('exportDate').value = ''; // 기본값 설정
    }

    const currentPath = window.location.pathname;
    if (currentPath === '/calc/calcdetail') {
        // 저장된 초기 계산 결과 복원
        document.getElementById('berthing').innerText = initialResults.berthing;
        document.getElementById('anchorage').innerText = initialResults.anchorage;
        document.getElementById('mooring').innerText = initialResults.mooring;
        document.getElementById('entryExit').innerText = initialResults.entryExit;
        document.getElementById('security').innerText = initialResults.security;
        document.getElementById('totalResult').innerText = initialResults.totalResult;

        document.getElementById('berthingRatio').innerText = initialResults.berthingRatio;
        document.getElementById('anchorageRatio').innerText = initialResults.anchorageRatio;
        document.getElementById('mooringRatio').innerText = initialResults.mooringRatio;
        document.getElementById('entryExitRatio').innerText = initialResults.entryExitRatio;
        document.getElementById('securityRatio').innerText = initialResults.securityRatio;

        // 차트도 초기화
        // 초기 차트 값으로 복원
        createChart(
            initialChartValues.berthing,
            initialChartValues.anchorage,
            initialChartValues.mooring,
            initialChartValues.entryExit,
            initialChartValues.security
        );
    } else {

        // 계산 값 초기화
        document.getElementById('berthing').innerText = '-';
        document.getElementById('anchorage').innerText = '-';
        document.getElementById('mooring').innerText = '-';
        document.getElementById('entryExit').innerText = '-';
        document.getElementById('security').innerText = '-';
        document.getElementById('totalResult').innerText = '-';

        // 비율 값 초기화
        document.getElementById('berthingRatio').innerText = '-';
        document.getElementById('anchorageRatio').innerText = '-';
        document.getElementById('mooringRatio').innerText = '-';
        document.getElementById('entryExitRatio').innerText = '-';
        document.getElementById('securityRatio').innerText = '-';
    }
};




// 작업 시간 또는 대기 시간 변경 시 제한 조건 확인
document.getElementById('workingHour').addEventListener('input', checkTimeConstraint);
document.getElementById('workingMinute').addEventListener('input', checkTimeConstraint);
document.getElementById('waitingHour').addEventListener('input', checkTimeConstraint);
document.getElementById('waitingMinute').addEventListener('input', checkTimeConstraint);

// 출항 일시를 저장하는 변수 (초기에는 기본 출항 일시로 설정)
let userEnteredExportDate = new Date(document.getElementById('exportDate').value);

// 사용자가 출항 일시를 수정할 때마다 userEnteredExportDate를 업데이트
document.getElementById('exportDateDisplay').addEventListener('input', function () {
    const updatedDate = new Date(document.getElementById('exportDateDisplay').value);
    if (!isNaN(updatedDate)) {
        userEnteredExportDate = updatedDate; // 수정된 값이 유효한 날짜일 경우만 업데이트
    }
});

// 작업 시간, 대기 시간 수정 시 출항 일시와 입항 일시 사이의 값을 넘지 않도록 하는 함수
function checkTimeConstraint() {
    const importDate = new Date(document.getElementById('importDate').value); // 입항 일시 (hidden 필드)

    const workingHour = parseFloat(document.getElementById('workingHour').value) || 0;
    const workingMinute = parseFloat(document.getElementById('workingMinute').value) || 0;
    const waitingHour = parseFloat(document.getElementById('waitingHour').value) || 0;
    const waitingMinute = parseFloat(document.getElementById('waitingMinute').value) || 0;

    // 작업 시간과 대기 시간의 총합 (분 단위)
    const totalRequiredMinutes = (workingHour * 60 + workingMinute) + (waitingHour * 60 + waitingMinute);

    // 입항 일시와 사용자가 수정한 출항 일시 간의 차이를 분 단위로 계산
    const totalAvailableMinutes = (userEnteredExportDate - importDate) / (1000 * 60); // 밀리초를 분으로 변환

    // 값 출력해 보기 (디버깅용)
    console.log('총 필요한 시간 (분):', totalRequiredMinutes);
    console.log('총 가능한 시간 (분):', totalAvailableMinutes);
    console.log('입항 일시:', importDate);
    console.log('사용자 입력 출항 일시:', userEnteredExportDate);

    // 조건 체크
    if (totalRequiredMinutes > totalAvailableMinutes) {
        alert('작업 시간과 대기 시간의 합이 입항 일시와 출항 예정 일시 사이의 시간을 초과합니다. 출항 예정 일시를 바꿔 주세요.');
        return false;
    }

    return true;
}




// 3. 초기화 시 기본값을 적용
document.getElementById('resetButton').addEventListener('click', resetToDefault);


// 4. 요금 계산
document.getElementById('calcButton').addEventListener('click', function (event) {
    event.preventDefault();
    if (checkTimeConstraint()) {
        calculation();
    } else {
        // 오류 처리
        alert('계산에 실패했습니다. 시간을 확인해주세요.');
    }
});


// 저장 버튼
document.getElementById('saveButton').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 폼 제출 방지
    if (checkTimeConstraint()) {
        alert('저장하였습니다.');
    } else {
        // 오류 처리
        alert('저장에 실패했습니다. 시간을 확인해주세요.');
    }
});