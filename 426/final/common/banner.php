<?php session_start();?>
<div id="page-head-div">
	<div id="page-head-title" 
		class="banner-button">
		Find My Roommate @ UNC Campus
	</div>
	<div id="page-head-greetings" 
		class="banner-button">
		Welcome to Join us, 
		<span id='uid-banner'>
			<?php echo $_SESSION['uid']?>!
		</span>
		</div>
</div>
