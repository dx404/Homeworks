<?php

class User {
	private $profile = array(
			'uid'=>'anonymous', 
			'firstName'=>'Fence', 
			'lastName'=>'Cracker', 
			'gender'=>'M', 
			'DOB'=>'1999-09-09', 
			'year'=>'SO', 
			'major1'=>'MATH', 
			'major2'=>'CS', 
			'musicListening'=>'headphone', 
			'visitors'=>'sometimes', 
			'cleanness'=>'weekly', 
			'sleepTime'=>'12-2', 
			'wakeTime'=>'7-9', 
			'call'=>'919-666-9999', 
			'email'=>'fence.cracker@gmail.com', 
			'facebook'=>'http://www.facebook.com/fence.cracker',
			'aboutMe'=>'An nice, easy-going and patient person', 
			'profilePhotoURL'=>'/image/users/fencecracker.jpg'); //profile, contact
	
	
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
	public function __construct($uid) {
		$this->profile['uid'] = $uid;
	}
	
	public function load_from_client($profile){
		foreach ($this->profile as $k => $v) {
			$this->profile[$k] = $profile[$k];
		}
	}
	
	public function load_from_DB(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$result = $db->query("SELECT * FROM User WHERE `uid`='" . $this->profile['uid'] . "';");
		if ($result && $result->num_rows != 0){
			$db_tuple = $result->fetch_array();
			foreach ($this->profile as $k => $v) {
				$this->profile[$k] = $db_tuple[$k];
			}
		}
		$db->close();
		return $result;
	}
	
	public function sync_to_DB(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		
		$query = "INSERT INTO User (";
		foreach ($this->profile as $k => $v){
			$query = $query . '`' . $k . '`,';
		}
		$query = substr_replace($query, ')' , -1);
		$query = $query . ' VALUES(';
		foreach ($this->profile as $k => $v){
			$query = $query . "'" . $this->profile[$k] . "',";
		}
		$query = substr_replace($query, ')' , -1);
		
		$query = $query . " ON DUPLICATE KEY UPDATE ";
		foreach ($this->profile as $k => $v){
			$query = $query . "`" . $k . "`='" . $this->profile[$k] . "',";
		}
		$query = substr_replace($query, ';' , -1);
		
		$result = $db->query($query);
		
		$db->close();
		return $result;
	}
	
	public function getJSON() {
		return json_encode($this->profile);
	}
	
	public function getValue($name){
		if ($name == 'major'){
			return array($this->profile['major1'], $this->profile['major2']);
		}
		return $this->profile[$name];
	}

	public function delete() { 
		$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
		$result = $mysqli->query("delete from User where uid = '" . $this->profile['uid'] . "'");
		return $result;
	}

}
?>