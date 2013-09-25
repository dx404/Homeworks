<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Find My Roommate - Delete Your profile</title>
<link rel="stylesheet" type="text/css" href="CSS/profile.css">
<link rel="stylesheet" type="text/css" href="CSS/body.css">
<link rel="stylesheet" type="text/css" href="CSS/banner.css">
<link rel="stylesheet" type="text/css" href="CSS/menu.css">
<link rel="stylesheet" type="text/css" href="CSS/content.css">
<script src="JS/jquery-1.8.2.js"></script>
<script src="JS/generateUser.js"></script>
<script src="JS/User.js"></script>
<script src="JS/setup.js"></script>
<script src="JS/dorm.js"></script>
<script src="JS/selectYourDorm.js"></script>
</head>

<body>
	<?php include 'common/banner.php'?>
	<?php include 'common/menu.php'?>
	<div class="profile-page content">
		<!-- Info Form For Register or Update -->
		<form class="clear" action="userRequests.php" method="GET">
			<table>
				<tr>
					<td>Username</td>
					<td><input type="text" name="uid">
					</td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input type="password"></td>
				</tr>
				<tr>
					<td>Delete My Profile</td>
					<td><input type="checkbox" name="delete"></td>
				</tr>
			</table>
			<button type="submit" class="clear">Clear Your Record</button>
		</form>
	</div>
</body>