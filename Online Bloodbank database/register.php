<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));
//$data1 = json_decode(file_get_contents("php://input"));

$username=$data['user_name'];
$password = $data['password'];
$email = $data['email_id'];
$firstname = $data['first_name'];
$lastnamae = $data['last_name'];
$mobilenumber = $data['mobile_number'];
$dob = $data['dob'];
$address = $data['address'];
$city = $data['city'];
$state = $data['state'];
$gender = $data['gender'];
$profile_picture = '';
 
// echo json_encode(array('success_message'=>$username,'success'=>false,'status_Code'=>202));
 
/* 406 Not Acceptable = null value
400 Bad Request
404 Not Found
409 Conflict == data already in database while insert data in table
200 OK == success
503 Service Unavailable == success fail
 */
 //insert Query
 
if($username==NULL || $password==NULL || $email==NULL || $firstname==NULL || $lastnamae==NULL || $mobilenumber==NULL || $dob==NULL || $address==NULL || $city==NULL || $state==NULL || $gender==NULL )
{
	$response["success_message"]="Please fill all required fields.";
	$response["success"]=false;
	$response["status_Code"]=406;
        print(json_encode($response));
//	die('Could not connect: ' . mysql_error());
exit;
}
else{
	$sql="SELECT * FROM login where user_name ='$username' AND email_id ='$email'";
	$select = mysql_query($sql);
	$checkUserName='';
	$checkEmail='';
	
	while($row = mysql_fetch_array($select))
	{  
		$checkUserName=$row['user_name'];
		$checkEmail=$row['email_id'];
	}
	
	$sql1="SELECT * FROM login where user_name ='$username' AND email_id <> '$email'";
	$select1 = mysql_query($sql1);
	$checkUserName1='';
	$checkEmail1='';
	
	while($row = mysql_fetch_array($select1))
	{  
		$checkUserName1=$row['user_name'];
		$checkEmail1=$row['email_id'];
	}
	
	$sql2="SELECT * FROM login where user_name <> '$username' AND email_id = '$email'";
	$select2 = mysql_query($sql2);
	$checkUserName2='';
	$checkEmail2='';

	while($row = mysql_fetch_array($select2))
	{  
		$checkUserName2=$row['user_name'];
		$checkEmail2=$row['email_id'];
	}
	
	//echo json_encode(array('data'=>$select));
	if($checkUserName && $checkEmail) {
		$response["success_message"]= "This UserName and Email Id already taken, please select different.";
		$response["success"] = false;
		$response["status_Code"]=409;
		print(json_encode($response)); 
	} else if ($checkUserName1){
		$response["success_message"]= "This UserName already taken, please select different.";
		$response["success"] = false;
		$response["status_Code"]=409;
		print(json_encode($response));
	} else if ($checkEmail2){
		$response["success_message"]= "This Email Id already used, please select different.";
		$response["success"] = false;
		$response["status_Code"]=409;
		print(json_encode($response));
	}else{
		$sql="INSERT INTO login(user_id,user_name,first_name,last_name,address,city,state,mobile_number,email_id,dob,gender,password)values('','$username','$firstname','$lastnamae','$address','$city','$state','$mobilenumber','$email','$dob','$gender','$password')";
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
}
//echo json_encode(array('data'=>$response));
?>