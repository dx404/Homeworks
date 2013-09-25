<!DOCTYPE html>
<html>
<head>
	<title>Clear Your Record</title>
</head>

<body>
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
		<button type="submit" class="clear" >Clear Your Record</button>
	</form>
</body>