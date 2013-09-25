<?php

class User {
	public $field_name 
		= array('uid', 
			'firstName', 
			'lastName', 
			'gender', 
			'DOB', 
			'year', 
			'major1', 
			'major2', 
			'musicListening', 
			'visitors', 
			'cleanness', 
			'sleepTime', 
			'wakeTime', 
			'call', 
			'email', 
			'facebook',
			'aboutMe', 
			'profilePhotoURL');
		
	private $profile = array(); //profile, contact
	
	public function mySQL_insert_query($profile_info){
		$query = "INSERT INTO User (";
		foreach (self::field_name as $k){
			$query = $query . '`' . $k . '`,';
		}
		$query = substr_replace($query, ')' , -1);
		$query = $query . ' VALUES(';
		foreach (self::field_name as $k){
			$query = $query . "'" . $profile_info[$k] . "',";
		}
		$query = substr_replace($query, ')' , -1);
		return $query;
	}
	
	public function mySQL_update_query($profile_info){
		$query = "UPDATE User SET ";
		foreach (self::field_name as $k){
			$query = $query . "`" . $k . "`='" . $profile_info[$k] . "',";
		}
		$query = substr_replace($query, ' ' , -1);
		$query = $query . "WHERE `uid`='" . $profile_info['uid'] . "';";
		return $query;
	}
	
	
	public static function findByUID($uid) {
		$publicDB = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
	  
		$result = $publicDB->query("SELECT * FROM User WHERE uid = '" . $uid ."'");

		if ($result) {
			if ($result->num_rows == 0){
				return null;
			}
			$user_info = $result->fetch_array();
			$user = new User($user_info);
			$publicDB->close();
			return $user;
		}
		return null;
	}

	//GET /user.php fetch all rows of User table
	private function __construct($profile_info) {
		foreach (self::field_name as $k) {
			$this->profile[$k] = $profile_info[$k];
		}
	}

	public function get_value($name){
		if ($name == 'major'){
			return array($this->profile['major1'], $this->profile['major2']);
		}
		return $this->profile[$name];
	}

	public function getJSON() {
		return json_encode($profile);
	}

	//POST user.php parameters (all parametors) //factory method
	public static function create($profile_info) 
	{
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query($this->mySQL_insert_query($profile_info));
		$mysqli->close();
		if ($result){
			return new User($profile_info);
		}
		return null;
	}

	public function updateUserInfo($profile_info) {
		foreach (self::field_name as $k){
			$this->profile[$k] = $profile_info[$k];
		}
		return $this->updateDB();
	}

	public function delete() { 
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("delete from User where uid = '" . $this->profile['uid'] . "'");
		return $result;
	}

	private function updateDB() {
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query($this->mySQL_update_query($this->profile));//uid is not allowed to update	
		$mysqli->close();
		return $result;
	}
}
?>