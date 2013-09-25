$(document).ready(function(){
	$('#uid').on('keyup', function(){
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
	
	$("#register-form").on('submit', function(e){
		e.preventDefault();
		var uid_pw_test = true;
		var minPwLength = 5;
		var pw = $('#pw').val();
		var pw_confirm = $('#pw_confirm').val();
		$(this).find('.msgBoard').text("");
		
		if ($('#uid').val().length < 1){
			$(this).find('.msgBoard').append("<li>Username cannot be empty </li>");
			uid_pw_test = false;
		}
		
		if (pw.length < 5){
			uid_pw_test = false;
			$(this).find('.msgBoard').append("<li>Password too short (at least " + minPwLength + ")! </li>");
		}
		if (pw != pw_confirm){
			uid_pw_test = false;
			$(this).find('.msgBoard').append("<li>Password do not match! </li>");
		}
		
		if (uid_pw_test == true){
			var accountServiceURL = 'PHPScripts/accountService.php';
			var client_data = {
					type: 'POST',
					data: $(this).serialize(),
					dataType: 'json',
					success: account_create_success_handler,
					error: account_create_error_handler,
					cache: false
			};
			$.ajax(accountServiceURL, client_data);
		}
		
	});
	
});

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

var account_create_success_handler = function(data, textStatus, jqXHR) {
	var server_response = $.parseJSON(jqXHR.responseText);
	if (server_response['status'] == 'OK'){
		$("#register-form").find('.msgBoard').append("<li>" + server_response['message'] + "</li>");
		$("#register-form").find('.msgBoard').append("<li>" + "You will log in automtically..." + "</li>");
		$.ajax('PHPScripts/accountService.php', {
			type:'POST',
			data: $('#uid').serialize() + "&account-request=log-in&"  + $("#pw").serialize(),
			dataType:'json',
			cache: false,
			success: logIn_success_handler,
			error: logIn_error_handler
		});
	}
	else {
		$("#register-form").find('.msgBoard').append("<li>" + server_response['message'] + "</li>");
	}
};

var account_create_error_handler = function(jqXHR, textStatus, errorThown) {
	alert('Something wrong!!! - account_create_error_handler');
};

var logIn_success_handler = function (data, textStatus, jqXHR) {
	alert('Login Successfully!!! Click to proceed to register your information. '); 
	window.location.replace('gallery.php?form=profile');
};
var logIn_error_handler = function (jqXHR, textStatus, errorThown) {
	$('#msgBoard').text('Automatic Login failed!');
};