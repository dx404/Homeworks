<?php
/**
 * Output user objects as JSON objects
 */
session_start();
require_once 'class/User.php';
require_once 'class/SearchEntry.php';
$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");

if (isset($_COOKIE['USERAUTH']) &&
    ($_COOKIE['USERAUTH'] == md5($_SESSION['username'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
	$uid = $_SESSION['username']; 
	$user = User::findByUID($uid);
	$userSearchEntry = SearchEntry::createFromRequest($uid, $_GET);
	$userSearchEntry->writeToDB();
	$roomieList = json_encode(
			$userSearchEntry->findRoomie(
					$_GET['base'], $_GET['size']
				)
			); //update 0, 5 later
	echo $roomieList;
}
else{
	header('HTTP/1.1 402 You have to log in first!');
	header('Content-type: application/json');
	print(json_encode('Error: you have not logged in to your account.'));
}
?>
