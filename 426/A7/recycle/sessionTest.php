<!DOCTYPE html>
<head>
	<title>session test</title>
</head>
<body>
<?php 
	session_start();
	echo var_dump($_SESSION);
?>
</body>