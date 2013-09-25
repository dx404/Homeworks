<?php 
session_start();
require_once('class/UserPass.php');
$requestType = $_POST['account-request'];
$uid = $_POST['uid'];
$pw = $_POST['pw'];
$user_pass = new UserPass($uid);
$response = array();
if ($_POST['account-request'] == 'register'){
	if ($user_pass->user_exist()){
		$response['status'] = false;
		$response['Message'] = 'The username already exists.';
	}
	else{
		$user_pass->write_hash_to_DB($pw);
		$response['status'] = true;
		$response['Message'] = 'Your account created successfully.';
	}
}
else if ($_POST['account-request'] == 'update'){
	$user_pass->write_hash_to_DB($pw);
}

echo json_encode($response);

?>