<?php
/**
 * Output user objects as JSON objects
 */
require_once 'class/User.php';
$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");

$uid = $_GET['uid'];
$user = User::findByUID($uid);
$userJSON = $user->getJSON();

echo $userJSON;
?>

