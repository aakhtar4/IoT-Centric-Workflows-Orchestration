<?php
header('Content-Type: text/plain');
$pass = '';
$spec = '';
if(!empty($_GET)){ //for categories
    $pass = $_GET['passer'];
}
else { //for service and specifications
    $spec = $_POST['specs'];
    list($pass, $spec) = explode('~', $spec);
}
$jsonString = file_get_contents('service.json');
$data = json_decode($jsonString, true);

//to add services
$myJSON = json_encode($data);
$myJSON = str_replace("[{","[\n\t{",$myJSON);
$myJSON = str_replace(":\"",": \"",$myJSON);
$myJSON = str_replace(",\"",", \"",$myJSON);
$myJSON = str_replace("},","},\n\n\t",$myJSON);
$myJSON = str_replace("},\n\n\t ","},\n\n\t",$myJSON);
$myJSON = str_replace("}]","},\n\n",$myJSON);
$myJSON = $myJSON . "\t". $pass . "]";
$myJSON = str_replace("}]","}\n]",$myJSON);

file_put_contents('service.json',$myJSON);
clearstatcache(true,'Service_Registration.html');
header("Cache-Control: no-cache, no-store, must-revalidate"); // HTTP 1.1.
sleep(3);
$jsonString2 = file_get_contents('registration.xml');
$data2 = json_decode($jsonString2, true);
$myJSON2 = json_encode($data2);
if(!file_exists('registration.xml')){
    echo 'file does not exist!';
    $jsonString2 = '<?xml version="1.0"?>' . "\n" . "<Workflow>" . $spec . "</Workflow>";
}
else {
    echo 'file already exists!';
    $jsonString2 = str_replace("</Workflow>", $spec, $jsonString2);
    $jsonString2 = $jsonString2 . "</Workflow>";
}
//to write registration file
file_put_contents('registration.xml',$jsonString2);
header("Location: Service_Registration.php");