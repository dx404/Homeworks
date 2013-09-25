$(document).ready(function () {
	$('#quit-form').on('submit', function (e) {
		e.preventDefault();
		$.ajax('PHPScripts/quitcheck.php',
				{type: 'GET',
			data: $('#quit-form').serialize(),
			dataType:'json',
			cache: false,
			success: function () {
				$.ajax('PHPScripts/userRequests.php',
						{type: 'GET',
					data: $('#quit-form ').serialize(),
					dataType:'json',
					cache: false,
					success: function (){ alert('You have successfully delete your profile');
					window.location.replace('login.php');},
					error: function(){ alert('Error!');}
						}
				);
			},
			error: function () {
				alert('Authorization Failed');}
				});
	});
});