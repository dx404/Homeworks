<?php session_start();?>
<div class="menu">
	<fieldset class="menu">
		<legend class="menu"> Menu</legend>
		<span> Hello, <?php echo $_SESSION['uid'];?></span>
		<table class="menu">
			<tr>
				<td><a href="gallery.php?form=account">My Account</a></td>
			</tr>
			<tr>
				<td><a href="gallery.php?form=profile">My Profile</a></td>
			</tr>
			<tr>
				<td><a href="gallery.php?form=dormCart">My Building</a></td>
			</tr>
			<tr>
				<td><a href="gallery.php?form=main">My Roommate</a></td>
			</tr>
			<tr>
				<td><a href="PHPScripts/accountService.php?account-request=sign-out">Sign Out</a></td>
			</tr>
		</table>
	</fieldset>
</div>