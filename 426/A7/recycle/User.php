<?php

class User
{
	private $uid;
	private $first;
	private $last;
	private $gender;
	private $dob;
	private $year;
	private $major1;
	private $major2;
	private $musicListening;
	private $visitors;
	private $cleanness;
	private $sleepTime;
	private $wakeTime;
	private $about;
	private $photo;

	//GET	/user.php/<id>
	public static function findByUID($uid) {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("select * from User where uid = '" . $uid ."'");
		if ($result) {
			if ($result->num_rows == 0){
				return null;
			}
			$user_info = $result->fetch_array();
			return new User($user_info['uid'],$user_info['firstName'],$user_info['lastName'],
					$user_info['gender'],$user_info['DOB'],$user_info['year'],$user_info['major1'],$user_info['major2'],
					$user_info['musicListening'],$user_info['visitors'],$user_info['cleanness'],$user_info['sleepTime'],
					$user_info['wakeTime'],$user_info['aboutMe'],$user_info['profilePhotoURL']);
		}
		return null;
	}

	//GET /user.php fetch all rows of User table
	public static function retrieveAll() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("select * from User order by uid ASC");
		$userArray = array();
		 
		if ($result) {
			if ($result->num_rows == 0){
				return null;
			}
			for ($i=1; $i<=$result->num_rows; $i++) {
				$next_row = $result->fetch_row();
				if ($next_row) {
					array_push($userArray, User::findByUID($next_row[0]));
				}
			}
		}
		return $userArray;
	}

	private function __construct($uid,$first,$last,$gender,$dob,$year,$major1,$major2,$musicListening,
			$visitors,$cleanness,$sleepTime,$wakeTime,$about,$photo) {
		$this->uid = $uid;
		$this->first = $first;
		$this->last = $last;
		$this->gender = $gender;
		$this->dob = $dob;
		$this->year = $year;
		$this->major1 = $major1;
		$this->major2 = $major2;
		$this->musicListening = $musicListening;
		$this->visitors = $visitors;
		$this->cleanness = $cleanness;
		$this->sleepTime = $sleepTime;
		$this->wakeTime = $wakeTime;
		$this->about=$about;
		$this->photo=$photo;
	}

	public function getID() {
		return $this->uid;
	}

	public function getFirst() {
		return $this->first;
	}

	public function getLast() {
		return $this->last;
	}

	public function getGender() {
		return $this->gender;
	}

	public function getDOB() {
		return $this->dob;
	}

	public function getYear() {
		return $this->year;
	}

	public function getMajor1() {
		return $this->major1;
	}
	
	public function getMajor2() {
		return $this->major2;
	}

	public function getMusicListening() {
		return $this->musicListening;
	}

	public function getVisitors() {
		return $this->visitors;
	}

	public function getCleanness() {
		return $this->cleanness;
	}

	public function getSleepTime() {
		return $this->sleepTime;
	}

	public function getWakeTime() {
		return $this->wakeTime;
	}

	public function getAbout() {
		return $this->about;
	}

	public function getPhoto() {
		return $this->photo;
	}

	public function getJSON() {
		$json_rep = array();
		$json_rep['uid'] = $this->uid;
		$json_rep['first'] = $this->first;
		$json_rep['last'] = $this->last;
		$json_rep['gender'] = $this->gender;
		$json_rep['dob'] = $this->dob;
		$json_rep['year'] = $this->year;
		$json_rep['major1'] = $this->major1;
		$json_rep['major2'] = $this->major2;
		$json_rep['musicListening'] = $this->musicListening;
		$json_rep['visitors'] = $this->visitors;
		$json_rep['cleanness'] = $this->cleanness;
		$json_rep['sleepTime'] = $this->sleepTime;
		$json_rep['wakeTime'] = $this->wakeTime;
		$json_rep['about'] = $this->about;
		$json_rep['photo'] = $this->photo;
		return $json_rep;
	}
	
	//POST user.php parameters (all parametors)
	public static function create($uid,$first,$last,$gender,$dob,$year,$major1,$major2,$musicListening,
			$visitors,$cleanness,$sleepTime,$wakeTime,$about,$photo) {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("INSERT INTO User (`uid`, `firstName`, `lastName`, `gender`, `DOB`, `year`,  `major1`,  `major2`, `musicListening`, `visitors`, `cleanness`, `sleepTime`, `wakeTime`, `aboutMe`, `profilePhotoURL`) VALUES('" .
				$uid . "', '" . $first . "', '" . $last . "', '" . $gender .  "', '" . $dob .  "', '" . $year .
				"', '" . $major1 . "', '" . major2 . "', '" . $musicListening . "', '" . $visitors . "', '" . $cleanness . "', '" . $sleepTime .
				"', '".$wakeTime."', '".about. "', '". $photo ."')");

		if ($result) {
			return new User($uid,$first,$last,$gender,$dob,$year,$major1,$major2,$musicListening,
					$visitors,$cleanness,$sleepTime,$wakeTime,$about,$photo);
		}
		return null;
	}

	//Add MajorIn Table Update query
	//POST user.php/<id>
	public function updateUserInfo($new_first,$new_last,$new_gender,$new_dob,$new_year,$new_major1,$new_major2,$new_music,
			$new_visitor,$new_clean,$new_sleep,$new_wake,$new_about,$new_photo) {

		$this->first = $new_first;
		$this->last = $new_last;
		$this->gender = $new_gender;
		$this->dob = $new_dob;
		$this->year = $new_year;
		$this->major1 = $new_major1;
		$this->major2 = $new_major2;
		$this->musicListening = $new_music;
		$this->visitors = $new_visitor;
		$this->cleanness = $new_clean;
		$this->sleepTime = $new_sleep;
		$this->wakeTime = $new_wake;
		$this->about = $new_about;
		$this->photo = $new_photo;
		return $this->update();
	}

	public function delete() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("delete from User where uid = '" . $this->uid . "'");
		return $result;
	}

	private function update() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("Update User set firstName ='" . $this->first . "', lastName = '" . $this->last . "', gender = '" . $this->gender .
				"', DOB ='" . $this->dob . "', year='" . $this->year . "', major1='" . $this->major1 . "', major2='" . $this->major2 . 
				"', musicListening='" . $this->musicListening . "', visitors='" . $this->visitors . "', cleanness='"  . $this->cleanness . 
				"', sleepTime='" . $this->sleepTime ."', wakeTime='" . $this->wakeTime . "', aboutMe='" . $this->about . "', profilePhotoURL='" . $this->photo . 
				"' where uid = '" . $this->uid."'");
		return $result;
	}
}
?>