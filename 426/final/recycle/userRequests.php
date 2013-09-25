<?php
session_start();
require_once('class/User.php');
require_once('class/UserPass.php');

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
	if (!is_null($_GET['delete'])){
		if (isset($_COOKIE['USERAUTH']) &&
				($_COOKIE['USERAUTH'] == md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
			$uid = $_SESSION['uid'];
			$user=User::findByUID($uid);
			if(!is_null($user)){
				$result=$user->delete();
				header("Content-type: application/json");
				print(json_encode(true));
				session_destroy();
			}
			else{
				header("Content-type: application/json");
				header("HTTP/1.1 401 Invalid Username");
				print(json_encode(false));
			}
		}
		else{
			header("Content-type: application/json");
			header("HTTP/1.1 402 Session Lost");
			print(json_encode(false));
		}
	}
	else{	
	    $user_array = User::retrieveAll();
	
	    if (is_null($user_array)) {
	      // Something went wrong. Return error.
	      header("HTTP/1.1 400 Bad Request");
	      print("Retrieving failed in Database");
	      exit();
	    }
	
	    header("Content-type: application/json");
	    foreach ($user_array as $user) {
	      echo(json_encode($user->getJSON()));
	    }
	    exit();
	}
    
} else if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  if (is_null($_SERVER['PATH_INFO'])) {
    $uid = $_POST['uid'];
    if (is_null($uid)) {
    	// Something went wrong. Return error.
    	header("HTTP/1.1 400 Bad Request");
    	print("Error: please specify a username! ");
    	exit();
    }
    $first = $_POST['firstName'];
    $last = $_POST['lastName'];
    $gender = $_POST['gender'];
    $year = $_POST['year'];
    $major1 = $_POST['major-1'];
    $major2 = $_POST['major-2'];
    $dob = $_POST['DOB'];
    $photo = $_POST['photoURL'];
    $about = $_POST['aboutMe'];
    $music = $_POST['musicListening'];
    $visitor = $_POST['visitors'];
    $clean = $_POST['cleanness'];
    $sleep = $_POST['sleepTime'];
    $wake = $_POST['wakeTime'];
    $call=$_POST['Call'];
    $email=$_POST['Email'];
    $facebook=$_POST['Facebook'];
    $oldPass=$_POST['oldpass'];
    $newPass=$_POST['newpass'];
    $user=User::findByUID($uid);
    if(!is_null($user)){
    	$result=$user->updateUserInfo($uid,$first,$last,$gender,$dob,$year,$major1,$major2,$music,
	  							   $visitor,$clean,$sleep,$wake,$about,$photo,$call,$email,$facebook);   	
    	if(!$result){
    		header("HTTP/1.1 400 Bad Request");
    		print("User failed at database");
    		exit();
    	}
    	
    	//check if current passwor match with DB records
    	if(isset($newPass)){
	    	$usrp = new UserPass($uid);
	    	$status = $usrp->check_pw_with_DB($oldPass);
	    	if ($status){
	    		$usrp->write_hash_to_DB($newPass);
	    	}
	    	else{
	    		header("HTTP/1.1 405 Bad Request");
	    		print("Incorrect current password.");
	    		exit();
	    	}
    	}
    	}
    else{
	    $user = User::create($uid,$first,$last,$gender,$dob,$year,$major1,$major2,$music,
	  							   $visitor,$clean,$sleep,$wake,$about,$photo,$call,$email,$facebook);
	    if (is_null($user)) {
	      header("HTTP/1.1 400 Bad Request");
	      print("User failed at database");
	      exit();
	    }
	    
	    if(isset($newPass)){
	    	$usrp = new UserPass($uid);
	    	$usrp->write_hash_to_DB($newPass);
	    }
	    
	    header('Content-type: application/json');
	    // Generate authorization cookie
	    $_SESSION['uid'] = $uid;
	    $_SESSION['authsalt'] = time();	    
	    $auth_cookie_val = md5($_SESSION['uid'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']);	    
	    setcookie('USERAUTH', $auth_cookie_val, 0, '/Courses/comp426-f12/duozhao/final', 'wwwp.cs.unc.edu', true);
    }
    
    header("Content-type: application/json");
    echo(json_encode($user->getJSON()));
    header('Location: https://wwwp.cs.unc.edu/Courses/comp426-f12/duozhao/final/MainPage.php' );
    exit();
    }
}else {
	//TODO
	header("HTTP/1.1 403 Bad Request");
	print("URL did not match any known action.");
}

?>