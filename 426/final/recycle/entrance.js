$(document).ready(function(){
	$('a.entrySwitch').on('click', function(){
		var targetFormURL = 'formSnippet/' + $(this).attr('data-target') + '.php';
		$.ajax(targetFormURL, {
			type: 'GET',
			data: "uid=" + roomieID, //more precise serialization later
			dataType: 'json',
			success: renderUserJSON_success_handler,
			error: renderUserJSON_error_handler,
			cache: false
		});
	});
});