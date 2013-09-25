<?php 
$field_name
= array('uid',
'firstName',
'lastName',
'gender',
'DOB',
'year',
'major1',
'major2',
'musicListening',
'visitors',
'cleanness',
'sleepTime',
'wakeTime',
'call',
'email',
'facebook',
		'aboutMe',
		'profilePhotoURL');
$profile = array();
$profile['uid'] = 'hello';
$profile['firstName'] = 'wweee';

		$query = "UPDATE User SET ";
		foreach ($field_name as $k){
			$query = $query . "`" . $k . "`='" . $profile[$k] . "',";
		}
		$query = substr_replace($query, ';' , -1);
echo $query;
?>