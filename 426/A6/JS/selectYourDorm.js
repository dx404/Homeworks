var cartDorms = {}; // key(dorm.abbr): value for pipeline to the submit 

var preSelectTotal = 0;
var preSelectChecked = 0;
var cartTotal = 0;
var cartChecked = 0;

var renderPreSelectDormRow = function (dormKey){
	var dorm = Dorm.all[dormKey];
	var imgSrc = 'photos/dorms/' + dorm.abbr + '.jpg';
	var iconSrc_Y = 'icons/validBlue.png'; //default is invalid
	var iconSrc_N = 'icons/blankGreen.png'; //default is invalid
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
	var iconSrc_Y = 'icons/validBlue.png';
	var iconSrc_N = 'icons/blankGrey.png';
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
	
	preSelectTotal = 0;
	for (var k in Dorm.all){
		$('table.preSelect tbody').append(renderPreSelectDormRow(k));
		$('#preSelect-ratio .denominator').text(++preSelectTotal);
	}
	
	
	$('#add-selected').bind('click', function() {
		$('tr.preSelect[data-isChecked=1]').each(function(){
			var keyToCart = $(this).attr('id').split('-', 1);
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
	
	$('#confirm-selected').click(function(e){
		e.preventDefault();
		window.location.replace("MainPage.php");
	});
});
