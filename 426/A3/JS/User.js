/* User
 *
 */

 var User = function (first, last, username, password, dorm, 
		 			  gender, dateofbirth, year, major, 
		 			  music, visitor, sleep, wake, clean, study, 
		 			  introparagraph) {
 	this.first = first;
 	this.last = last;
 	this.username = username;
 	this.password=password;
 	this.dorm=Dorm.all[dorm];
 	
 	// basic info
 	this.gender = gender;
 	this.dateofbirth=dateofbirth;
 	this.year=year;
 	this.major=major;
 	
 	//living preference
 	this.music=music;
 	this.visitor=visitor;
 	this.sleep=sleep;
 	this.wake=wake;
 	this.clean=clean;
 	this.study=study;
 	
 	this.introparagraph=introparagraph;
 }
 
 User.all={};
 User.all{'tomf'}=new User{'Tom','Felton','tomf','1234567','RAM','Male','07/04/91','Senoir','Computer Science,
	 					'Speaker','Before 7AM','Clean Everyday','Study in Library'};
 User.all{'alicem'}=new User{'Alice','Milton','alicem','1234567','RAM','Female','07/04/93','Sophomore','Business',
	 	 					'Headphone','Before 7AM','Clean Everyday','Study in Library'};	 					
 }
	 
	User.prototype.updatePassword = function(user, oldpassword, newpassword) {
		 if(oldpassword==this.password){
		 	this.password = newpassword;
		 }
		 else {
			 // actions for failed authentication
		 }
	 };
 
	 User.prototype.compareScore =function(user){
		 return this.comparePreference(searchmusic, searchvisitor,searchsleep,searchwake,searchclean,searchstudy)+
		 		this.compareBasicInfo(searchgender,searchageupper, searchagelower,searchyear,searchmajor1,searchmajor2,searchmajor3)+
		 		this.compareDorm(dorm);
	 }
	 
	 User.prototype.compareDorm = function (dorm){
		 if(this.dorm==dorm){
			 return 10;
		 }
		 else {
			 return 0;
		 }
	 }
	 
	 User.prototype.getAge = function(){
		 var dateOfBirth = this.basicinfo['DateOfBirth'].split("-");
		 var year=parseInt(dateOfBirth[0]);
		 var month=parseInt(dateOfBirth[1]);
		 var day=parseInt(dateOfBirth[2]);
		 var today=new Date();
		 var age=today.getFullYear()-year;
		 if(today.getMonth()<month || 
		    (today.getMonth()==month && today.getDate()<day)){
			 age--;
		 }
		 return age;
	 } 
	 
	 User.prototype.comparePreference = function (searchmusic, searchvisitor,searchsleep,searchwake,searchclean,searhcstudy){
		var returnScore=0;
		if(this.music!=searchmusic){
			returnScore+=1;
		}
		if(this.visitor!=searchvisitor){
			returnScore+=1;
		}
		if(this.sleep!=searchsleep){
			returnScore+=1;
		}
		if(this.wake!=searchwake){
			returnScore+=1;
		}
		if(this.clean!=searchclean){
			returnScore+=1;
		}
		if(this.study!=searhcstudy){
			returnScore+=1;
		}
		return returnSocre;
	 }
	 
	 User.prototype.compareBasicInfo = function (searchgender, searchageupper,searchagelower,searchyear,
			 									 searchmajor1,searchmajor2, searchmajor3){
			var compareResult=0;
			if(this.gender!=searchgender){
				compareResult+=50;
			}
			
			compareResult+=User.prototype.compareAge(searchageupper,searchsearchagelower);
			
			if(this.year!=searchyear){
				compareResult+=5;
			}
			if(this.major==searchmajor1){
				compareResult+=3;
			}
			else if (this.major==searchmajor2){
				compareResult+=2;
			}
			else if (this.major==searchmajor3){
				compareResult+=1;
			}
			return compareResult;
		 }

	 User.prototype.compareAge = function (ageupperlimit, agelowerlimit){
		 if(this.getAge()<=ageupperlimit.parseInt()&&this.getAge()>=agelowerlimit.parseInt()){
			 return 5;
		 }
		 else {
			 return 0;
		 }
	 }
	 


 