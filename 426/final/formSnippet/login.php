<form id="login-form" class="clear"
	action="PHPScripts/accountService.php" method="post">
	<fieldset>
		<legend>Log In</legend>
		<span id="msgBoard" style="color: red;"></span> <input type="hidden"
			id="account-request" name='account-request' value='log-in'>
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
				<td><button type="submit">Log In</button>
				</td>
				<td><a class="entry-switch" data-target="register" href="entrance.php?entry=register">To Sign Up</a></td>
			</tr>
			<tr>
				<td colspan="2"><a href="info.html">Forgot your password?</a>
				</td>
			</tr>
		</table>
	</fieldset>
</form>