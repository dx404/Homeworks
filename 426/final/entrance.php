<!DOCTYPE html>
<html lang="en">
<?php 
$request_token = 'login'; //default to be main
if ($_GET['entry'] == 'register' ||
		$_GET['entry'] == 'sign-up' ||
		$_GET['entry'] == 'signup'){
	$request_token = 'register';
}
?>
<head>
<meta charset="utf-8">
<title>Find My Roommate - Sign In</title>
<link rel="stylesheet" type="text/css" href="CSS/banner.css">
<link rel="stylesheet" type="text/css" href="CSS/body.css">
<link rel="stylesheet" type="text/css" href="CSS/login.css">
<link rel="stylesheet" type="text/css" href="CSS/entrance.css">
<script src="JS/jquery-1.8.2.js"></script>
<script src="JS/<?php echo $request_token?>.js"></script>
</head>

<body>
	<?php include_once 'common/banner.php'; ?>

	<div class="content">
		<div class="left-column">
			<img alt="Image is not available" src="image/background/dormSnow.gif"
				width="640"  height="500">
			<h3 class="slogan" style="color: yellow; font-style: italic">FindMyRoomate
				helps you enjoy campus life with the right people</h3>
		</div>

		<!-- refactor to ajax call later -->
		<div class="right-column" id="<?php echo $request_token;?>-entry">
			<?php include_once 'formSnippet/'.$request_token.'.php';?>
			<img alt="A dorm photo" src="image/background/TCE-1.jpg" width="380" height="290">
		</div>
	</div>
</body>