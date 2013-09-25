<?php
session_start();
require_once 'class/User_v2.php';
if ($_SERVER['REQUEST_METHOD'] == 'POST' ) { //other authentication checking later uid null ..
	$uid = $_POST['uid'];
	if (is_null($uid)) {
		header("HTTP/1.1 400 Bad Request");
		echo("Error: please specify a username! Not logged-in");
		exit();
	}
	
	$user_profile = new User($_POST['uid']);
	$user_profile->load_from_client($_POST);
	$db_feedback = $user_profile->sync_to_DB();
	if (!$db_feedback) {
		header("HTTP/1.1 400 Bad Request");
		$message = json_encode("User failed at database");
		echo($messagex);
		exit();
	}
	header("Content-type: application/json");
	echo $user_profile->getJSON();
}
else if ($_SERVER['REQUEST_METHOD'] == 'GET' && !is_null($_GET['uid'])){
	$user_profile = new User($_GET['uid']);
	$user_profile->load_from_DB();
	header("Content-type: application/json");
	echo $user_profile->getJSON();
}
else {
	header("HTTP/1.1 403 Bad Request ". $_SERVER['REQUEST_METHOD']);
	print("Request did not match any known action. No such a service!!");
}
?>