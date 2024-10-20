function changeFavorite(vNumber) {
    // 기존에 다른 즐겨찾기가 설정되어 있는지 확인하기 위해 서버에 요청
    fetch(`/user/favorite/status?vNumber=${vNumber}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            console.log(data); // 서버로부터의 응답을 로그에 찍어서 확인
            const { alreadyFavorite, hasTopFavorite } = data;

            if (alreadyFavorite) {
                // 이미 현재 항해가 즐겨찾기로 등록되어 있으면 바로 변경 요청을 보내지 않음
                alert('이 항해는 이미 즐겨찾기로 등록되어 있습니다.');
                window.location.reload();
                return;
            }

            if (hasTopFavorite) {
                // 이미 다른 항해가 topFavorite으로 설정되어 있는 경우
                if (confirm('이미 즐겨찾기로 등록된 항해가 있습니다. 변경하시겠습니까?')) {
                    // 사용자가 확인을 누른 경우에만 즐겨찾기 변경 요청 전송
                    updateFavorite(vNumber);
                } else {
                    window.location.reload();
                }
            } else {
                // 즐겨찾기가 설정되지 않은 경우 바로 즐겨찾기 변경 요청 전송
                updateFavorite(vNumber);
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}



function updateFavorite(vNumber) {
    // 즐겨찾기 변경 요청 전송
    fetch(`/user/favorite?vNumber=${vNumber}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            'vNumber': vNumber
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('즐겨찾기가 변경되었습니다.');

                // UI 업데이트: 기존 즐겨찾기 해제 및 새로운 즐겨찾기 설정
                const allFavoriteCheckboxes = document.querySelectorAll('input[name="favorite"]');

                // 기존의 topFavorite이 설정된 항목을 해제
                allFavoriteCheckboxes.forEach(checkbox => {
                    if (checkbox.value !== vNumber.toString() && checkbox.checked) {
                        checkbox.checked = false;  // 기존 즐겨찾기 해제
                    }
                });

                // 새로운 항목 체크
                const currentCheckbox = document.querySelector(`input[name="favorite"][value="${vNumber}"]`);
                if (currentCheckbox) {
                    currentCheckbox.checked = true;  // 새로 선택한 항해를 즐겨찾기로 설정
                }

                // 변경 사항 반영을 위해 페이지 새로고침
                window.location.reload();

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