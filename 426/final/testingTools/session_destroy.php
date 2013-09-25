<?php 
session_start();
var_dump($_SESSION['uid']);
session_destroy();
echo 'Session exited';
?>