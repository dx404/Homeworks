<?php 
class UserPass{ //server-commited
	private $uid;
	
	private $saltLength = 7;
	
	public function __construct($uid){
		$this->uid = $uid;
	}
	
	public function user_exist(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$query = "SELECT `uid` FROM Password where uid='" . $this->uid . "'";
		$result = $db->query($query);
		if ($result) {
			if ($result->num_rows == 0){
				$db->close();
				return false;
			}
		}
		$db->close();
		return true;
	}
	
	public function get_hash_from_DB(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$query = "SELECT * FROM Password where uid='" . $this->uid . "'";
		$result = $db->query($query);
		if ($result){
			if ($result->num_rows == 0){
				return null;
			}
			$db_tuple =  $result->fetch_array();
			$db_hash =  $db_tuple['password'];
			$db->close();
			return $db_hash;
		}
		$db->close();
		return null;
	}
	
	public function write_hash_to_DB($pw, $salt = NULL){
		if (is_null($salt)){
			$salt = substr(md5(time()), 0, $this->saltLength);
		}
		$hash = $salt . sha1($salt . $pw);
		
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$insert_query = "INSERT INTO `Password` (`uid` ,`password`) VALUES ('" . $this->uid . "',  '" . $hash ."');";
		$update_query = "UPDATE `Password` SET password='" . $hash . "' WHERE `uid`='" . $this->uid . "';";
		$db_hash = $this->get_hash_from_DB();
		if (is_null($db_hash)){
			$db->query($insert_query);
		}
		else{
			$db->query($update_query);
		}
	}
	
	public function check_with_DB($pw){
		$db_hash = $this->get_hash_from_DB();
		$salt = substr($db_hash, 0, $this->saltLength);
		$user_hash = $salt . sha1($salt . $pw);
		if ($user_hash == $db_hash){
			return true;
		}
		return false;
	}
}

?>