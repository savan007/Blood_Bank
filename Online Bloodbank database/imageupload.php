<?php
include("config.php");
$data = (array)json_decode(file_get_contents("php://input"));
$image=$data['image'];
$userId = $data['userid'];

 function base64_to_jpeg($base64_string, $output_file) {
    $ifp = fopen($output_file, "wb"); 

    $data = explode(',', $base64_string);

    fwrite($ifp, base64_decode($data[1])); 
    fclose($ifp); 

    return $output_file; 
}

$response["success_message"]=$userId;
$response["success_message1"]=$image;
		$response["success"] = true;
		$response["status_Code"]=200;
		print(json_encode($response));
exit;

	$filename_path = md5(time().uniqid()).".jpg"; 
	$decoded=base64_decode($image);
	file_put_contents("uploads/".$filename_path,$decoded);
	
	$sql = "INSERT INTO profilePicture (profile_picture,image_id,user_id) VALUES ('$filename_path','','$userId')";

	if(mysqli_query($con,$sql)){
		file_put_contents($path,base64_decode($image));
		$response["success_message"]="Successfully Uploaded";
		$response["success"] = true;
		$response["status_Code"]=200;
		print(json_encode($response));
	} else{
		$response["success_message"]="Error while uploading image.";
		$response["success"] = false;
		$response["status_Code"]=503;
		print(json_encode($response));
	}
	mysqli_close($con);
?> 