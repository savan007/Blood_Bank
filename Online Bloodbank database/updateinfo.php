<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));

/*    
$userId=$_REQUEST['userid'];
$firstName = $_REQUEST['firstname'];
$dob = $_REQUEST['dob'];
$lastName = $_REQUEST['lastname'];
$mobileNumber = $_REQUEST['mobilenumber'];
$address = $_REQUEST['address'];
$city = $_REQUEST['city'];
$state = $_REQUEST['state'];
$profile_picture = ''; 
 */
$userId = $data['userid'];
$firstName = $data['firstname'];
$lastName = $data['lastname'];
$mobileNumber = $data['mobilenumber'];
$dob = $data['dob'];
$address = $data['address'];
$city = $data['city'];
$state = $data['state'];
 

	$sql ="UPDATE `login` SET  first_name =  '$firstName',
								last_name ='$lastName' ,
								address = '$address',
								city = '$city',
								state = '$state',
								mobile_number = '$mobileNumber',
								dob = '$dob' WHERE `user_id` = '$userId';
								";
	$update = mysql_query($sql);
	
		
	if(!$update)
	{
		$response["success_message"]="Problem while updating data";
		$response["success"] = false;
		$response["status_Code"]=503;
		print(json_encode($response));
	} else {
		$response["success_message"]="Record inserted successfully";
		$response["success"] = true;
		$response["status_Code"]=200;
		print(json_encode($response));
	}
	

//echo json_encode(array('data'=>$response));
?>