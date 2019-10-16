<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));


/* $userid = $_REQUEST['userid'];
$date = $_REQUEST['date'];
$time = $_REQUEST['time'];
$category=$_REQUEST['category'];
$subject = $_REQUEST['subject'];
$bookname = $_REQUEST['bookname'];
$authorname = $_REQUEST['authorname'];
$description = $_REQUEST['description'];
$cost = $_REQUEST['cost']; */
 
$userid = $data['userid'];
$subject = $data['subject'];
$bookname = $data['bookname'];
$authorname = $data['authorname'];
$cost = $data['cost'];
$description = $data['description'];
$date = $data['date'];
$time = $data['time'];
$category=$data['category'];

/* 406 Not Acceptable = null value
400 Bad Request
404 Not Found
409 Conflict == data already in database while insert data in table
200 OK == success
503 Service Unavailable == success fail
 */
 //insert Query
 
if($userid==NULL || $date==NULL || $time==NULL || $category==NULL || $subject==NULL || $bookname==NULL || $authorname==NULL || $description==NULL || $cost==NULL )
{
	$response["success_message"]="Please fill all required fields.";
	$response["success"]=false;
	$response["status_Code"]=406;
    print(json_encode($response));
	die('Could not connect: ' . mysql_error());
}
else{
	$sql="INSERT INTO post_ad(userId,Subject,bookName,authorName,price,description,bookImage,postId,date,time,category)values
	('$userid','$subject','$bookname','$authorname','$cost','$description','','','$date','$time','$category')";
	$insert = mysql_query($sql);
	
	if(!$insert)
	{
		$response["success_message"]="Problem while inserting data";
		$response["success"] = false;
		$response["status_Code"]=503;
		print(json_encode($response));
	} else {
		$response["success_message"]="Record inserted successfully";
		$response["success"] = true;
		$response["status_Code"]=200;
		print(json_encode($response));
	}
}
//echo json_encode(array('data'=>$response));
?>