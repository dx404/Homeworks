<?php
session_start();
require_once 'class/UserWithDorms.php';
if ($_SERVER['REQUEST_METHOD'] == 'POST' ) { //other authentication checking later uid null ..
	$uid = $_POST['uid'];
	if (is_null($uid)) {
		header("HTTP/1.1 400 Bad Request - null uid");
		echo("Error: please specify a username! Not logged-in");
		exit();
	}
	
	$cart = new UserWithDorms($_POST['uid']);
	$cart->load_from_client($_POST['dorm']);
	$db_feedback = $cart->sync_to_DB();
	if (!$db_feedback) {
		header("HTTP/1.1 400 Bad Request - DB error");
		$message = json_encode("User failed at database");
		echo($messagex);
		exit();
	}
	header("Content-type: application/json");
	echo $cart->getJSON();
}
else if ($_SERVER['REQUEST_METHOD'] == 'GET' && !is_null($_GET['uid'])){
	$cart = new UserWithDorms($_GET['uid']);
	$cart->load_from_DB();
	header("Content-type: application/json");
	echo $cart->getJSON();
}
else {
	header("HTTP/1.1 403 Bad Request ". $_SERVER['REQUEST_METHOD']);
	print("Request did not match any known action. No such a service!!");
}
?>