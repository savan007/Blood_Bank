<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));
     
$username = $_REQUEST['user_name'];
$password = $_REQUEST['password']; 

//select query
$sql="SELECT * FROM login where user_name ='$username' AND password ='$password'";

$select = mysql_query($sql);
$user_id='';

while($row = mysql_fetch_assoc($select))
{  
	$response["UserInfo"]=$row;
	$user_id=$row['user_id'];
}
if(!$user_id) {
	$response["success_message"]= "Login Failed..! Please check your credential.";
	$response["success"] = false;
	$response["status_Code"]=503;
	$response["user_id"]=$user_id;
        $response["user_name"]=$username;
	$response["password"]=$password;
	print(json_encode($response)); 
}
else
{
	$response["success_message"]= "Login successful.";
	$response["success"] = true;
	$response["status_Code"]=200;
	$response["user_id"]=$user_id;
	print(json_encode($response)); 
	
}
//echo json_encode(array('username'=>$username,'password'=>$password));
?>