<?php
session_start();

if (isset($_COOKIE['USERAUTH']) &&
				($_COOKIE['USERAUTH'] == md5($_SESSION['username'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
	session_destroy();
	header('Location: https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/A7/login.php');
}
else{
	echo('You are not logged in to your account. This page will be redirected to login page in 5 seconds...');
	header('Refresh: 5; URL=https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/A7/login.php' );
}

?>