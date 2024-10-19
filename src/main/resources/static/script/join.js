/**
 * 회원가입처리
 */
let notExistId = false;

$("#idCheck").on('click', idCheck);
$('#userId').on('keyup', function () {
    notExistId = false;
    $('#idInfo').text("아이디 중복 여부를 확인해 주세요").css("color", "red");
})


function idCheck() {
    alert($('#userId').val());
    $.ajax({
        url: "/user/idCheck",
        method: "GET",
        data: { "userid": $('#userId').val() },
        success: function (resp) {
            if (resp === "OK") {
                $('#idInfo').text("사용가능한 아이디입니다.").css("color", "blue");
                notExistId = true;
            } else {
                $('#idInfo').text("사용 불가능").css("color", "red");
                notExistId = false;
            }

        },
        error: function (resp) {
            $('#idInfo').text("사용 불가능").css("color", "red");
            notExistId = false;
        }
    });
}