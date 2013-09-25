<!DOCTYPE html>
<html>
<head>
	<title>Session and Cookie Dump</title>
</head>
<body>
	<?php session_start();?>
	<p>Session variables </p>

	<?php var_dump($_SESSION); ?>
	
	<p> Cookie variables </p>
	<?php var_dump($_COOKIE); ?>
	
	<p> AUTH variables </p>
	<?php echo isset($_COOKIE['USERAUTH']) && 
		($_COOKIE['USERAUTH'] == md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt'])); ?>
</body>
</html>