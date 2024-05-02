/**
 * 
 */

/* id 중복확인 화면 출력 요청*/
function idcheck() {
	if ($("#userId").val() == "") {
		alert("아이디를 입력해 주세요!");
        $("#userId").focus();
        return false;;
	}
	
	/*id 중복확인 창 오픈*/
	var url = "id_check_form?userid="+$("#userId").val();
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
			"resizable=yes, width=550, height=300");
}

function pwconfirm() {
	var result = "n";
	if ($("#userPw").val() == "") {
		alert("비밀번호를 입력해 주세요!");
		$("#userPw").focus();
		return false;
	} else if($("#userPw").val() != $("#userpwCheck").val()) {
		alert("비밀번호가 일치하지 않습니다!");
		$("#userPw").focus();
		return false;
	} else if($("#userPw").val() == $("#userpwCheck").val()) {
		url = "pw_confirm?userpw="+$("#userPw").val();
		window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
			"resizable=yes, width=550, height=300");
	}
}
