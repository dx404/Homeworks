<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Find My Roommate</title>
</head>
<body>

	<div id="password" class="simple-input-block">
		<?php session_start();?>
		<form>
			<fieldset class="simple-input-block">
				<legend>FindMyRoommate Account - Update</legend>
				<input type="hidden" name='account-request' value='update'>
				<table>
					<tr>
						<td>Username</td>
						<td><input type="text" id="uid" name="uid"
							value="<?php echo $_SESSION['username'];?>" disabled>
						</td>
					</tr>
					<tr>
						<td>Current Password</td>
						<td><input type="password" name="pw">
						</td>
					</tr>
					<tr>
						<td>New Password</td>
						<td><input type="password" name="npw">
						</td>
					</tr>
					<tr>
						<td>Confirm your password</td>
						<td><input type="password" name="npw_confirm"></td>
					</tr>
					<tr>
						<td>Delete My Profile</td>
						<td><input type="checkbox" name="delete" value="on"></td>
					</tr>
				</table>
				<button type="submit">Confirm</button>
			</fieldset>
		</form>
	</div>
</body>
</html>
