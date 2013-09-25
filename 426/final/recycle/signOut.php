<?php
session_start();

if (isset($_COOKIE['USERAUTH']) && 
		($_COOKIE['USERAUTH'] == md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
	session_destroy();
	header('Location: ../login.php');
}
else{
	echo('You are not logged in to your account. This page will be redirected to login page in 3 seconds...');
	header('Refresh: 3; URL=../login.php');
}

?>