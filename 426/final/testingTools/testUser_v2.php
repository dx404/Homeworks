<?php 
// require_once 'PHPScripts/class/User_v2.php';
// $sample_profile = array(
// 			'uid'=>'duo', 
// 			'firstName'=>'Xence', 
// 			'lastName'=>'f', 
// 			'gender'=>'M', 
// 			'DOB'=>'1999-11-19', 
// 			'year'=>'GR', 
// 			'major1'=>'MATH', 
// 			'major2'=>'EE', 
// 			'musicListening'=>'speaker', 
// 			'visitors'=>'freq', 
// 			'cleanness'=>'weekly', 
// 			'sleepTime'=>'12-2', 
// 			'wakeTime'=>'7-9', 
// 			'call'=>'919-666-9999', 
// 			'email'=>'abc@gmail.com', 
// 			'facebook'=>'abc@fb.com',
// 			'aboutMe'=>'very nice', 
// 			'profilePhotoURL'=>'hello.jpg'
// 		);

require_once 'PHPScripts/class/UserWithDorms.php';
$dorms = array('MFR', 'RAM');
$ud = new UserWithDorms('fencecracker');
$ud->load_from_DB();
echo var_dump($ud->getJSON());

?>