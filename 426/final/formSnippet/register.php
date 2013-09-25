<form id="register-form" method="post">
	<fieldset class="simple-input-block">
		<legend>Register</legend>
		<input type="hidden" name='account-request' value='register'>
		<table>
			<tr>
				<td>Username</td>
				<td><input type="text" id="uid" name="uid"> <span id="uidBoard"
					class="valid-msg"></span>
				</td>
			</tr>
			<tr>
				<td>Password</td>
				<td><input type="password" id="pw" name="pw">
				</td>
			</tr>
			<tr>
				<td>Confirm Password</td>
				<td><input type="password" id="pw_confirm" name="pw_confirm"></td>
			</tr>
		</table>
		<ul class="msgBoard invalid-msg" id="msgBoard"></ul>
		<button type="submit">Confirm</button>
		<a class="entry-switch" 
			href="entrance.php?entry=login"
			data-target="login">
			Have an account? to log in
		</a>
	</fieldset>
</form>
