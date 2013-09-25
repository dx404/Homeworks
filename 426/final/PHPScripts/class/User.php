<?php
/**
 * The database objects of two administrators
 */
$DB_Isabella = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
$DB['Duo'] =  new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");

class User
{
	private $myDB; 
	/**
	 * Fields of a User object
	 */
	private $uid;
	private $firstName;
	private $lastName;
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
	private $call;
	private $email;
	private $facebook;
	
	/**
	 * The database objects of two administrators
	 */
	
	/**
	 * GET	/user.php/<id>
	 * Factory Method for the creation of an user object
	 */
	public static function findByUID($uid) {
		$publicDB = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
	    
		//Query the database to create a user object to represent the user
		$result['User'] = $publicDB->query("SELECT * FROM User WHERE uid = '" . $uid ."'");
		$result['Contact'] = $publicDB->query("SELECT * FROM Contact WHERE uid = '" . $uid ."'");
		if ($result['User']) {
			if ($result['User']->num_rows == 0){
				return null;
			}
		$user_info = $result['User']->fetch_array();
		$user_Contact = $result['Contact']->fetch_array();
		$user = new User(
				$user_info['uid'],
				$user_info['firstName'],
				$user_info['lastName'],
				$user_info['gender'],
				$user_info['DOB'],
				$user_info['year'],
				$user_info['major1'],
				$user_info['major2'],
				$user_info['musicListening'],
				$user_info['visitors'],
				$user_info['cleanness'],
				$user_info['sleepTime'],
				$user_info['wakeTime'],
				$user_info['aboutMe'],
				$user_info['profilePhotoURL'],
				$user_Contact['call'],
				$user_Contact['email'],
				$user_Contact['facebook']
				);
		$publicDB->close();
		return $user;
		}
		
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

	private function __construct(
			$uid,
			$firstName,
			$lastName,
			$gender,
			$dob,
			$year,
			$major1,
			$major2,
			$musicListening,
			$visitors,
			$cleanness,
			$sleepTime,
			$wakeTime,
			$about,
			$photo,
			$call,
			$email,
			$facebook
	) {
			$this->uid = $uid;
			$this->firstName = $firstName;
			$this->lastName = $lastName;
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
			$this->about = $about;
			$this->photo = $photo;
			$this->call = $call;
			$this->email = $email;
			$this->facebook = $facebook;
	}

	public function getID() {
		return $this->uid;
	}

	public function getFirst() {
		return $this->firstName;
	}

	public function getLast() {
		return $this->lastName;
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

	public function getMajor() {
		return array($this->major1, $this->major2);
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
	
	public function getCall() {
		return $this->call;
	}
	
	public function getEmail() {
		return $this->email;
	}
	
	public function getFacebook() {
		return $this->facebook;
	}

	public function getMyDB () {
		return $this->myDB;
	}
	
	public function getJSON() {
		$json_rep = array();
		$json_rep['uid'] = $this->uid;
		$json_rep['firstName'] = $this->firstName;
		$json_rep['lastName'] = $this->lastName;
		$json_rep['gender'] = $this->gender;
		$json_rep['dob'] = $this->dob;
		$json_rep['year'] = $this->year;
		$json_rep['major'] = array($this->major1, $this->major2);
		$json_rep['musicListening'] = $this->musicListening;
		$json_rep['visitors'] = $this->visitors;
		$json_rep['cleanness'] = $this->cleanness;
		$json_rep['sleepTime'] = $this->sleepTime;
		$json_rep['wakeTime'] = $this->wakeTime;
		$json_rep['about'] = $this->about;
		$json_rep['photo'] = $this->photo;
		$json_rep['call'] = $this->call;
		$json_rep['email'] = $this->email;
		$json_rep['facebook'] = $this->facebook;
		return json_encode($json_rep);
	}

	//POST user.php parameters (all parametors) //factory method
	public static function create(
			$uid,
			$firstName,
			$lastName,
			$gender,
			$dob,
			$year,
			$major1,
			$major2,
			$musicListening,
			$visitors,
			$cleanness,
			$sleepTime,
			$wakeTime,
			$about,
			$photo,
			$call,
			$email,
			$facebook ) 
	{
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result['User'] = $mysqli->query( 
				"INSERT INTO User (
					`uid`,
					`firstName`, 
					`lastName`, 
					`gender`, 
					`DOB`, 
					`year`, 
					`major1`, 
					`major2`, 
					`musicListening`, 
					`visitors`, 
					`cleanness`, 
					`sleepTime`, 
					`wakeTime`, 
					`aboutMe`, 
					`profilePhotoURL`) 
				VALUES(
					'" . $uid . "',
					'" . $firstName . "', 
					'" . $lastName . "',
					'" . $gender .  "', 
					'" . $dob .  "', 
					'" . $year . "',
					'" . $major1 . "',
					'" . $major2 . "',
					'" . $musicListening . "', 
					'" . $visitors . "', 
					'" . $cleanness . "', 
					'" . $sleepTime ."',
					'" . $wakeTime ."', 
					'" . about . "', 
					'" . $photo ."')"
				);
		
		$result['Contact'] = $mysqli->query("
				INSERT INTO Contact (
					`uid`,
					`call`,
					`email`,
					`facebook` )
				VALUES(
					'" . $uid . "',
					'" . $call . "',
					'" . $email . "',
					'" . $facebook . "')
				");

		return new User(
				$uid,
				$firstName,
				$lastName,
				$gender,
				$dob,
				$year,
				$major1,
				$major2,
				$musicListening,
				$visitors,
				$cleanness,
				$sleepTime,
				$wakeTime,
				$about,
				$photo,
				$call,
				$email,
				$facebook
			);
	}

	//POST user.php/<id>
	public function updateUserInfo(
			$uid,
			$firstName,
			$lastName,
			$gender,
			$dob,
			$year,
			$major1,
			$major2,
			$musicListening,
			$visitors,
			$cleanness,
			$sleepTime,
			$wakeTime,
			$about,
			$photo,
			$call,
			$email,
			$facebook		
	) {
			$this->uid = $uid;
			$this->firstName = $firstName;
			$this->lastName = $lastName;
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
			$this->about = $about;
			$this->photo = $photo;
			$this->call = $call;
			$this->email = $email;
			$this->facebook = $facebook;
//  		$this->updateDB();
		return $this->updateDB();
	}

	public function delete() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("delete from User where uid = '" . $this->uid . "'");
		return $result;
	}

	private function updateDB() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result['User'] = $mysqli->query( //uid is not allowed to update
				"Update User set 
					firstName ='" . $this->firstName . "',
					lastName = '" . $this->lastName . "', 
					gender = '" . $this->gender . "', 
					DOB ='" . $this->dob . "', 
					year='" . $this->year . "', 
					major1='" . $this->major1 . "', 
					major2='" . $this->major2 . "', 
					musicListening='" . $this->musicListening . "', 
					visitors='" . $this->visitors . "',
					cleanness='"  . $this->cleanness . "',
					sleepTime='" . $this->sleepTime . "', 
					wakeTime='" . $this->wakeTime . "', 
					aboutMe='" . $this->about . "', 
					profilePhotoURL='" . $this->photo . "' 
					where uid = '" . $this->uid."'"
		);		
		$result['Contact'] = $mysqli->query("
				Update Contact set 
					`call` = '" . $this->call . "',
					email = '" . $this->email . "',
					facebook = '" . $this->facebook . "'
				where uid = '" . $this->uid. "'");
		$mysqli->close();
		return $result['User'];
	}
}
?>