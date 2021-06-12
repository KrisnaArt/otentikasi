<?php
 if($_SERVER['REQUEST_METHOD']=='POST'){
  	// echo $_SERVER["DOCUMENT_ROOT"];  // /home1/demonuts/public_html
	//including the database connection file
  	include_once("config.php");
  	  	
  	//$_FILES['image']['name']   give original name from parameter where 'image' == parametername eg. city.jpg
  	//$_FILES['image']['tmp_name']  temporary system generated name

	  	$username = $_POST['username'];
        $email = $_POST['email'];
        $pass = $_POST['password'];
  
        $originalImgName= $_FILES['filename']['name'];
        $tempName= $_FILES['filename']['tmp_name'];
        $folder="images/foto_wajah/";
        $url = "https://192.168.1.3/skripsi/images/foto_wajah/".$originalImgName; //update path as per your directory structure 
        
		$originalImgName1= $_FILES['filename1']['name'];
        $tempName1= $_FILES['filename1']['tmp_name'];
		$folder1="images/foto_ktp/";
		$url1 = "https://192.168.1.3/skripsi/images/foto_ktp/".$originalImgName1; //update path as per your directory structure 

        if(move_uploaded_file($tempName,$folder.$originalImgName)&&move_uploaded_file($tempName1,$folder1.$originalImgName1)){
                $query = "INSERT INTO foto_user (id, username, email, password, foto_wajah, foto_ktp, foto_baru) VALUES (null, '$username','$email','$pass','$url','$url1', null)";
                if(mysqli_query($con,$query)){
                	 $query= "SELECT * FROM foto_user WHERE foto_wajah='$url' AND foto_ktp='$url1'";
	                 $result= mysqli_query($con, $query);
	                 $emparray = array();
	                     if(mysqli_num_rows($result) > 0){  
	                     while ($row = mysqli_fetch_assoc($result)) {
                                     $emparray[] = $row;
                                   }
                                   echo json_encode(array( "status" => "true","message" => "Successfully file added!" , "data" => $emparray) );
                                   
	                     }else{
	                     		echo json_encode(array( "status" => "false","message" => "Failed!") );
	                     }
			   
                }else{
                	echo json_encode(array( "status" => "false","message" => "Failed!") );
                }
        	//echo "moved to ".$url;
        }else{
        	echo json_encode(array( "status" => "false","message" => "Failed!") );
        }
  }
?>