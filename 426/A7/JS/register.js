$(document).ready(function(){
	$("#register").on('submit', function(e){
		e.preventDefault();
		var password_test = true;
		var minPwLength = 5;
		var pw = $('#pw').val();
		var pw_confirm = $('#pw_confirm').val();
		$(this).find('.msgBoard').text("");
		
		if (pw.length < 5){
			password_test = false;
			$(this).find('.msgBoard').append("<li>Password too short (at least " + minPwLength + ")! </li>");
		}
		if (pw != pw_confirm){
			password_test = false;
			$(this).find('.msgBoard').append("<li>Password do not match! </li>");
		}
		
		if (password_test == true){
			var accountServiceURL = '../PHPScripts/accountService.php';
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
var account_create_success_handler = function(data, textStatus, jqXHR) {
	var server_response = $.parseJSON(jqXHR.responseText);
	alert(server_response['message'] );
	if (server_response['status'] == true){
		alert(server_response['message'] );
	}
	else {
		$(this).find('.msgBoard').append("<li>" + server_response['message'] + "</li>");
	}
};

var account_create_error_handler = function(jqXHR, textStatus, errorThown) {
	alert('Something wrong!!!');
};