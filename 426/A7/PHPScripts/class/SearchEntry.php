<?php 
class SearchEntry {
	public $uid;
	public $gender;
	public $ageLow;
	public $ageHigh;
	public $year;
	public $major1;
	public $major2;
	public $major3;
	public $musicListening;
	public $visitors;
	public $cleanness;
	public $sleepTime;
	public $wakeTime;
	
	public function __construct(
			$uid,
			$gender,
			$ageLow,
			$ageHigh,
			$year,
			$major1,
			$major2,
			$major3,
			$musicListening,
			$visitors,
			$cleanness,
			$sleepTime,
			$wakeTime
	){
			$this->uid = $uid;
			$this-> gender = $gender;
			$this-> ageLow = $ageLow;
			$this-> ageHigh = $ageHigh;
			$this-> year = $year;
			$this-> major1 = $major1;
			$this-> major2 = $major2;
			$this-> major3 = $major3;
			$this-> musicListening = $musicListening;
			$this-> visitors = $visitors;
			$this-> cleanness = $cleanness;
			$this-> sleepTime = $sleepTime;
			$this-> wakeTime = $wakeTime;
	}
	
	/** 
	 * @var $userRequest is a serialized array
	 */
	public static function createFromRequest($uid, $userRequest){
		return new self(
				$uid, 
				$userRequest['gender'],
				$userRequest['ageLow'],
				$userRequest['ageHigh'],
				$userRequest['year'],
				$userRequest['major1'],
				$userRequest['major2'],
				$userRequest['major3'],
				$userRequest['musicListening'],
				$userRequest['visitors'],
				$userRequest['cleanness'],
				$userRequest['sleepTime'],
				$userRequest['wakeTime']
		);
	}
	
	public static function createFromDB($uid){
		return null;
	}
	
	public function writeToDB(){
		$DB = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		if ($this->isInDB()){
			$update_query = "update SearchHistory set
					uid ='" . $this->uid . "',
					gender = '" . $this->gender . "',
					ageLow ='" . $this->ageLow . "',
					ageHigh ='" . $this->ageHigh . "',
					year='" . $this->year . "',
					major1='" . $this->major1 . "',
					major2='" . $this->major2 . "',
					major3='" . $this->major3 . "',
					musicListening='" . $this->musicListening . "',
					visitors='" . $this->visitors . "',
					cleanness='"  . $this->cleanness . "',
					sleepTime='" . $this->sleepTime . "',
					wakeTime='" . $this->wakeTime . "'
					where uid = '" . $this->uid. "'";
			$DB->query($update_query);
		}
		else {
			$insert_query = "INSERT INTO SearchHistory (
					`uid`,
					`gender`,
					`ageLow`,
					`ageHigh`,
					`year`,
					`major1`,
					`major2`,
					`major3`,
					`musicListening`,
					`visitors`,
					`cleanness`,
					`sleepTime`,
					`wakeTime` )
				VALUES(
					'" . $this->uid . "',
					'" . $this->gender .  "',
					'" . $this->ageLow .  "',
					'" . $this->ageHigh .  "',
					'" . $this->year .  "',
					'" . $this->major1 .  "',
					'" . $this->major2 .  "',
					'" . $this->major3 .  "',
					'" . $this->musicListening . "',
					'" . $this->visitors . "',
					'" . $this->cleanness . "',
					'" . $this->sleepTime . "',
					'" . $this->wakeTime . "')";
			$DB->query($insert_query);
		}
		$DB->close();
		return true;
	}
	
	public function delelteFromDB(){
		$DB = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		if (isInDB()){
			$delete_query = "delete from SearchHistory where uid = '". $this->uid ."'";
			$DB->query($delete_query);
		}
		$DB->close();
	}
	
	public function isInDB(){
		$DB = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$count_query = "SELECT * FROM SearchHistory WHERE uid='" . $this->uid . "'";
		$result = $DB->query($count_query);
		return $result->num_rows;
		if ($result->num_rows == 0){
			$DB->close();
			return false;
		}
		else {
			$DB->close();
			return true;
		}
	}
	
	public function findRoomie($startIndex, $length){
		$DB = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
		$roomies = array();
		$lookup_query = "
				SELECT
					searcheeID as uid,
					total as score
				FROM `ScoreTotal`
				WHERE
					searcherID='" . $this->uid . "'
				LIMIT " . $startIndex . ", " . $length . ";
			";
		if ($result = $DB->query($lookup_query)) {
			while ($row = $result->fetch_array()) {
				$roomies[$row['uid']] = intval($row['score']);
			}
			$result->close();
		}
		$DB->close();
		return $roomies;
	}
}

?>