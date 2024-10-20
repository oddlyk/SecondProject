// 페이지 로드 시 초기 값 설정
document.addEventListener('DOMContentLoaded', function () {
    const currentPath = window.location.pathname;

    // calc/calculation일 때만 자동 계산 실행
    if (currentPath === '/calc/calcdetail') {
        calculation();  // 자동 계산 실행
        saveInitialValues();  // 계산 후 초기 값을 저장
    }
});

// 초기 값을 저장하기 위한 객체
let initialResults = {};

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
    let entry_exit_rate = 135;
    return entry_exit_rate * 2;
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
    document.getElementById('berthing').innerText = Number(berthing_fee.toFixed(1)).toLocaleString();
    document.getElementById('anchorage').innerText = Number(anchorage_fee.toFixed(1)).toLocaleString();
    document.getElementById('mooring').innerText = Number(mooring_fee.toFixed(1)).toLocaleString();
    document.getElementById('entryExit').innerText = Number(entryExitFee.toFixed(1)).toLocaleString();
    document.getElementById('security').innerText = Number(securityFee.toFixed(1)).toLocaleString();
    document.getElementById('totalResult').innerText = Number(total_fee.toFixed(1)).toLocaleString();

    // 각각의 비율 표시
    document.getElementById('berthingRatio').innerText = (berthing_fee / total_fee * 100).toFixed(1);
    document.getElementById('anchorageRatio').innerText = (anchorage_fee / total_fee * 100).toFixed(1);
    document.getElementById('mooringRatio').innerText = (mooring_fee / total_fee * 100).toFixed(1)
    document.getElementById('entryExitRatio').innerText = (entryExitFee / total_fee * 100).toFixed(1);
    document.getElementById('securityRatio').innerText = (securityFee / total_fee * 100).toFixed(1);
};

// 1. 초기화
function resetToDefault() {
    // 초기 값 저장
    const defaultValues = {
        portName: '[[${ portName }]]',
        tonnage: '[[${ tonnage }]]',
        importDate: '[[${ importDate }]]',
        exportDate: '[[${ exportDate }]]',
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


// 3. 초기화 시 기본값을 적용
document.getElementById('resetButton').addEventListener('click', resetToDefault);

// 4. 요금 계산
document.getElementById('calcButton').addEventListener('click', function (event) {
    event.preventDefault();
    calculation();
});

// 저장 버튼
document.getElementById('saveButton').addEventListener('click', function (event) {
    event.preventDefault(); // 기본 폼 제출 방지
    alert("저장하시겠습니까?");
});
