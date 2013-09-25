$(document).ready(function () {
	$('#login-form').on('submit', function (e) {
		e.preventDefault();
		var logInProcessor = $(this).attr("action"); // a php file to verify 
		var formData = {
				type: 'GET',
				data: $('#login-form').serialize(),
				dataType:'json',
				cache: false,
				success: function () {

					alert('Login Successful'); 
					window.location.replace('https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/A7/MainPage.php');
				},
				error: function () {
					alert('Login Failed');}
		};
		$.ajax(logInProcessor, formData);
	});
});