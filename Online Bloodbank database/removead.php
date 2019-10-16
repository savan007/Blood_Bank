<?php

include('config.php');
$data = (array)json_decode(file_get_contents("php://input"));

$userid = $_REQUEST['userid'];
$postid = $_REQUEST['postid'];

$sql="DELETE FROM post_ad WHERE userId ='$userid' AND postId ='$postid'";
$result = mysql_query($sql);

$sql = "SELECT * FROM post_ad where userId ='$userid' AND postId ='$postid'";
$delete = mysql_query($sql);
$userId='';

while($row = mysql_fetch_assoc($delete))
{  
	$response["AdInfo"]=$row;
	$userId=$row['userId'];
}

if(!$userId) {
	$response["success_message"]="Ad deleted successfully.";
	$response["success"] = true;
	$response["status_Code"]=200;
}
else{
	$response["success_message"]="Problem in deleting Ad.Please try again.";
	$response["success"] = false;
	$response["status_Code"]=503;
	print(json_encode($response));
}
?>