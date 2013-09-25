//$(document).ready(function () {
//	$('form').on('submit', function(e) {	
//			var first=$('input[name="first"]').val();
//			var last=$('input[name="last"]').val();
//			var gender=$('select[name="gender"]').val();
//			var year=$('select[name="year"]').val();
//			var major=$('select[name="major"]').val();
//			var dob=$('input[name="DOB"]').val();
//			var username=first;
//			var password=$('input[name="newpass"]').val();
//			var dorm=Dorm.all['RAM'];
//			var music=$('select[name="music-listening"]').val();
//			var visitor=$('select[name="Visitors"]').val();
//			var clean=$('select[name="cleanness"]').val();
//			var sleep=$('select[name="sleeping-preference"]').val();
//			var wake=$('select[name="wake-preference"]').val();
//			var study=$('select[name="study-preference"]').val();
//			var intro=$('textarea[name="tarea"]').val();
//			var user=new User(first,last,username,password,dorm,gender,dob,year,major,music,visitor,sleep,wake,clean,study,intro);
//			User.all.push(user);
//			
//			alert(user.first+" object is created!");
//			e.preventDefault();
//	});
//});