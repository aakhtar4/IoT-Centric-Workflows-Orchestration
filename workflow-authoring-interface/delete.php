<?php
	session_start();
	$id = $_GET['id'];
	
    $data = file_get_contents('service.json');
	// decode json to associative array
    $json_arr = json_decode($data, true);
	$arr_index = array();
    foreach ($json_arr as $key => $value)
       {
         if ($value['id'] == $id)
       {
           $arr_index[] = $key;
        }
       }
	   
	   foreach ($arr_index as $i)
{
    unset($json_arr[$i]);
}

// rebase array
$json_arr = array_values($json_arr);
$myJSON = json_encode($json_arr);
$myJSON = str_replace("[{","[\n\t{",$myJSON);
$myJSON = str_replace(":\"",": \"",$myJSON);
$myJSON = str_replace(",\"",", \"",$myJSON);
$myJSON = str_replace("},","},\n\n\t",$myJSON);
$myJSON = str_replace("},\n\n\t ","},\n\n\t",$myJSON);
$myJSON = str_replace("}]","}\n\n",$myJSON);
$myJSON = $myJSON . "\t". $pass . "]";
//$myJSON = str_replace("}]","}\n]",$myJSON);
// encode array to json and save to file
file_put_contents('service.json', $myJSON);
	$Workflow = simplexml_load_file('registration.xml');

	//we're are going to create iterator to assign to each user
	$index = 0;
	$i = 0;

	foreach($Workflow->Gamma as $Gamma){
		if($Gamma->attributes()->id == $id){
			$index = $i;
			break;
		}
		$i++;
	}

	unset($Workflow->Gamma[$index]);
	file_put_contents('registration.xml', $Workflow->asXML());

	$_SESSION['message'] = 'Service deleted successfully';
	header('location: serviceList.php');

?>