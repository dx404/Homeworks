/* User
 *
 */

 var User = function (first, last, username, password, dorm, 
		 			  gender, dob, year, major, 
		 			  music, visitor, sleep, wake, clean, study, 
		 			  intro) {
 	this.first = first;
 	this.last = last;
 	this.username = username;
 	this.password=password;
 	this.dorm=dorm;
 	
 	// basic info
 	this.gender = gender;
 	this.dob=dob;
 	this.year=year;
 	this.major=major;
 	
 	//living preference
 	this.music=music;
 	this.visitor=visitor;
 	this.sleep=sleep;
 	this.wake=wake;
 	this.clean=clean;
 	this.study=study;
 	
 	this.intro=intro;
 	this.compareScore=0;
 }
 
 
 User.prototype.compareDorm = function (otherdorm){
	 if(this.dorm==otherdorm){
		 return 10;
	 }
	 else {
		 return 0;
	 }
 }
	 
	 User.prototype.getAge = function(){
		 var dateOfBirth = this.dob.split("-");
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
	 
	 User.prototype.comparePreference = function (searchmusic, searchvisitor,searchsleep,searchwake,searchclean,searchstudy){
		var returnScore=0;
		if(this.music==searchmusic){
			returnScore+=1;
		}
		if(this.visitor==searchvisitor){
			returnScore+=1;
		}
		if(this.sleep==searchsleep){
			returnScore+=1;
		}
		if(this.wake==searchwake){
			returnScore+=1;
		}
		if(this.clean==searchclean){
			returnScore+=1;
		}
		if(this.study==searchstudy){
			returnScore+=1;
		}
		return returnScore;
	 }
	 
	 User.prototype.compareBasicInfo = function (searchgender, searchyear,
			 									 searchmajor1,searchmajor2, searchmajor3){
			var compareResult=0;
			if(this.gender==searchgender){
				compareResult+=50;
			}
					
			if(this.year==searchyear){
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
		 if(this.getAge()<=ageupperlimit&&this.getAge()>agelowerlimit){
			 return 5;
		 }
		 else {
			 return 0;
		 }
	 }


	 


 