<form id='account-form' method='post'>
	<fieldset class="simple-input-block">
		<legend>FindMyRoommate Account - Update</legend>
		<input type="hidden" id="account-request" name='account-request'
			value='update'>
		<table>
			<tr>
				<td>Username</td>
				<td><input type="text" id="uid" name="uid"
					value="<?php echo $_SESSION['uid'];?>" disabled> <span
					id="uidBoard" class="valid-msg"></span>
				</td>
			</tr>
			<tr>
				<td colspan=2><input type="checkbox" id="uid-change"
					name="uid-change" value="on"> <label for="uid-change">Check to
						change Your Username</label>
				</td>
			</tr>
			<tr>
				<td>Current Password</td>
				<td><input type="password" id='pw' name="pw">
				</td>
			</tr>
			<tr>
				<td>New Password</td>
				<td><input type="password" id='npw' name="npw">
				</td>
			</tr>
			<tr>
				<td>Confirm your password</td>
				<td><input type="password" id='npw-confirm' name="npw-confirm"></td>
			</tr>
			<tr>
				<td colspan=2><input type="checkbox" id="delete-profile"
					name="delete-profile" value="on"> <label for="delete-profile">Check
						to delete Your Profile</label>
				</td>
			</tr>
		</table>
		<ul id="msgBoard" class="msgBoard invalid-msg"></ul>
		<button type="submit">Confirm</button>
	</fieldset>
</form>
