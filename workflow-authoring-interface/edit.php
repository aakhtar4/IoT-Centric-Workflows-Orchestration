<?php
	session_start();
	if(isset($_POST['edit'])){
		$Workflow = simplexml_load_file('registration.xml');
		foreach($Workflow->Gamma as $Gamma){
			if($Gamma->attributes()->id == $_POST['Service_ID']){
				$Gamma->Service_Category = $_POST['Service_Category'];
				$Gamma->Service_Type = $_POST['Service_Type'];
				$Gamma->Deployable = $_POST['Deployable'];
				$Gamma->Service_Input = $_POST['Service_Input'];
				$Gamma->Service_Output = $_POST['Service_Output'];
				$Gamma->URI = $_POST['URI'];
				//start of Location=======================================================
				foreach($Gamma-> Location as $Location){
				$Location->Latitude= $_POST['Latitude'];
				$Location->Longitude = $_POST['Longitude'];
				//start of Radius---------------------------------------------------------
				   foreach($Location -> Radius as $Radius){
					$Radius->Value = $_POST['val_radius'];
					if(!empty($Radius->Unit = $_POST['unit_radius'])) {
                 $selected = $_POST['unit_radius'];
                    echo 'You have chosen: ' . $selected;
                     } else {
                      echo 'Please select the Unit.';
                        } 
					//$Radius->Unit = $_POST['unit_radius'];	
				    }//end of radius------------------------------------------------------
				}//end of Location========================================================
				 //start of Availability==================================================
				foreach($Gamma-> Availability as $Availability){
					$Availability->Value=$_POST['val_avail'];
					if(!empty($Availability->Unit = $_POST['unit_avail'])) {
                    $selected = $_POST['unit_avail'];
                    echo 'You have chosen: ' . $selected;
                     } else {
                      echo 'Please select the Unit.';
                        } 
						
				}//end of availability====================================================
				//start of hardware_Requirements==========================================
				foreach($Gamma-> Hardware_Requirements as $Hardware_Requirements){
					$Hardware_Requirements->CPU=$_POST['CPU'];
					$Hardware_Requirements->Network=$_POST['Network'];
					//start of RAM--------------------------------------------------------
					foreach($Hardware_Requirements->RAM as $Ram){
						$Ram->Value=$_POST['val_ram'];
						if(!empty($Ram->Unit=$_POST['unit_ram'])) {
                    $selected = $_POST['unit_ram'];
                    echo 'You have chosen: ' . $selected;
                     } else {
                      echo 'Please select the Unit.';
                        } 
					   
					}//end of RAM--------------------------------------------------------
					//start of External storage------------------------------------------
					foreach($Hardware_Requirements->external_storage as $external_storage){
						$external_storage->Value=$_POST['val_ext'];
					if(!empty($external_storage->Unit=$_POST['unit_ext'])) {
                        $selected = $_POST['unit_ext'];
                        echo 'You have chosen: ' . $selected;
                        } else {
                      echo 'Please select the Unit.';
                        } 
					}//end of external_storage-------------------------------------------
					
				}//end of hardware_Requirements==========================================
				
				//start of Software_Requirements=========================================
				foreach($Gamma-> Software_Requirements as $Software_Requirements){
					if(!empty($Software_Requirements->OS = $_POST['OS'])) {
                        $selected = $_POST['OS'];
                        echo 'You have chosen: ' . $selected;
                        } else {
                      echo 'Please select the OS.';
                        } 
					;
                        //start of packageList--------------------------------------
					    $str_arr = $_POST['Package_List'];
						$myArray = explode(',', $str_arr);
						var_dump($myArray);
						foreach($Software_Requirements-> Package_List as $Package_List){
							unset( $Package_List->Package);
					    
						  for($i= 0;$i<=count($myArray); $i++){
                              var_dump($myArray[$i]);
							  if($myArray[$i]!==null){
							  $Package_List->addChild('Package', $myArray[$i]);}
						 }
                         }//end of packageList-----------------------------------------
				 
				 }//end of software_requirements==============================================
				
				break;
			}
		}

		file_put_contents('registration.xml', $Workflow->asXML());
		$_SESSION['message'] = 'Service updated successfully';
		header('location: serviceList.php');
	}
	else{
		$_SESSION['message'] = 'Select Service to edit first';
		header('location: serviceList.php');
	}

?>