<?php
require_once 'class/UserPass.php';
session_start();

if (isset($_COOKIE['USERAUTH']) &&
		($_COOKIE['USERAUTH'] == md5($_SESSION['username'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
		$username = $_SESSION['username'];
		$user_pass = new UserPass($username); //create a user pass by given uid
		$password = $_GET['pw'];
		if ($user_pass->check_pw_with_DB($password)) {
		  header('Content-type: application/json');
		  print(json_encode(true));	  
		}
		else {
			header('HTTP/1.1 402 Password mismatch with Database record');
			header('Content-type: application/json');
			print(json_encode(false));
		}		
}else {
  header('HTTP/1.1 401 Session not found');
  header('Content-type: application/json');
  print(json_encode(false));
}
?>