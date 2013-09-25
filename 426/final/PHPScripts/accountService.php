<?php 
session_start();
require_once('class/UserPass.php');
$requestType = $_REQUEST['account-request'];

$response_info = array();
if ($requestType == 'register'){
	$user_pass = new UserPass($_REQUEST['uid']);
	if ($user_pass->is_uid_available()){
		$user_pass->write_hash_to_DB($_REQUEST['pw']);
		$response_info['status'] = 'OK';
		$response_info['message'] = 'Your account created successfully.';
	}
	else{
		$response_info['status'] = 'error';
		$response_info['message'] = 'The username already exists.';
	}
}
else if ($requestType == 'log-in'){
	$user_pass = new UserPass($_REQUEST['uid']);
	if ($user_pass->check_pw_with_DB($_REQUEST['pw'])) {
		header('Content-type: application/json');
		// Generate authorization cookie
		$_SESSION['uid'] = $_REQUEST['uid'];
		$_SESSION['authsalt'] = time();

		$auth_cookie_val = md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']);

		setcookie('USERAUTH', $auth_cookie_val, 0, '/Courses/comp426-f12/duozhao/final', $_SERVER['HTTP_HOST'], true);

		$response_info['status'] = 'OK';
		$response_info['message'] = 'Log in successfully';

	} else {
		header('HTTP/1.1 401 Unauthorized');
		header('Content-type: application/json');
		
		$response_info['status'] = 'error';
		$response_info['message'] = 'Incorrect username/password combination';
	}
}

else if ($requestType == 'sign-out'){
	$user_pass = new UserPass($_Session['uid']);
	if (isset($_COOKIE['USERAUTH']) &&
			($_COOKIE['USERAUTH'] == md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
		session_destroy();
		header('Location: ../entrance.php?entry=login');
		$response_info['status'] = 'OK';
		$response_info['message'] = 'Signed Out Successfully';
	}
	else{
		header('Refresh: 3; URL= ../entrance.php?entry=login');
		$response_info['status'] = 'error';
		$response_info['message'] = 'Not logged in to an account or session expired. This page will be redirected to login page in 3 seconds...';
	}
	session_destroy(); //*** always destory for now
}
else if ($requestType == 'update'){
	$user_pass = new UserPass($_SESSION['uid']);
	if ($user_pass->check_pw_with_DB($_REQUEST['pw'])){
		if ($_REQUEST['uid-change'] == 'on'){
			$user_pass->update_uid($_REQUEST['uid']);
		}
		$user_pass->write_hash_to_DB($_REQUEST['npw']);
		$response_info['status'] = 'OK';
		$response_info['message'] = 'Your username/password updated successfully, please re-log-in';
	}
	else {
		$response_info['status'] = 'error';
		$response_info['message'] = 'Your old password does not correct';
	}
	session_destroy();
	header('Refresh: 2; URL=../entrance.php?entry=login');
}
else if ($requestType == 'delete'){
	$user_pass = new UserPass($_SESSION['uid']);
	if ($user_pass->check_pw_with_DB($_REQUEST['pw'])){
		$user_pass->account_delete();
		$response_info['status'] = 'OK';
		$response_info['message'] = 'Your account has been deleted successfully';
	}
	else{
		$response_info['status'] = 'error';
		$response_info['message'] = 'Account deletion failed';
	}
	session_destroy();
	header('Refresh: 3; URL=../entrance.php?entry=login');
}
else {
	$response_info['status'] = 'error';
	$response_info['message'] = 'No such a service for ' . $requestType;
}

echo json_encode($response_info);

?>