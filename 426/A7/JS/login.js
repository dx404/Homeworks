$(document).ready(function () {
	$('#login-form').on('submit', function (e) {
		e.preventDefault();
		var logInProcessor = $(this).attr("action"); // a php file to verify 
		var formInfo = {
				type: $(this).attr("method"),
				data: $(this).serialize(),
				dataType:'json',
				cache: false,
				success: logIn_success_handler,
				error: logIn_error_handler
		};
		$.ajax(logInProcessor, formInfo);
	});
});
var logIn_success_handler = function (data, textStatus, jqXHR) {
	alert('Login Successfully, process to main page'); 
	window.location.replace("MainPage.php");
};
var logIn_error_handler = function (jqXHR, textStatus, errorThown) {
	$('#msgBoard').text('username and password do not match');
};