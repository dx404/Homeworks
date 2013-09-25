<!DOCTYPE html>
<html>
<head>
	<title>form submission tester</title>
</head>

<body>
<p>
<h1> hello, world </h1>
<?php
	$n = 5;
 	$db = new mysqli('classroom.cs.unc.edu', 'duozhao', 'hawk42617', 'comp42617db');
 	$query = "select * from ScoreAge";
 	$result = $db->query($query);
 	$num = $result->num_rows;
	echo "<h2>Num of Rows:".stripslashes($row['spacepure'])$result."</h2>";
?>
</p>
	<p>Here is my body</p>
	<div>
		<button type="submit">Control Button</button>
	</div>
</body>
</html>