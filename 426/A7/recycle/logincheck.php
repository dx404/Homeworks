<?php
session_start();
function check_password($username, $password) {
	$mysqli = new mysqli("classroom.cs.unc.edu", "haoyangh", "comp426", "comp42617db");
	$result = $mysqli->query("select * from Password where uid = '" . $username ."'");
	$upass=null;
	if ($result) {
		if ($result->num_rows == 0){
			return false;
		}
		$user_info = $result->fetch_array();
		$upass=$user_info['password'];
		if ($password == $upass) {
			return true;
		}
	}
	return false;
}

$username = $_GET['uid'];
$password = $_GET['pw'];
if (check_password($username, $password)) {
	header('Content-type: application/json');

	// Generate authorization cookie
	$_SESSION['username'] = $username;
	$_SESSION['authsalt'] = time();

	$auth_cookie_val = md5($_SESSION['username'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']);

	setcookie('USERAUTH', $auth_cookie_val, 0, '/Courses/comp426-f12/duozhao/A7', 'wwwp.cs.unc.edu', true);

	print(json_encode(true));

} else {
	unset($_SESSION['username']);
	unset($_SESSION['authsalt']);

	header('HTTP/1.1 401 Unauthorized');
	header('Content-type: application/json');
	print(json_encode(false));
}
?>