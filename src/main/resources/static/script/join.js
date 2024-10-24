/**
 * 회원가입처리
 */
let notExistId = false;
let pwdCheck = false;


// 아이디 중복 확인
$("#idCheck").on('click',
    function () {
        $.ajax({
            url: "/user/idCheck",
            method: "GET",
            data: { "userid": $('#userId').val() },
            success: function (resp) {
                if (resp === "OK") {
                    $('#idInfo').text("사용가능한 아이디입니다.").css("color", "blue");
                    notExistId = true;
                } else {
                    $('#idInfo').text("이미 존재하는 아이디입니다.").css("color", "red");
                    notExistId = false;
                }

            },
            error: function (resp) {
                $('#idInfo').text("회원가입이 불가능합니다. 기업과 연락을 취해주세요.").css("color", "red");
                notExistId = false;
            }
        });
    });
$('#userId').on('keyup', function () {
    notExistId = false;
    $('#idInfo').text("아이디 중복 여부를 확인해 주세요").css("color", "red");
})

//비밀번호 확인 기능
$('#userPwd').on('keyup', function () {
    const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!<>?^*()])|(?=.*[a-zA-Z])(?=.*[0-9])|(?=.*[a-zA-Z])(?=.*[!<>?^*()])/;
    if (!regex.test($('#userPwd').val().trim()) || $('#userPwd').val().trim().length < 7 || $('#userPwd').val().trim().length > 20) {
        $('#pwdRole').text('사용 불가능').css("color", "red");
    } else {
        $('#pwdRole').text('사용 가능').css("color", "blue");
    }
    $('#pwdCheck').prop('disabled', false);
    $('#pwdCheck').val("");
    $('#pwdInfo').text('비밀번호는 비밀번호 확인 값과 동일해야 합니다.').css("color", "red");
    pwdCheck = false;
})
$('#pwdCheck').on('keyup', function () {
    if ($('#userPwd').val().trim() != $('#pwdCheck').val().trim()) {
        $('#pwdInfo').text('비밀번호는 비밀번호 확인 값과 동일해야 합니다.').css("color", "red");
        pwdCheck = false;
    } else {
        $('#pwdInfo').text('비밀번호 확인이 완료되었습니다.').css("color", "blue");
        $('#pwdCheck').prop('disabled', true);
        pwdCheck = true;
    }
})

//이메일 뒷부분 상태
$("#emailchoose").on('change', function () {
    if ($("#emailchoose").val() === "직접 입력") {
        $("#emailB").prop('disabled', false);
        $("#emailB").val("");
        $("#emailB").select();
    } else {
        $('#emailB').prop('disabled', true);
        $('#emailB').val($("#emailchoose").val());
    }
});

// 가입 버튼 클릭 시
$("#submitBtn").on('click', function () {

    //이름 길이 체크 (30자 이내)
    let username = $('#username');
    if (username.val().trim().length < 3 || username.val().trim().length > 20) {
        alert("이름은 20자 이내로 작성해주세요.");
        username.select();
        return;
    }

    // 아이디 길이 체크 (5~20)
    let userid = $('#userId');
    if (userid.val().trim().length < 5 || userid.val().trim().length > 20) {
        alert("아이디는 영문, 숫자 포함 5~20글자로 입력해 주세요.");
        userid.select();
        return;
    }
    if (!notExistId) {
        alert("아이디 중복을 확인해 주세요.");
        userid.select();
        return;
    }

    // 비밀번호 길이 체크 (7~20, 영어 숫자 특수문자)
    let userpwd = $('#userPwd');
    if (userpwd.val().trim().length < 7 || userpwd.val().trim().length > 20) {
        alert("비밀번호는 영문, 숫자, 특수문자(<>.?!^*()) 중 2가지 이상 혼합하여 7~20 글자로 입력해 주세요.");
        userpwd.select();
        return;
    }
    if (!pwdCheck) {
        alert("비밀번호를 재확인해주세요.");
        $('#pwdCheck').select();
        return;
    }
    const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!<>?^*()])|(?=.*[a-zA-Z])(?=.*[0-9])|(?=.*[a-zA-Z])(?=.*[!<>?^*()])/;
    if (!regex.test(userpwd.val().trim())) {
        alert('비밀번호는 영문, 숫자, 특수문자(<>.?!^*()) 중 2가지 이상 혼합하여 7~20 글자로 입력해 주세요.');
        userpwd.select();
        return;
    }


    //전화번호 숫자만
    let phone = $('#phone');
    if (isNaN(phone.val().trim()) || phone.val().trim().length < 9 || phone.val().trim().length > 12) {//문자열이 숫자로 변환될 수 있는 경우에만 false
        alert("전화번호는 숫자만 입력하여 바르게 입력해 주세요.");
        phone.select();
        return;
    }

    // 이메일 조합 emailF emailchoose emailB
    let emailF = $('#emailF');
    if (emailF.val().trim().length == 0 || /@/.test(emailF.val().trim())) {
        alert("이메일의 @ 이전부분을 바르게 입력해 주세요.")
        emailF.select();
        return;
    }
    let emailB = $('#emailB');
    if (emailB.val().trim().length == 0 || !/./.test(emailB.val().trim())) {
        alert("이메일의 뒷주소를 바르게 입력해 주세요.");
        emailB.select();
        return;
    }
    let email = emailF.val() + '@' + emailB.val();
    $('#email').val(email);
    $.ajax({
        url: "/user/emailCheck",
        method: "GET",
        data: { "email": email },
        success: function (resp) {
            if (resp === "OK") {
                $('#joinForm').submit();
            } else {
                alert("이미 존재하는 이메일입니다. 다른 이메일을 사용해 주세요.");
                $("#emailF").val("");
                $("#emailF").select();
                return;
            }

        },
        error: function (resp) {
            alert("회원가입이 불가능합니다. 기업과 연락을 취해주세요.");
            return;
        }
    });
})