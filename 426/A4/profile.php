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
		<form class="main">
			<div id="info">
				<div id="basic-info" class="simple-input-block">
					<fieldset class="simple-input-block">
						<legend>Basic Information</legend>
						<table>
							<tr>
								<td><label>Username</label></td>
								<td><input type="text" name="username"> <span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td><label>First Name</label></td>
								<td><input type="text" name="first"> <span class="required">*</span>
								</td>
							</tr>
							<tr>
								<td><label>Last Name</label></td>
								<td><input type="text" name="last"> <span class="required">*</span></td>
							</tr>
							<tr>
								<td><label>Sex</label></td>
								<td><select name="gender">
										<option>Male</option>
										<option>Female</option>
								</select><span class="required">*</span></td>
							</tr>
							<tr>
								<td><label>Year</label></td>
								<td><select name="year">
										<option>Freshmen</option>
										<option>Sophomore</option>
										<option>Junior</option>
										<option>Senior</option>
										<option>Graduate</option>
										<option>Other</option>
								</select></td>
							</tr>
							<tr>
								<td><label>Major</label></td>
								<td><select name="major">
										<option>Computer Science</option>
										<option>Electronic Engineering</option>
										<option>Mathematics</option>
										<option>Statistics</option>
										<option>Physics</option>
										<option>Chemistry</option>
										<option>Biology</option>
										<option>Business</option>
										<option>Others</option>
								</select></td>
							</tr>
							<tr>
								<td><label>Date of Birth</label></td>
								<td><input type="date" name="DOB"></td>
							</tr>
							<tr>
								<td><label>Call</label></td>
								<td><input type="text"> <label>public</label> <input
									type="checkbox"></td>
							</tr>
							<tr>
								<td><label>Email</label></td>
								<td><input type="text"> <label>public</label> <input
									type="checkbox" checked> <span class="required">*</span></td>
							</tr>
							<tr>
								<td><label>Facebook</label></td>
								<td><input type="text"> <label>public</label> <input
									type="checkbox" checked></td>
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
									Image</label> <br> <input type="text" value="http://www." name="photo"
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
							<td><select name="music-listening">
									<option>With headsets on</option>
									<option>Speaker</option>
							</select></td>
						</tr>
						<tr>
							<td>Visitors</td>
							<td><select name="Visitors">
									<option>No visitors</option>
									<option>Frequent Visitors</option>
									<option>Party Everyday</option>
							</select></td>
						</tr>
						<tr>
							<td>Cleanness</td>
							<td><select name="cleanness">
									<option>Clean everyday</option>
									<option>Clean every week</option>
									<option>No often</option>
							</select></td>
						</tr>
						<tr>
							<td>Sleep Preference</td>
							<td><select name="sleeping-preference"><option>Before
										12AM</option>
									<option>12AM-2AM</option>
									<option>After 2AM</option>
							</select></td>
						</tr>
						<tr>
							<td>Wake Preference</td>
							<td><select name="wake-preference">
									<option>Before 7AM</option>
									<option>7AM-9AM</option>
									<option>After 9AM</option>
							</select></td>
						</tr>
<!-- 						<tr> -->
<!-- 							<td>Study Habits</td> -->
<!-- 							<td><select name="study-preference"> -->
<!-- 									<option>Study in my room</option> -->
<!-- 									<option>Study in library</option> -->
<!-- 							</select></td> -->
<!-- 						</tr> -->
					</table>
				</fieldset>
			</div>

			<div id="about-me" class="simple-input-block">
				<fieldset class="simple-input-block">
					<legend>About Me</legend>
					<textarea name="tarea" rows="15" cols="80">Say something about yourself</textarea>
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