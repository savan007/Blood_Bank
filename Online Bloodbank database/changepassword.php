<?php

include('config.php');
$data = (array)json_decode(file_get_contents("php://input"));


$username = $_REQUEST['user_name'];
$password = $_REQUEST['password'];
$newpassword = $_REQUEST['newpassword'];

$sql="SELECT * FROM login WHERE user_name ='$username' AND password ='$password'";
$select = mysql_query($sql);
$userId='';

while($row = mysql_fetch_assoc($select))
{  
	$response["UserInfo"]=$row;
	$userId=$row['user_id'];
}

if(!$userId) {
	$response["success_message"]= "Password does not match.";
	$response["success"] = false;
	$response["status_Code"]=503;
	$response["user_id"]=$user_id;
    $response["user_name"]=$username;
	$response["password"]=$password;
	print(json_encode($response));
}
else{
	$sql1 = "UPDATE `login` SET  `password` =  '$newpassword' WHERE `user_id` = '$userId';";
	$update = mysql_query($sql1);
	
	if(!$update)
	{
		$response["success_message"]="Problem in changing password";
		$response["success"] = false;
		$response["status_Code"]=503;
		print(json_encode($response));
	} else {
		$response["success_message"]="Password changed successfully";
		$response["success"] = true;
		$response["status_Code"]=200;
		print(json_encode($response));
	}
}
?>