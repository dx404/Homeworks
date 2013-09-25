$(document).ready(function(){
	$('#uid-change').on('click', function(){
		if ($(this).attr('checked')=='checked'){
			$('#uid').removeAttr('disabled');
			$(this).attr('value', 'on');
		}
		else{
			$('#uid').attr('disabled', 'disabled');
			$(this).attr('value', 'off');
		}
	});
	
	install_uid_keyup_checker($('#uid'));
	
	$('#delete-profile').on('click', function(){
		$('#uid-change').attr('disabled', 'disabled');
		if ($(this).attr('checked')=='checked'){
			$('[id|=npw]').attr('disabled', 'disabled');
			if ($('#uid-change').attr('checked')=='checked'){
				$('#uid-change').removeAttr('checked');
				$('#uid').attr('disabled', 'disabled');
			}
			$('#account-request').attr('value', 'delete');
		}
		else {
			$('[id|=npw]').removeAttr('disabled');
			$('#uid-change').removeAttr('disabled');
			$('#account-request').attr('value', 'update');
		}
	});
	
	$('#account-form').on('submit', function(e){
		e.preventDefault();
		$("#msgBoard").text("");
		var accountServiceURL = 'PHPScripts/accountService.php';
		var uid_pw_test = true;
		var client_data = {
				type: 'POST',
				data: $(this).serialize(),
				dataType: 'json',
				success: account_edit_success_handler,
				error: account_edit_error_handler,
				cache:false
		};
		
		if ($('#delete-profile').attr('checked') != 'checked'){
			uid_pw_test = uid_pw_check(
						$('#uid').val(), 
						$('#npw').val(), 
						$('#npw-confirm').val(), 
						$("#msgBoard")
					);
		}
		
		if (uid_pw_test == true){
			$.ajax(accountServiceURL, client_data);
		}
		
	});
	
});


var account_edit_success_handler = function(data, textStatus, jqXHR) {
	var server_response = $.parseJSON(jqXHR.responseText);
		$('ul.msgBoard').text(server_response['message']);
		window.location.replace("entrance.php?entry=login");
};

var account_edit_error_handler = function(jqXHR, textStatus, errorThown) {
	var server_response = $.parseJSON(jqXHR.responseText);
	$('ul.msgBoard').text(server_response['message']);
	alert('Something wrong!!! - uid_check_error_handler');
};