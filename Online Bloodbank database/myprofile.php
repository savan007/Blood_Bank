<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));

$userid = $data['userid'];
   
//$userid = $_REQUEST['userid'];
 
//select query
$sql="SELECT * FROM login where user_id ='$userid'";

$select = mysql_query($sql);
$user_id='';

while($row = mysql_fetch_assoc($select))
{  
	$response["UserData"]=$row;
	$user_id=$row['user_id'];
}
if(!$user_id) {
	$response["success_message"]= "User not exist.";
	$response["success"] = false;
	$response["status_Code"]=404;
	$response["user_id"]=$user_id;
	print(json_encode($response)); 
}
else
{
	$response["success_message"]= "Success.";
	$response["success"] = true;
	$response["status_Code"]=200;
	$response["user_id"]=$user_id;
	print(json_encode($response)); 
	
}
//echo json_encode(array('username'=>$username,'password'=>$password));
?>