<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));


$userid = $data['userid'];

// $userid = $_REQUEST['userid'];
 
//select query
$sql="SELECT * FROM post_ad pa INNER JOIN login lg ON pa.userId = lg.user_id WHERE pa.userId != '$userid'";

$select = mysql_query($sql);
$postId='';
$firstName='';
$lastName='';
$profilePicture='';

while($row = mysql_fetch_assoc($select))
{  
	$list[]=$row;
	$postId=$row['postId'];
	$firstName=$row['first_name'];
	$lastName=$row['last_name'];
}
if(!$postId) {
	$response["success_message"]= "Post not available.";
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
	$response["postId"]=$postId;
	$response["data"]=$list;
	print(json_encode($response)); 
	
}
//echo json_encode(array('username'=>$username,'password'=>$password));
?>