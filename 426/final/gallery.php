<!DOCTYPE html>
<?php 
	session_start();
	$request_token = 'main'; //default to be main
	$is_session_valid = 
			isset($_COOKIE['USERAUTH']) && 
					$_COOKIE['USERAUTH'] == md5(
						$_SESSION['uid'] .
						$_SERVER['REMOTE_ADDR'] .
						$_SESSION['authsalt']
					);
	
	if ($_GET['form'] == 'account' || $_GET['form'] == '1'){
		$request_token = 'account';
	}
	else if ($_GET['form'] == 'profile' || $_GET['form'] == '2'){
		$request_token = 'profile';
	}
	else if ($_GET['form'] == 'dormCart' || $_GET['form'] == '3'){
		$request_token = 'dormCart';
	}
	
	if (!$is_session_valid){
		$request_token = 'malSession';
		header('Refresh: 5; URL=entrance.php?entry=login');
	}
?>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Find My Roommate - </title>
		<base href="https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/final/">
		<link rel="stylesheet" type="text/css" href="CSS/body.css">
		<link rel="stylesheet" type="text/css" href="CSS/banner.css">
		<link rel="stylesheet" type="text/css" href="CSS/menu.css">
		<link rel="stylesheet" type="text/css" href="CSS/content.css">
		<link rel="stylesheet" type="text/css" href="CSS/fieldset.css">
		<link rel="stylesheet" type="text/css" href="CSS/<?php echo $request_token;?>.css">
	
		<script type="text/javascript" src="JS/jquery-1.8.2.js"></script>
		<script type="text/javascript" src="JS/dormInfo.js"></script>
		<script type="text/javascript" src="JS/<?php echo $request_token;?>.js"></script>
		
		<?php if($_GET['form'] == 'account') :?>
			<script type="text/javascript" src="JS/uid_pw_checker.js"></script>
		<?php endif;?>
		<?php if($_GET['form'] == 'main') :?>
			<script type="text/javascript" src="JS/sampleSnippet.js"></script>
		<?php endif;?>
		
	</head>

	<body>
		<?php include_once 'common/banner.php';?>
		<?php include_once 'common/menu.php';?>
		<div class="content" id="<?php echo $request_token?>-div">
			<?php include 'formSnippet/' . $request_token . '.php';?>
		</div>
	</body>
</html>
