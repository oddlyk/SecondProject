// 1. 초기화
document.addEventListener('DOMContentLoaded', function () {

    function resetToDefault() {
        document.getElementById('tonnage').value = defaultValues.tonnage;
        document.getElementById('importDate').value = defaultValues.importDate;
        document.getElementById('exportDate').value = defaultValues.exportDate;
        document.getElementById('workingHour').value = defaultValues.workingHour;
        document.getElementById('workingMinute').value = defaultValues.workingMinute;
        document.getElementById('waitingHour').value = defaultValues.waitingHour;
        document.getElementById('waitingMinute').value = defaultValues.waitingMinute;
    }

    // 초기화 버튼 클릭 시 기본값으로 설정
    document.getElementById('resetButton').addEventListener('click', resetToDefault);
})

// 2. 저장 버튼 활성화/비활성화



// 3. 요금 계산

// 시와 분 결합해 시간 계산
document.querySelector("#calcForm").addEventListener("submit", function (event) {
    const workingHour = document.getElementById("workingHour").value;
    const workingMinute = document.getElementById("workingMinute").value;
    const waitingHour = document.getElementById("waitingHour").value;
    const waitingMinute = document.getElementById("waitingMinute").value;

    // 여기서 시와 분을 조합해서 사용할 수 있습니다.
    const workingTime = `${workingHour.padStart(2, '0')}:${workingMinute.padStart(2, '0')}`;
    const waitingTime = `${waitingHour.padStart(2, '0')}:${waitingMinute.padStart(2, '0')}`;

    console.log("작업 시간:", workingTime);
    console.log("대기 시간:", waitingTime);
});


// 4. 외해 체크박스 클릭 시 계산