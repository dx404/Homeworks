<!DOCTYPE html>
<head>
	<title>session test</title>
</head>
<body>
<?php 
	session_start();
	$_SESSION['abc'] = 'bcd';
	echo var_dump($_SESSION);
?>
</body>