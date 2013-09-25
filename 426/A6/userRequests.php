<?php
require_once('User.php');

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
	if (!is_null($_GET['delete'])){
		$uid = $_GET['uid'];
		$user=User::findByUID($uid);
		if(!is_null($user)){
			$result=$user->delete();
			header("Content-type: application/json");
      		print(json_encode(true));
		}
		exit();
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
    $major = $_POST['major-1'];
    $dob = $_POST['DOB'];
    $photo = $_POST['photoURL'];
    $about = $_POST['aboutMe'];
    $music = $_POST['musicListening'];
    $visitor = $_POST['visitors'];
    $clean = $_POST['cleanness'];
    $sleep = $_POST['sleepTime'];
    $wake = $_POST['wakeTime'];
    
    $user=User::findByUID($uid);
    if(!is_null($user)){
    	$result=$user->updateUserInfo($first,$last,$gender,$dob,$year,$major,$music,
	  							   $visitor,$clean,$sleep,$wake,$about,$photo);
    	if(!$result){
    		header("HTTP/1.1 400 Bad Request");
    		print("User failed at database");
    		exit();
    	}
    }
    else{
	    $user = User::create($uid,$first,$last,$gender,$dob,$year,$major,$music,
	  							   $visitor,$clean,$sleep,$wake,$about,$photo);
	    if (is_null($user)) {
	      header("HTTP/1.1 400 Bad Request");
	      print("User failed at database");
	      exit();
	    }
    }
    
    header("Content-type: application/json");
    echo(json_encode($user->getJSON()));
    exit();
    }
}else {
	//TODO
	header("HTTP/1.1 400 Bad Request");
	print("URL did not match any known action.");
}

?>