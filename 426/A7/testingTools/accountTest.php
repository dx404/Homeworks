<?php 
require_once 'class/UserPass.php';
$usrp = new UserPass('Bmyf');
$usrp->write_hash_to_DB('603603');
echo $usrp->get_hash_from_DB() . "\n";
$status = $usrp->check_with_DB('603603');
if ($status){
	echo "Correct Password\n";
}
else {
	echo "Incorrect Password\n";
}
?>