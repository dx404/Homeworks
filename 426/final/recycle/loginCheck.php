<?php
require_once 'class/UserPass.php';
session_start();
$username = $_REQUEST['uid'];
$password = $_REQUEST['pw'];

$user_pass = new UserPass($username); //create a user pass by given uid

if ($user_pass->check_pw_with_DB($password)) {
	header('Content-type: application/json');
	// Generate authorization cookie
	$_SESSION['uid'] = $username;
	$_SESSION['authsalt'] = time();

	$auth_cookie_val = md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']);

	setcookie('USERAUTH', $auth_cookie_val, 0, '/Courses/comp426-f12/duozhao/final', $_SERVER['HTTP_HOST'], true);

	print(json_encode(true));

} else {
	unset($_SESSION['uid']);
	unset($_SESSION['authsalt']);

	header('HTTP/1.1 401 Unauthorized');
	header('Content-type: application/json');
	print(json_encode(false));
}
?>