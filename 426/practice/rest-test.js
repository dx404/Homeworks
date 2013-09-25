/**
 * @author kmp
 * @editor duzhao
 */
$(document).ready(function () {
	$('#restform').on('submit', form_submit_handler); //form#restform, submit a form, click a button 
	$('#clearbutton').on('click', function () { 
		$('#params input').each(function (i,e) { //i for index, e for element
			$(e).val(i);
		});
	});
});

/**
 * the differences between e and $('this')?
 */
var form_submit_handler = function (e) {
	e.preventDefault();
	e.stopPropagation();

	// Collect parameter name/value pairs as data 
	var data_pairs = {};
	$('#params div').each(function (i, e) {
		/**
		 * $.trim()
		 * $(selector_0).find(selector_1) filtered by selector_1
		 * Q: why not $('e input.pname') incorrect or $('input.pname', e) //may be correct
		 */
		var pname = $.trim($(e).find('input.pname').val()); 
		
		if (pname != "") {
			data_pairs[$(e).find('input.pname').val()] =
				$(e).find('input.pval').val();
		}// add name/value pair
	});

	// Get URL from rest_url text input
	var ajax_url = $('#rest_url').val();

	// Set up settings for AJAX call
	var settings = {
			type: $('#methodselect option:selected').val(),
			data: data_pairs,
			success: ajax_success_handler,
			error: ajax_error_handler,
			cache: false
	}

	// Make AJAX call
	$.ajax(ajax_url, settings);
};

/**
 * Q: Why is the order of formal parameters of the two functions below different?
 * success/error -> No restriction on the function signature
 * is jqXHR a keyword/identifier, (prefer identifier, but the data type and meaning 
 * are passed in by the server accordingly)
 */
var ajax_success_handler = function(data, textStatus, jqXHR) {
	$('#returnstatus').html(jqXHR.status);
	$('#returntext').html(jqXHR.responseText);
};

var ajax_error_handler = function(jqXHR, textStatus, errorThown) {
	$('#returnstatus').html(jqXHR.status);
	$('#returntext').html(jqXHR.responseText);
}

