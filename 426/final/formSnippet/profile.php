<?php session_start();?>
<form id="profile-form" class="main" action="PHPScripts/profileService.php" method="post">
	<p class="simple-input-block section-help">
		Please fill out the following form to update your account. An <span
			class="required">&#42;</span> denotes required fields.
	</p>
	<div id="basic-info" class="simple-input-block">
		<fieldset class="simple-input-block">
			<legend>Basic Information</legend>
			<input type="hidden" id="uid" name="uid"
				value=<?php echo $_SESSION['uid'];?>>
			<table>
				<tr>
					<td>First Name</td>
					<td><input class="required" type="text" id="firstName"
						name="firstName">
					</td>
				</tr>
				<tr>
					<td>Last Name</td>
					<td><input class="required" type="text" id="lastName"
						name="lastName"></td>
				</tr>
				<tr>
					<td><label>Gender</label></td>
					<td><select id="gender" name="gender">
							<option value="M">Male</option>
							<option value="F">Female</option>
					</select></td>
				</tr>
				<tr>
					<td>Year</td>
					<td><select class="required" id="year" name="year">
							<option value="FR">Freshmen</option>
							<option value="SO">Sophomore</option>
							<option value="JR">Junior</option>
							<option value="SR">Senior</option>
							<option value="GR">Graduate</option>
					</select></td>
				</tr>
				<tr>
					<td>Major 1</td>
					<td><select id="major1" name="major1">
							<?php include 'common/majorOptions.php';?>
					</select></td>
				</tr>
				<tr>
					<td>Major 2</td>
					<td><select id="major2" name="major2">
							<option value="NULL">None</option>
							<?php include 'common/majorOptions.php';?>
					</select></td>
				</tr>
				<tr>
					<td>Date of Birth</td>
					<td><input type="date" id="DOB" name="DOB"></td>
				</tr>
				<tr>
					<td>Call</td>
					<td><input type="text" id="call" name="call"></td>
				</tr>
				<tr>
					<td>Email</td>
					<td><input type="text" id="email" name="email"></td>
				</tr>
				<tr>
					<td>Facebook</td>
					<td><input type="text" id="facebook" name="facebook"></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="profile-picture" class="simple-input-block">
		<fieldset class="simple-input-block">
			<legend>Your Current Profile Picture</legend>
			<table>
				<tr>
					<td>
						<figure>
							<img id="previewPhoto" src="image/users/default/littleEngineerAvatar.jpg"
								alt="Profile Picture" width="150" height="150">
						</figure>
					</td>
					<td>
						<div>&nbsp; &nbsp;Profile pictures are small pictures of yourself,
							which are displayed within your user profile.</div>
					</td>
				</tr>
				<tr>
					<td colspan=2>
						<label for="profilePhotoURL"> Please enter an URL to Your Photo </label><br>
						<textarea  
							id="profilePhotoURL" 
							name="profilePhotoURL"
							rows="5" cols="50" >
								http://i943.photobucket.com/albums/ad272/spacepure/homelesstoharvard.jpg
						</textarea><br>
						<button type="button" id="testPhoto">Test Your Link</button>
					</td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="living-habits" class="simple-input-block">
		<fieldset class="simple-input-block">
			<legend>Living Habits</legend>
			<table>
				<tr>
					<td>Music Listening</td>
					<td><select id="musicListening" name="musicListening">
							<option value="headphone">With headsets on</option>
							<option value="speaker">Speaker</option>
					</select></td>
				</tr>
				<tr>
					<td>Visitors</td>
					<td><select id="visitors" name="visitors">
							<option value="no">No visitors</option>
							<option value="sometimes">Sometimes</option>
							<option value="freq">Frequent Visitors</option>
					</select></td>
				</tr>
				<tr>
					<td>Cleanness</td>
					<td><select id="cleanness" name="cleanness">
							<option value="daily">Clean every day</option>
							<option value="weekly">Clean every week</option>
							<option value="never">Never to clean up</option>
					</select></td>
				</tr>
				<tr>
					<td>Sleep Preference</td>
					<td><select id="sleepTime" name="sleepTime">
							<option value="before12">Before 12AM</option>
							<option value="12-2">12AM-2AM</option>
							<option value="after2">After 2AM</option>
					</select></td>
				</tr>
				<tr>
					<td>Wake Preference</td>
					<td><select id="wakeTime" name="wakeTime">
							<option value="before7">Before 7AM</option>
							<option value="7-9">7AM-9AM</option>
							<option value="after9">After 9AM</option>
					</select></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<div id="about-me" class="simple-input-block">
		<fieldset class="simple-input-block">
			<legend>About Me</legend>
			<textarea id="aboutMe" name="aboutMe" rows="15" cols="80">Say something about yourself</textarea>
		</fieldset>
	</div>

	<div id="disclaimer" class="simple-input-block">
		<fieldset class="simple-input-block">
			<legend>Terms of Service</legend>
			<textarea name="tarea" rows="8" cols="80" disabled>
						<?php include 'common/disclaimer.php';?>
					</textarea><br>
			<input id="disclaimer-check" type="checkbox">
			<label for="disclaimer-check">
				<em>I have read, and agree to abide by the Find My Roommate rules.</em>
			</label>
		</fieldset>
	</div>

	<div id="bottom-button" class="control-button bottom">
		<button id="profile-reset-button" type="reset">Reset</button>
		<button id="profile-submit-button" type="submit" disabled="disabled">
			Enter Your Information 
			<span id="agreement-checkbox-remainder">
				<br>(Please agree to terms of service first)
			<span>
		</button>
	</div>
</form>
