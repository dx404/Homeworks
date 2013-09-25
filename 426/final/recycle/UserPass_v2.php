<?php 
class UserPass{ //server-commited
	private $uid;
	private $saltLength = 7;
	
	/**
	 * @var $state
	 * -1: uncommited pass with DB
	 *  0: unchecked pass 
	 *  1: commited pass with DB
	 */
	private $uid_state = 0;
	private $action_result = array();


	public function __construct($uid){
		$this->uid = $uid;
	}
	
	public function check_session($session_uid){
		if ($this->uid == $session_uid){
			$this->$uid_state = 1;
			return true;
		}
		$this->$uid_state = -1;
		return false;
	}
	
	public function create_accout($pw){
		if ($this->is_uid_available()){
			$this->write_hash_to_DB($pw);
			$this->state = 1;
			$action_result['status'] = 'OK';
			$action_result['message'] = 'Your account created successfully.';
		}
		else{
			$this->state = -1;
			$action_result['status'] = 'error';
			$action_result['message'] = 'The username already exists.';
		}
	}
	
	public function update_uid(){
		
	}
	
	public function update_pw($old_pw, $new_pw){
		if ($this->check_pw_with_DB($old_pw)) {
			$this->write_hash_to_DB($new_pw);
			$action_result['status'] = 'OK';
			$action_result['message'] = 'Password updated successfully.';
		}
		else {
			$action_result['status'] = 'error';
			$action_result['message'] = 'Old Pass word does not match';
		}
	}
	
	public function delete_account(){
		
	}
	
	public function get_message(){
		return $this->message;
	}

	public function is_uid_available(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$query = "SELECT `uid` FROM Password where uid='" . $this->uid . "'";
		$result = $db->query($query);
		if ($result) {
			if ($result->num_rows == 0){
				$db->close();
				return true;
			}
		}
		$db->close();
		return false;
	}

	public function check_pw_with_DB($pw){
		$db_hash = $this->get_hash_from_DB();
		$salt = substr($db_hash, 0, $this->saltLength);
		$user_hash = $salt . sha1($salt . $pw);
		if ($user_hash == $db_hash){
			return true;
		}
		return false;
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
		$db->close();
	}
	
	public function delete_DB_record(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$result = $db->query("delete from UserPass where uid = '" . $this->uid . "'");
		$db->close();
		return $result;
	}

}

?>