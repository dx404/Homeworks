<?php 
	$n = 5;
 	$db = new mysqli('classroom.cs.unc.edu', 'duozhao', 'hawk42617', 'comp42617db');
 	$qry = "select * from User";
 	$result = $db->query($qry);
 	$action = "INSERT INTO User (uid, firstName, lastName, gender, DOB, year, musicListening, visitors, cleanness, sleepTime, wakeTime, aboutMe, profilePhotoURL) VALUES
('Titanic', 'Titan', 'James', 'F', '1991-02-21', 'GR', 'speaker', 'no', 'daily', '12-2', '7-9', '', ''),";
 	$result = $db->query($action);
	echo "<h2>Num of Rows:".$qry."</h2>";
?>

