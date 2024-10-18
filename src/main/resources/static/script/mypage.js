function changeFavorite(vNumber) {
    // Checkbox를 체크한 경우 즐겨찾기로 등록/변경 요청 전송
    fetch(`/user/favorite?vNumber=${vNumber}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('즐겨찾기가 변경되었습니다.');
            } else {
                alert('즐겨찾기 변경에 실패하였습니다.');
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}


// 전체 선택
document.addEventListener('DOMContentLoaded', function () {
    // 전체 선택 체크박스
    const chooseAllCheckbox = document.getElementById('choose-all');
    const chooseOneCheckboxes = document.querySelectorAll('.choose-one');

    // 전체 선택 체크박스 클릭 시 개별 체크박스 상태 변경
    chooseAllCheckbox.addEventListener('change', function () {
        chooseOneCheckboxes.forEach(checkbox => {
            checkbox.checked = chooseAllCheckbox.checked;
        });
    });

    // 개별 체크박스 상태에 따라 전체 선택 체크박스 상태 변경
    chooseOneCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            if ([...chooseOneCheckboxes].every(cb => cb.checked)) {
                chooseAllCheckbox.checked = true;
            } else {
                chooseAllCheckbox.checked = false;
            }
        });
    });
});

// 선택 항목 삭제
function confirmDelete() {
    const checkedBoxes = document.querySelectorAll('.choose-one:checked');
    if (checkedBoxes.length > 0) {
        // 삭제 확인 경고창
        const isConfirmed = confirm('삭제하시겠습니까?');
        if (isConfirmed) {
            const deleteForm = document.getElementById('delete-form');

            // 선택된 체크박스들만 유지하도록 처리
            checkedBoxes.forEach(checkbox => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'vNumbers';
                input.value = checkbox.value;
                deleteForm.appendChild(input);
            });

            deleteForm.submit();
        }
    } else {
        // 항목을 선택하지 않았을 때만 경고창 표시
        alert('삭제할 항목을 선택해주세요.');
    }
}