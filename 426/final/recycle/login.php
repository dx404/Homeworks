<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Find My Roommate - Sign In</title>
	<link rel="stylesheet" type="text/css" href="CSS/login.css">
	<link rel="stylesheet" type="text/css" href="CSS/banner.css">
	<link rel="stylesheet" type="text/css" href="CSS/body.css">
	<script src="JS/jquery-1.8.2.js"></script>
	<script src="JS/login.js"></script>
</head>

<body>
	<?php include_once 'common/banner.php'; ?>
	
	<div class="content">
		<div class="left-column">
			<img alt="Image is not available" src="image/background/dormSnow.gif"
				width="640" , height="500">
			<h3 class="slogan" style="color:yellow;font-style:italic">FindMyRoomate helps you enjoy
				campus life with the right people</h3>
		</div>
		
		<div class="right-column">
			<form id="login-form" class="clear" action="PHPScripts/loginCheck.php" method="post">
				<fieldset>
					<legend>Log In</legend>
					<span id="msgBoard" style="color:red;"></span>
					<table class="login">
						<tr class="major">
							<td><label for="uid">Username</label></td>
							<td><input type="text" id="uid" name="uid"></td>
						</tr>
						<tr class="major">
							<td><label for="pw">Password</label></td>
							<td><input type="password" id="pw" name="pw"></td>
						</tr>
						<tr>
							<td><button type="submit" style="font-size: 16pt">Log In</button>
							</td>
							<td><a href="profile.php">To Sign Up</a></td>
						</tr>
						<tr>
							<td colspan="2"><label><input type="checkbox"> Keep me logged in
							</label>
							</td>
						</tr>
						<tr>
							<td colspan="2"><a href="info.html">Forgot your password?</a>
							</td>
						</tr>
					</table>

					<img alt="A dorm photo"
						src="image/background/TCE-1.jpg" 
						width="380" , height="290">
				</fieldset>
			</form>
		</div>
	</div>
</body>