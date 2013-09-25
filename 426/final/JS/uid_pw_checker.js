var minPwLength = 5;

var uid_pw_check = function (uid, pw, pw_confirm, msgBoard){
	var uid_pw_test = true;
	if (uid.length < 1){
		msgBoard.append("<li>Username cannot be empty </li>");
		uid_pw_test = false;
	}

	if (pw.length < 5){
		uid_pw_test = false;
		msgBoard.append("<li>Password too short (at least " + minPwLength + ")! </li>");
	}
	if (pw != pw_confirm){
		uid_pw_test = false;
		msgBoard.append("<li>Password do not match! </li>");
	}
	return uid_pw_test;
};

var uid_check_success_handler = function(data, textStatus, jqXHR) {
	var server_response = $.parseJSON(jqXHR.responseText);
	if (server_response == true){
		$('#uidBoard').text('available');
	}
	else {
		$('#uidBoard').text('username already exists!');
	}
};

var uid_check_error_handler = function(jqXHR, textStatus, errorThown) {
	alert('Something wrong!!! - uid_check_error_handler');
};

var install_uid_keyup_checker = function (uid_obj){
	uid_obj.on('keyup', function(){
		var uidCheckerURL = "PHPScripts/uidChecker.php";
		var uid_ajaxed = {
				type: 'POST',
				data: $('#uid').serialize(),
				dataType: 'json',
				success: uid_check_success_handler,
				error: uid_check_error_handler,
				cache: false
		};
		$.ajax(uidCheckerURL, uid_ajaxed);
	});
}
