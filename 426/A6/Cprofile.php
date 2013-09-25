<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Find My Roommate</title>
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
</head>

<body>
	<?php include 'common/banner.php'?>
	<?php include 'common/menu.php'?>
	
	<div class="profile-page content">
		<p class="simple-input-block section-help">
			Please fill out the following form to update your account. An <span
				class="required">&#42;</span> denotes required fields.
		</p>
		<!-- Info Form For Register or Update -->
		<form class="main" action="userRequests.php" method="get">
			<div id="info">
				<div id="basic-info" class="simple-input-block">
					<fieldset class="simple-input-block">
						<legend>Basic Information</legend>
						<table>
							<tr>
								<td>Username</td>
								<td><input type="text" name="uid"> 
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>First Name</td>
								<td><input type="text" name="firstName">
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Last Name</td>
								<td><input type="text" name="lastName">
									<span class="required">*</span></td>
							</tr>
							<tr>
								<td><label>Gender</label></td>
								<td><select name="gender">
										<option value="M">Male</option>
										<option value="F">Female</option>
									</select>
									<span class="required">*</span></td>
							</tr>
							<tr>
								<td>Year</td>
								<td><select name="year">
										<option value="FR">Freshmen</option>
										<option value="SO">Sophomore</option>
										<option value="JR">Junior</option>
										<option value="SR">Senior</option>
										<option value="GR">Graduate</option>
									</select>
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Major 1</td>
								<td><select name="major-1">
										<option value="BCHM">Biochemistry</option>
										<option value="BIOL">Biology</option>
										<option value="BIOS">Biostatistics</option>
										<option value="BSBA">Business Administration</option>
										<option value="BUJO">Business Journalis</option>
										<option value="CHEM">Chemistry</option>
										<option value="COMM">Communication Studies</option>
										<option value="CS">Computer Science</option>
										<option value="ECON">Economics</option>
										<option value="EE">Electronic Engineering</option>
										<option value="NUTR">Health Nutrition</option>
										<option value="MDS">Mathematical Decision Science</option>
										<option value="MATH">Mathematics</option>
										<option value="OR">Operations Research</option>
										<option value="PHIL">Philosophy</option>
										<option value="PHYS">Physics</option>
										<option value="PSYC">Psychology</option>
										<option value="STAT">Statistics</option>
									</select>
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Major 2</td>
								<td><select name="major-2">
										<option value="NULL">None</option>
										<option value="BCHM">Biochemistry</option>
										<option value="BIOL">Biology</option>
										<option value="BIOS">Biostatistics</option>
										<option value="BSBA">Business Administration</option>
										<option value="BUJO">Business Journalis</option>
										<option value="CHEM">Chemistry</option>
										<option value="COMM">Communication Studies</option>
										<option value="CS">Computer Science</option>
										<option value="ECON">Economics</option>
										<option value="EE">Electronic Engineering</option>
										<option value="NUTR">Health Nutrition</option>
										<option value="MDS">Mathematical Decision Science</option>
										<option value="MATH">Mathematics</option>
										<option value="OR">Operations Research</option>
										<option value="PHIL">Philosophy</option>
										<option value="PHYS">Physics</option>
										<option value="PSYC">Psychology</option>
										<option value="STAT">Statistics</option>
									</select>
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Date of Birth</td>
								<td><input type="date" name="DOB">
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Call</td>
								<td><input type="text" name="Call"> 
									<label> public <input type="checkbox" name="isCallPublic"> </label> 
								</td>
							</tr>
							<tr>
								<td>Email</td>
								<td><input type="text" name="Email">
									<label> public <input type="checkbox" name="isEmailPublic" checked> </label> 
									<span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td>Facebook</td>
								<td><input type="text" name="Facebook">
									<label> public <input type="checkbox" name="isFacebookPublic" checked> </label>
								</td>
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
										<img src="./photos/littleEngineerAvatar.jpg"
											alt="Profile Picture" width="150" height="150">
									</figure>
								</td>
								<td>
									<div>&nbsp; &nbsp;Profile pictures are small pictures of
										yourself, which are displayed within your user profile.</div>
									<div>
										&nbsp; &nbsp; <input type="checkbox"> <label>Delete
											current image?</label>
									</div>
								</td>
							</tr>
						</table>
					</fieldset>
				</div>

				<div id="picture-update" class="simple-input-block">
					<fieldset class="simple-input-block">
						<legend>Customize Your Profile Picture</legend>
						<ul class="option">
							<li><label>Enter the URL to the
									Image</label> <br> <input type="text" name="photoURL" value="http://www."
								size="45"></li>
							<li><label>Upload Image From Your
									Computer</label> <br> <input type="file" name="fname"></li>
						</ul>
					</fieldset>
				</div>
			</div>

			<div id="password" class="simple-input-block">
				<fieldset class="simple-input-block">
					<legend>Password</legend>
					<table>
						<tr>
							<td>Current Password</td>
							<td><input type="password"><span class="required">*</span></td>
						</tr>
						<tr>
							<td>New Password</td>
							<td><input type="password" name="newpass"><span class="required">*</span></td>
						</tr>
						<tr>
							<td>Confirm your password</td>
							<td><input type="password"><span class="required">*</span></td>
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
							<td><select name="musicListening">
									<option value="headphone">With headsets on</option>
									<option value="speaker">Speaker</option>
							</select></td>
						</tr>
						<tr>
							<td>Visitors</td>
							<td><select name="visitors">
									<option value="no">No visitors</option>
									<option value="sometimes">Sometimes</option>
									<option value="freq">Frequent Visitors</option>
							</select></td>
						</tr>
						<tr>
							<td>Cleanness</td>
							<td><select name="cleanness">
									<option value="daily">Clean every day</option>
									<option value="weekly">Clean every week</option>
									<option value="never">Never to clean up</option>
							</select></td>
						</tr>
						<tr>
							<td>Sleep Preference</td>
							<td><select name="sleepTime">
									<option value="before12">Before 12AM</option>
									<option value="12-2">12AM-2AM</option>
									<option value="after2">After 2AM</option>
							</select></td>
						</tr>
						<tr>
							<td>Wake Preference</td>
							<td><select name="wakeTime">
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
					<textarea name="aboutMe" rows="15" cols="80">Say something about yourself</textarea>
				</fieldset>
			</div>

			<div id="disclaimer" class="simple-input-block">
				<fieldset class="simple-input-block">
					<legend>Terms of Service</legend>
					<textarea name="tarea" rows="8" cols="80" disabled>
Disclaimer to write, Disclaimer to write, Disclaimer to write 
	Disclaimer to write, Disclaimer to write, Disclaimer to write 
		Disclaimer to write, Disclaimer to write, Disclaimer to write 
			Disclaimer to write, Disclaimer to write, Disclaimer to write 
				Disclaimer to write, Disclaimer to write, Disclaimer to write 
					Disclaimer to write, Disclaimer to write, Disclaimer to write 
						Disclaimer to write, Disclaimer to write, Disclaimer to write 
					Disclaimer to write, Disclaimer to write, Disclaimer to write 
				Disclaimer to write, Disclaimer to write, Disclaimer to write 
			Disclaimer to write, Disclaimer to write, Disclaimer to write 
		Disclaimer to write, Disclaimer to write, Disclaimer to write 
	Disclaimer to write, Disclaimer to write, Disclaimer to write 
Disclaimer to write, Disclaimer to write, Disclaimer to write 
	Disclaimer to write, Disclaimer to write, Disclaimer to write 
		Disclaimer to write, Disclaimer to write, Disclaimer to write 
			Disclaimer to write, Disclaimer to write, Disclaimer to write 
				Disclaimer to write, Disclaimer to write, Disclaimer to write 
					Disclaimer to write, Disclaimer to write, Disclaimer to write 
						Disclaimer to write, Disclaimer to write, Disclaimer to write 
					</textarea>
					<br> <input type="checkbox"> <label><em>I
							have read, and agree to abide by the Find My Roommate rules.</em></label>
				</fieldset>
			</div>

			<div id="bottom-button" class="control-button bottom">
				<button type="reset">Reset</button>
				<button type="submit">Enter Your Information</button>
			</div>

		</form>
	</div>
</body>
</html>