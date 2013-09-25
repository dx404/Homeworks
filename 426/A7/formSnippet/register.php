<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Find My Roommate</title>
<script src="../JS/jquery-1.8.2.js"></script>
<script type="text/javascript"src="../JS/register.js"></script>
</head>
<body>
	<div id="password" class="simple-input-block">
		<form id="register">
			<fieldset class="simple-input-block">
				<legend>FindMyRoommate Account - Register</legend>
				<input type="hidden" name='account-request' value='register'>
				<table>
					<tr>
						<td>Username</td>
						<td><input type="text" id="uid" name="uid">
						</td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" id="pw" name="pw">
						</td>
					</tr>
					<tr>
						<td>Confirm your password</td>
						<td><input type="password" id="pw_confirm" name="pw_confirm"></td>
					</tr>
				</table>
				<ul class="msgBoard"></ul>
				<button type="submit">Confirm</button>
			</fieldset>
		</form>
	</div>
</body>
</html>
