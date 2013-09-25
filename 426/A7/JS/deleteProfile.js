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
					window.location.replace('https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/A7/login.php');},
					error: function(){ alert('Error!');}
						}
				);
			},
			error: function () {
				alert('Authorization Failed');}
				});
	});
});