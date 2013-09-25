$(document).ready(function(){
	$.ajax('PHPScripts/profileService.php',{
		type: 'GET',
		data: $('#uid').serialize(),
		dataType: 'json',
		success: profile_preload_success_handler,
		error: profile_preload_error_handler,
		cache:false
	});
	
	$('#testPhoto').on('click', function(){
		$('#previewPhoto').attr('src', $('#profilePhotoURL').val());
	});
	
	$('#profile-form').on('submit', function(e){
		e.preventDefault();
		$.ajax('PHPScripts/profileService.php',{
			type: 'POST',
			data: $(this).serialize(),
			dataType: 'json',
			success: profile_update_success_handler,
			error: profile_update_error_handler,
			cache:false
		});
	});
	
	var basicInfoPosition = $('#basic-info fieldset').position();
	$('#profile-picture').css('top', basicInfoPosition.top);
	$('#profile-picture fieldset').css('height', $('#basic-info fieldset').css('height'));
	
	$('#disclaimer-check').on('click', function(){
		if ($(this).attr('checked')=='checked'){
			$('#profile-submit-button').removeAttr('disabled');
			$('#agreement-checkbox-remainder').css('display', 'none');
		}
		else{
			$('#profile-submit-button').attr('disabled', 'disabled');
			$('#agreement-checkbox-remainder').css('display', 'inline');
		}
	});
	
});

var profile_preload_success_handler = function(data, textStatus, jqXHR) {
	var profileJSON = $.parseJSON(jqXHR.responseText);
	$.each(profileJSON, function(key, db_val){
		$('#' + key).val(db_val);
	});
	$('#previewPhoto').attr('src', $('#profilePhotoURL').val());
};

var profile_preload_error_handler = function(jqXHR, textStatus, errorThown) {
	alert('Something wrong!!! - No such a record on Database');
};

var profile_update_success_handler = function(data, textStatus, jqXHR) {
	var profileJSON = $.parseJSON(jqXHR.responseText);
	alert("Your profile information has successfully updated to database, please proceed to select your dorms.");
	window.location.replace('gallery.php?form=dormCart');
};

var profile_update_error_handler = function(jqXHR, textStatus, errorThown) {
	alert('Something wrong!!! profile information update to database failure.');
};