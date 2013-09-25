<!DOCTYPE html>
<html>
<head>
	<title>TEST Dorm submission</title>
	<script type="text/javascript" src="../JS/jquery-1.8.2.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#toMainPage").on('click', function(){
				window.location.replace("MainPage.php");
			});
		});
	</script>
</head>
<?php 
	$userDB = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
	$uid = 'fencecracker';//$_SESSION['uid']
	$selected = $_POST["dorm"];
	foreach ($selected as $dormIndex => $dormAbbr){
			$userDB->query(
					"INSERT INTO UserDormInterest (
					`uid`,
					`dorm`
			)
					VALUES (
					'".$uid."',
					'".$dormAbbr."'
			)"
		);
	}
?>
<body>
	<div>
		<p>Dear <?php echo($uid) ?>,</p> 
		<p>&nbsp; &nbsp; &nbsp; &nbsp; You have selected the following dorm(s): </p>
		<ul>
			<?php foreach ($_POST["dorm"] as $abbr) {
				echo('<li>'.$abbr.'</li>');
			}
			?>
		</ul>
		<button type="button" id="toMainPage"> Continue </button>
	</div>
</body>

</h1>
</html>