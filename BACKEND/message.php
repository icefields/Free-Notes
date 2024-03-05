<?php
$FILENAME = "log.json";
$KEY_ID = 'id';
#$json_string = json_encode($_POST['data']);
$message = $_GET['message']; #file_get_contents('php://input'); #$_POST['data'];
$dateStr = date("Y-m-d")."  ".date("H:i:s");
$title = $_GET['title'];
$action = $_GET['action'];

$json_data = file_get_contents($FILENAME);
$json = json_decode($json_data, true);
$array = $json['data'];
$newObj = array();

if ($action == 'delete') {
	$id = $_GET[$KEY_ID];
	$pos = -1;
	$i = 0;
	while($i < count($array) && $pos == -1) {
	//for ($i = 0; $i < count($array); $i++) {
		if ($array[$i][$KEY_ID] == $id) {
			$pos = $i;	
		}
		++$i;
	}
	if ($pos != -1) {
		array_splice($array, $pos, 1);
		//unset($array[$pos]);
	}
} else {
	$newObj[$KEY_ID] = uniqid();
	$newObj['title'] = $title;
	$newObj['message'] = $message;
	$newObj['date'] = $dateStr;
	array_push($array, $newObj);
}

$json['data'] = $array;

// Convert JSON data from an array to a string
$jsonString = json_encode($json, JSON_PRETTY_PRINT);
// Write in the file
$fp = fopen($FILENAME, 'w');
fwrite($fp, $jsonString);
fclose($fp);

echo $jsonString;
?>
