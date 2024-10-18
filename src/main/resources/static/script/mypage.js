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

function confirmDelete() {
    if (confirm("선택한 항목을 삭제하시겠습니까?")) {
        document.getElementById("delete-form").submit();
    }
}