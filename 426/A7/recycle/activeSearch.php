<!DOCTYPE html>
<html>
<head>
<title>Active Search Feedback</title>
</head>
<?php
session_start();
require_once 'class/User.php';
require_once 'class/SearchEntry.php';
if (isset($_COOKIE['USERAUTH']) &&
    ($_COOKIE['USERAUTH'] == md5($_SESSION['username'] . $_SERVER['REMOTE_ADDR'] . $_SESSION['authsalt']))) {
	$uid = $_SESSION['username'];
}
$user = User::findByUID($uid);
$db = new mysqli("classroom.cs.unc.edu", "duozhao", "hawk42617", "comp42617db");
$userSearchEntry = SearchEntry::createFromRequest($uid, $_GET);
$userSearchEntry->writeToDB();
$roomies = $userSearchEntry->findRoomie(0, 5);

?>
<body>
	<div>
		<p><?php var_dump($userSearchEntry->uid); ?></p>
		<p><?php var_dump($userSearchEntry->isInDB()); ?></p>
		<p><?php var_dump($roomies); ?></p>
		<?php foreach($roomies as $roomieID => $score){
					echo '<p>' . $roomieID . ':' . $score . '</p>';
					$roomie = User::findByUID($roomieID);
					echo var_dump($roomie->getJSON());
				}
		?>
	</div>
	<div class="search-result-item">
		<div>
			<div class="ranking">1</div>
			<figure>
				<img src=<?php echo('"photos/users/' . $user->getPhoto() . '"'); ?> 
					alt="<?php echo $user->getFirst() . " " . $user->getLast(); ?>'s profile picture"
					height="150" width="150">
			</figure>
		</div>
		
		<div>
			<div class="Name">
				<?php echo $user->getFirst() . " " . $user->getLast(); ?>
			</div>
			<div class="stats">
				<span class="gender">Gender:</span> 
					<?php echo $user->getGender(); ?>
				<span class="age">Age:</span>
					<?php echo $user->getDOB(); ?>
				<span class="year">Year:</span> 
					<?php echo $user->getYear(); ?> 
				<span class="major">Major:</span> 
					<?php $major = $user->getMajor();
						echo $major[0] . ", ". $major[1]; ?>
			</div>
			<div>
				<table class="preference-table">
					<tbody>
						<tr>
							<td><span>Music Listening: </span>
								<?php echo $user->getMusicListening(); ?>
							</td>
							<td><span>Visitor:</span>
								<?php echo $user->getVisitors(); ?>
							</td>
						</tr>
						<tr>
							<td><span>Sleep Preference:</span>
								<?php echo $user->getSleepTime(); ?>
							</td>
							<td><span>Wake Preference:</span>
								<?php echo $user->getWakeTime(); ?>
							</td>
						</tr>
						<tr>
							<td><span>Cleanness: </span>
								<?php echo $user->getCleanness(); ?>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
