var activeSearchURL = "PHPScripts/activeSearch.json.php"; //match later;
var userJSONURL = "PHPScripts/query.user.json.php"; //match later;
var num_snippets = 5;

$(document).ready(function () {
	var userSearch_data_pairs;
	$('#active-search-form').on('submit', function(event) {	
		event.preventDefault();
		$('#baseIndex').attr('value', 0);
		$('#windowSize').attr('value', 5);
		userSearch_data_pairs = $(this).serialize();
		activeSearchRequest(userSearch_data_pairs);
	});
	
	$('#next').on('click', function(){
		var baseIndex = parseInt($('#baseIndex').attr('value'));
		var windowSize = parseInt($('#windowSize').attr('value'));
		$('#baseIndex').attr('value', baseIndex + windowSize);
		userSearch_data_pairs = $('#active-search-form').serialize();
		activeSearchRequest(userSearch_data_pairs);
	});
	
	$('#prev').on('click', function(){
		var baseIndex = parseInt($('#baseIndex').attr('value'));
		var windowSize = parseInt($('#windowSize').attr('value'));
		$('#baseIndex').attr('value', baseIndex - windowSize);
		userSearch_data_pairs = $('#active-search-form').serialize();
		activeSearchRequest(userSearch_data_pairs);
	});
	
});

var activeSearchRequest = function (data_pairs){
	var settings = {
			type: 'GET',
			data: data_pairs,
			dataType: 'json',
			success: activeSearch_success_handler,
			error: activeSearch_error_handler,
			cache: false
	}
	$.ajax(activeSearchURL, settings);
};

var activeSearch_success_handler = function(data, textStatus, jqXHR) {
	roomieList = $.parseJSON(jqXHR.responseText);
	$('div.search-result-div').append(jqXHR.status);
	$('div.search-result-div').append(jqXHR.responseText);
	$('div.search-result-div').append(roomieList);
	
	$('div.search-result-div').empty();
	$.each(roomieList, function(key, val){
		var divID = key + "-snippet";
		$('div.search-result-div').append("<div class='search-result-item' " +
				"id='" + divID + "' data-score=" + val +">" + divID + ":" + val + "</div>");
	});
	
	$("[id$='-snippet']").each(function(i){ //div
		var baseIndex = parseInt($('#baseIndex').attr('value'));
		$(this).attr('data-index', i + 1 + baseIndex);
		roomieID = $(this).attr('id').split('-')[0];
		var settings = {
				type: 'GET',
				data: "uid=" + roomieID, //more precise serialization later
				dataType: 'json',
				success: renderUserJSON_success_handler,
				error: renderUserJSON_error_handler,
				cache: false
		}
		$.ajax(userJSONURL, settings);
	});
};

var renderUserJSON_success_handler = function(data, textStatus, jqXHR){
	var userJSON = $.parseJSON(jqXHR.responseText);
	var snippetDIV = $("#" + userJSON['uid'] + "-snippet");
	snippetDIV.html(sampleSnippet);
	snippetDIV.find('.ranking').text(
			snippetDIV.attr('data-index') + " score: " + 
			snippetDIV.attr('data-score')
		);
	snippetDIV.find('.photo').attr({
		'src':userJSON['photo'],
		'alt':userJSON['photo']
	});
	$.each(userJSON, function(name, val){
		snippetDIV.find('.' + name).text(val);
	});
};

var activeSearch_error_handler = function(jqXHR, textStatus, errorThown) {
	$('div.search-result-div').append(jqXHR.status);
	$('div.search-result-div').append(jqXHR.responseText);
	alert(jqXHR.responseText);
	window.location.replace('login.php');
};

var renderUserJSON_error_handler = function(jqXHR, textStatus, errorThown) {
	$('div.search-result-div').append(jqXHR.status);
	$('div.search-result-div').append(jqXHR.responseText);
	alert(jqXHR.responseText);
	window.location.replace('login.php');
};
