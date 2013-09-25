<?php 
class UserWithDorms{
	private $uid;
	private $dormCart=array();
	
	public function __construct($uid){
		$this->uid = $uid;
	}
	public function load_from_client($dorm){
		foreach ($dorm as $key => $val){
			$this->dormCart[$key] = $val;
		}
	}
	
	public function load_from_DB(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$result = $db->query("SELECT * FROM UserDormInterest WHERE `uid`='" . $this->uid . "';");
		
		while ($row = $result->fetch_array(MYSQLI_ASSOC)){
			$this->dormCart[] = $row['dorm'];
		}
		$db->close();
		return $result;
	}
	
	public function sync_to_DB(){
		$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$result = $db->query("DELETE FROM UserDormInterest WHERE uid='" . $this->uid . "';");
		foreach ($this->dormCart as $index => $dormAbbr){
			$result = $result && 
				$db->query(
					"INSERT INTO UserDormInterest (
					`uid`,
					`dorm`
				)
					VALUES (
					'".$this->uid."',
					'".$dormAbbr."'
				);"
			);
		}
		return $result;
	}
	
	public function getJSON(){
		return json_encode($this->dormCart);
	}
	
}

?>