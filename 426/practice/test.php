<!DOCTYPE html>
<html>
<head>
	<title> TEST A PAGE </title>
</head>

<body>

<p> 111
<?php
	$a = 1;
	$cars=array("Saab","Volvo","BMW","Toyota");
	$pvar = $_GET['dorm'];
	$num = $cars[0];
	$size = count($pvar);
	echo '<p>'.$pvar.'</p>';
	echo '<p>'.$pvar[0].'</p>';
	echo '<p>'.$pvar[1].'</p>';
?>
</p>
<p> 222
</p>
</body>
</html>