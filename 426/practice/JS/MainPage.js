// $(document).ready(function () {
//	$('form').on('submit', function(e) {	
//		$('#search-result-div').empty();
//		var searchResult=$('#search-result-div');
//		for (var i = 0; i<User.all.length; i++) {
//			var score=0;
//			score+=User.all[i].compareAge($('#age-no-older-than').val(),$('#age-no-younger-than').val());
//			score+=User.all[i].compareBasicInfo($('input[name="select-gender"]').val(),$('input[name="select-year"]').val(),
//												$('#major-option-1').val(),$('#major-option-2').val(),$('#major-option-3').val());
//			score+=User.all[i].comparePreference($('#music-listening').val(),$('#visitor-preference').val(),$('#sleeping-preference').val(),
//												$('#wake-preference').val(),$('#cleanness').val());
//			User.all[i].compareScore=score;
//		}
//		
//		User.all.sort(function(a,b) {
//			if (a.compareScore < b.compareScore) {
//				return 1;
//			}else {
//				return -1;
//			}
//		});
//		
//		for (var i = 0; i<User.all.length; i++) {
//			searchResult.append(renderSearchResultItem(User.all[i],i+1));
//		}
//		
//		for (var i = 0; i<User.all.length; i++) {
//			User.all[i].compareScore=0;
//		}
//
//		e.preventDefault();
//	});
//});
//
//var renderSearchResultItem = function (user,index){
//	var imgSrc = './photos/icon.jpg';
//	var searchResultItem = $("<div class='search-result-item'></div>");
//	searchResultItem.append("<div class ='picture'></div>");
//	searchResultItem.append("<div class ='basic'></div>");
//
//	searchResultItemPic=$('div.picture', searchResultItem);
//	searchResultItemPic.append("<div class='ranking'>" +index+"</div>");
//	searchResultItemPic.append("<figure><img></figure></div>");
//	searchResultItemBasic=$('div.basic', searchResultItem);
//	searchResultItemBasic.append("<div class='Name'>"+user.first+" "+user.last+"</div><div class='stats'>"+
//								 "<span>Gender:</span> "+ user.gender+" <span>Age:</span> "+ user.getAge()+
//								 " <span>Year:</span> " +user.year+" <span>Major:</span> "+ user.major+"</div>");
//	searchResultItemBasic.append("<div class ='preference'></div>");
//	searchResultItemPref=$('div.preference',searchResultItemBasic);
//	searchResultItemPref.append("<table class='preference-table'><tbody>" +
//								"<tr><td><span>Music Listening: </span>"+user.music+"</td><td><span>Visitor:</span>"+user.visitor+"</td></tr>" +
//								"<tr><td><span>Sleep Preference:</span>"+user.sleep+"</td><td><span>Wake Preference:</span>"+user.wake+"</td></tr>" +
//								"<tr><td><span>Cleanness: </span>"+user.clean+"</td></tr></tbody></table>");
//	$('img', searchResultItem).attr({
//		'src':imgSrc, 
//		'height':'150',
//		'width':'150',
//		'alt':'profile picture'
//		});
//	return searchResultItem;
//};
