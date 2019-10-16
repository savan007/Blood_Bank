<?php
  
  header('Content-Type: application/json');
error_reporting(0);
 $dbhost = 'mysql1.000webhost.com';
   $dbuser = 'a6916234_booksk';
   $dbpass = 'qwertyuiop0';
   $conn = mysql_connect($dbhost, $dbuser, $dbpass);
   
   if(! $conn ) {
      die('Could not connect: ' . mysql_error());
   }
   
   mysql_select_db('a6916234_booksk');  

  ?>