<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));


$userid = $data['userid'];

// $userid = $_REQUEST['userid'];
 
//select query
$sql="SELECT * FROM post_ad where userId ='$userid'";
$sql1="SELECT * FROM login where user_id ='$userid'";
$sql2="SELECT * FROM profilepicture where user_id ='$userid'";

$select = mysql_query($sql);
$select1 = mysql_query($sql1);
$select2 = mysql_query($sql2);
$user_id='';
$firstName='';
$lastName='';
$profilePicture='';

while($row = mysql_fetch_assoc($select))
{  
	$list[]=$row;
	$user_id=$row['userId'];
}
while($row = mysql_fetch_assoc($select1))
{  
	$firstName=$row['first_name'];
	$lastName=$row['last_name'];
}
while($row = mysql_fetch_assoc($select2))
{  
	$profilePicture=$row['profile_picture'];
}
if(!$user_id) {
	$response["success_message"]= "User not exist.";
	$response["success"] = false;
	$response["status_Code"]=404;
	print(json_encode($response)); 
}
else
{
	$response["success_message"]= "Success.";
	$response["success"] = true;
	$response["status_Code"]=200;
	$response["firstname"]=$firstName;
	$response["lastname"]=$lastName;
	$response["profile_picture"]=$profilePicture;
	$response["data"]=$list;
	print(json_encode($response)); 
	
}
//echo json_encode(array('username'=>$username,'password'=>$password));
?>