
var cartDorms = {}; // key(dorm.abbr): value for pipeline to the submit 

var preSelectTotal = 0;
var preSelectChecked = 0;
var cartTotal = 0;
var cartChecked = 0;
var testObj = {'a[0]':'HJK', 'a[1]':'JRF'};
var lastDorms = {};

var renderPreSelectDormRow = function (dormKey){
	var dorm = Dorm.all[dormKey];
	var imgSrc = 'image/dorms/' + dorm.abbr + '.jpg';
	var iconSrc_Y = 'image/icons/validBlue.png'; //default is invalid
	var iconSrc_N = 'image/icons/blankGreen.png'; //default is invalid
	var preSelectDormRow = $(
		"<tr class='preSelect row'>" + 
			"<td> <img class='checkbox preSelect'> </td>" +
			"<td> <img class='dormImg preSelect'> </td>" +
			"<td class='description preSelect'>" + 
				"<span class='title description preSelect'>" + 
					dorm.name + "</span>" + 
					"<ul class='list description preSelect'>" +
						"<li>Style: " + dorm.style + "</li>" +
						"<li>Gender: " + dorm.gender + "</li>" +
						"<li>Community: " + dorm.community + "</li>" +
						"<li>Location: " + Dorm.fullLocation[dorm.location] + "</li>" +
					"</ul></td> " + 
		"</tr>");
	preSelectDormRow.attr({
		'id': dorm.abbr + '-preSelect-row', 
		'data-isChecked':0
		});
	$('img.checkbox', preSelectDormRow).attr({
		'id': dorm.abbr + '-preSelect-checkbox',
		'src':iconSrc_N, 
		'alt':'false'
		});
	$('img.dormImg', preSelectDormRow).attr({
		'id':dorm.abbr + '-preSelect-Img',
		'src':imgSrc,
		'alt':dorm.name,
		'width':'120', 
		'height':'100'
		});
	$('td.description', preSelectDormRow).attr(
			'id', dorm.attr + '-preSelect-description');
	
	$('img.checkbox', preSelectDormRow).toggle(
			function(){
				$(this).parent().parent().attr('data-isChecked', 1);
				$(this).attr({
					'src':iconSrc_Y,
					'alt':'true'
					});
				$('#preSelect-ratio .numerator').text(++preSelectChecked);
			},
			function(){
				$(this).parent().parent().attr('data-ischecked', 0);
				$(this).attr({
					'src': iconSrc_N, 
					'alt':'false'
				});
				$('#preSelect-ratio .numerator').text(--preSelectChecked);
			}
	);
	
	return preSelectDormRow;
};

var renderCartDormRow = function (dormKey){
	var dorm = Dorm.all[dormKey];
	var iconSrc_Y = 'image/icons/validBlue.png';
	var iconSrc_N = 'image/icons/blankGrey.png';
	var cartDormRow = $(
			"<tr class='row cart'>" +
				"<td> <img class='checkbox cart'> </td>" +
				"<td> <span class='dormName cart'>" + dorm.name + 
					"</span></td>" +
			"</tr>");
	cartDormRow.attr({
		'id': dorm.abbr + '-cart-row',
		'data-isChecked':1
		});
	$('img.checkbox', cartDormRow).attr({
		'id': dorm.abbr + '-cart-checkbox',
		'src':iconSrc_Y, 
		'alt':'true'
		});
	$('span.dormName', cartDormRow).attr(
		'id', dorm.abbr + '-cart-dormName'
		);
	$('img.checkbox', cartDormRow).toggle(
			function(){
				$(this).parent().parent().attr('data-isChecked', 0);
				$(this).attr({
					'src': iconSrc_N,
					'alt':'false'
				});
				$('#cart-ratio .numerator').text(--cartChecked);
			},
			function(){
				$(this).parent().parent().attr('data-isChecked', 1);
				$(this).attr({
					'src':iconSrc_Y,
					'alt':'false'
				});
				$('#cart-ratio .numerator').text(++cartChecked);
			}
	);
	return cartDormRow;
};

$(document).ready(function(){
	
	$('.ration span').text(0);
	$('table.preSelect tbody').empty();
	$('table.cart tbody').empty();
	
	$.ajax("PHPScripts/dormRegistrar.php", {
		type: 'GET',
		data: $('#uid').serialize(),
		dataType: 'json',
		success: function (data, textStatus, jqXHR){
				var preDorms = $.parseJSON(jqXHR.responseText);
				$.each(preDorms, function(key, db_val){
					$('#preDorms').append(", " + db_val);
				});
			},
		error: function (jqXHR, textStatus, errorThown){
				alert('Load previous dorms failed');
			},
		cache:false
	});
	
	
	preSelectTotal = 0;
	for (var k in Dorm.all){
		$('table.preSelect tbody').append(renderPreSelectDormRow(k));
		$('#preSelect-ratio .denominator').text(++preSelectTotal);
	}
	
	
	$('#add-selected').bind('click', function() {
		$('tr.preSelect[data-isChecked=1]').each(function(){
			var keyToCart = $(this).attr('id').split('-', 1)[0]; //the first
			var rowInCart = renderCartDormRow(keyToCart);
			$('table.cart tbody').append(rowInCart);
			$('#cart-ratio .numerator').text(++cartChecked);
			$('#cart-ratio .denominator').text(++cartTotal);
			
			$(this).remove();
			$('#preSelect-ratio .numerator').text(--preSelectChecked);
			$('#preSelect-ratio .denominator').text(--preSelectTotal);
		});
	}); 
	
	$('#refresh-cart').bind('click', function(){
		$('tr.cart[data-isChecked=0]').each(function(){
			var keyToPreSelect = $(this).attr('id').split('-', 1);
			var rowInPreSelect = renderPreSelectDormRow(keyToPreSelect);
			$('table.preSelect').append(rowInPreSelect);
			$('#preSelect-ratio .denominator').text(++preSelectTotal);
			
			$(this).remove();
			$('#cart-ratio .denominator').text(--cartTotal);
		});
	});
	
	$('#preSelect-all').bind('click', function(){
		$('tr.preSelect[data-isChecked=0] .checkbox').trigger('click');
	});
	
	$('#preSelect-none').bind('click', function(){
		$('tr.preSelect[data-isChecked=1] .checkbox').trigger('click');
	});
	
	$('#uncheck-all-cart').bind('click', function(){
		$('tr.cart[data-isChecked=1] .checkbox').trigger('click');
	});
	
	$('#check-all-cart').bind('click',function(){
		$('tr.cart[data-isChecked=0] .checkbox').trigger('click');
	});
	
	/**
	 * Events Handler is missing after sorting, to be fixed here
	 */
	$('select.sort option').bind('click', function(){
		var sortedAbbr = [];
		var commonID = $(this).parent().attr('id').split('-')[0];
		$('tbody.' + commonID +' tr').each(function(){
			sortedAbbr.push($(this).attr('id').split('-')[0]);
		});
		if ($(this).attr('value') == 'a2z'){
			sortedAbbr = sortedAbbr.sort();
		}
		else if ($(this).attr('value') == 'z2a'){
			sortedAbbr = sortedAbbr.sort().reverse();
		}
		$('table.' + commonID + ' tbody').empty();
		if (commonID == 'preSelect'){
			for (var i in sortedAbbr){
				$('table.preSelect tbody').append(renderPreSelectDormRow(sortedAbbr[i]));
			}
		} else if (commonID == 'cart'){
			for (var i in sortedAbbr){
				$('table.cart tbody').append(renderCartDormRow(sortedAbbr[i]));
			}
		}
	});
	
	

	$('#confirm-selected').click(function(event){
		event.preventDefault();
		var checked_dorms = {};
		checked_dorms['uid'] = $('#uid').val();
		$('tr.cart[data-isChecked=1]').each(function (i, el) {
			var dormAbbr = $(el).attr('id').split('-', 1)[0]; //why array?
			checked_dorms["dorm[" + i + "]"] = dormAbbr;
		});
		
		$.ajax("PHPScripts/dormRegistrar.php", {
			type: 'POST',
			data: checked_dorms,
			success: function(data, textStatus, jqXHR) {
				alert('dorm saved OK');
				window.location.replace("gallery.php?form=main");
			},
			error: function(jqXHR, textStatus, errorThown) {
				alert('dorm saved error');
			},
			cache: false
		});
	});
});



/** Dorm
 * 
 * Models the information associated with each dormitory.
 */

var Dorm = function (abbr, name, style, gender, community, location) {
	this.abbr = abbr; 
	this.name = name;
	this.style = style;
	this.gender = gender;
	this.community = community;
	this.location = location;
};

Dorm.all = {};
Dorm.all['RAM'] = new Dorm('RAM', 'Ram Village', 'Apartment', 'Coed', 'Ram Village', 'S');
Dorm.all['ODM'] = new Dorm('ODM', 'Odum Village', 'Apartment', 'Coed', 'Odum Village', 'S');
Dorm.all['MFR'] = new Dorm('MFR', '1101 Mason Farm Road', 'Apartment', 'Coed', 'Odum Village', 'S');
Dorm.all['MRS'] = new Dorm('MRS', 'Morrison', 'Suite', 'Coed', 'Morrison Community', 'S');
Dorm.all['HJA'] = new Dorm('HJA', 'Hinton James', 'Apartment', 'Coed', 'Hinton James', 'S');
Dorm.all['CRN'] = new Dorm('CRN', 'Craige North', 'Suite', 'Coed', 'Manning West', 'S')

Dorm.all['PKR'] = new Dorm('PKR', 'Parker', 'Suite', 'Female', 'Parker', 'Mid');
Dorm.all['TGU'] = new Dorm('TGU', 'Teague', 'Suite', 'Coed', 'Parker', 'Mid');

Dorm.all['ODE'] = new Dorm('ODE', 'Old East', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['ODW'] = new Dorm('ODW', 'Old Wast', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['RFN'] = new Dorm('RFN', 'Ruffin', 'Corridor', 'Coed', 'Olde Campus Upper Quad', 'N');
Dorm.all['GHM'] = new Dorm('GHM', 'Graham', 'Corridor', 'Male', 'Olde Campus Lower Quad', 'N');
Dorm.all['ACK'] = new Dorm('ACK', 'Aycock', 'Corridor', 'Female', 'Olde Campus Lower Quad', 'N');

Dorm.fullLocation = {'S':'Southern Campus', 'M':'Mid Campus', 'N':'Northern Campus'};



