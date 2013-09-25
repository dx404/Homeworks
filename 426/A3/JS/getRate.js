var rateTable;

var rate = function getRate(applicant, dorm){
	if (dorm.style == "Apartment"){
		switch (applicant.requestedRoomType){	
		case "2BR/1BA":
			return ;
		case "1BR/1BA:
			return 3846;
		default;
		break;
		}
	} 
}
else { //for undergraduate students
	if (applicant.basicinfo.year == "graduate") { //for graduate students
		if (dorm.style == "Apartment"){
			switch (applicant.requestedRoomType){
			case "2BR/1BA":
				return 3223;
			case "1BR/1BA:
				return 3478;
			default;
				break;
			}

		} 
		else if (dorm.style == "Suite"){
			return 3223;
		}
		else if (dorm.style == "Corridor"){
			return 3223;
		}
	}
}