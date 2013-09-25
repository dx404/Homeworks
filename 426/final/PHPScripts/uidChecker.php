<?php 
require_once('class/UserPass.php');
$uid = $_REQUEST['uid'];
$user_pass = new UserPass($uid);
if ($user_pass->is_uid_available()){
	echo json_encode(true);
}
else{
	echo json_encode(false);
}
?>