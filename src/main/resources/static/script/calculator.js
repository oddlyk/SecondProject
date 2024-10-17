// 1. 초기화
document.addEventListener('DOMContentLoaded', function () {
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
        callSign: '[[${ callSign }]]'
    };

    function resetToDefault() {
        document.getElementById('tonnage').value = defaultValues.tonnage;
        document.getElementById('workingHour').value = defaultValues.workingHour || 0;
        document.getElementById('workingMinute').value = defaultValues.workingMinute || 0;
        document.getElementById('waitingHour').value = defaultValues.waitingHour || 0;
        document.getElementById('waitingMinute').value = defaultValues.waitingMinute || 0;

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

    // 초기화 버튼 클릭 시 기본값으로 설정
    document.getElementById('resetButton').addEventListener('click', resetToDefault);
})

// 2. 저장 버튼 활성화/비활성화
document.addEventListener('DOMContentLoaded', function () {
    // 저장 버튼 활성화 여부 확인 및 설정
    const saveButton = document.getElementById('saveButton');
    const isSaveEnabled = saveButton.getAttribute('disabled') === null;

    if (!isSaveEnabled) {
        saveButton.disabled = true;  // 저장 버튼 비활성화
    } else {
        saveButton.disabled = false; // 저장 버튼 활성화
    }
});

// 저장 버튼 클릭 시 경고창 띄우기
document.getElementById('saveButton').addEventListener('click', function (event) {
    alert("저장하시겠습니까?");
});




// 3. 요금 계산
document.getElementById('calcButton').addEventListener('click', function (event) {

    event.preventDefault(); // 기본 폼 제출 방지
    calculation();

});

function calculation() {
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
    }

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
    }

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
    }

    function entry_exit_fee() {
        let entry_exit_rate = 135;
        return entry_exit_rate * 2;
    }

    function security_fee() {
        let tonnage = document.getElementById('tonnage').value;
        let security_rate = 3;

        return tonnage * security_rate;
    }

    function all_fee_calc() {
        let berthing_fee = calculate_berthing_fee();
        let anchorage_fee = calculate_anchorage_fee();
        let mooring_fee = calculate_mooring_fee();
        let entryExitFee = entry_exit_fee();
        let securityFee = security_fee();

        let total_fee = berthing_fee + anchorage_fee + mooring_fee + entryExitFee + securityFee;

        // 각각의 금액 표시
        document.getElementById('berthing').innerText = berthing_fee.toFixed(1);
        document.getElementById('anchorage').innerText = anchorage_fee.toFixed(1);
        document.getElementById('mooring').innerText = mooring_fee.toFixed(1);
        document.getElementById('entryExit').innerText = entryExitFee.toFixed(1);
        document.getElementById('security').innerText = securityFee.toFixed(1);
        document.getElementById('totalResult').innerText = total_fee.toFixed(1);

        // 각각의 비율 표시
        document.getElementById('berthingRatio').innerText = (berthing_fee / total_fee * 100).toFixed(1);
        document.getElementById('anchorageRatio').innerText = (anchorage_fee / total_fee * 100).toFixed(1);
        document.getElementById('mooringRatio').innerText = (mooring_fee / total_fee * 100).toFixed(1)
        document.getElementById('entryExitRatio').innerText = (entryExitFee / total_fee * 100).toFixed(1);
        document.getElementById('securityRatio').innerText = (securityFee / total_fee * 100).toFixed(1);
    }

    all_fee_calc();
}