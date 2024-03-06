<?php
$START_LOG = 'log.json'; 
$KEY_ID = 'id';
$ACTION_DELETE = 'delete';
$ACTION_EDIT = 'edit';
$ACTION_NEW = 'new';
$ACTION_VIEW = 'view';

$action = $_GET['action'];

$username = $_GET['username'];
if ($username == "freenotes-debug-user.random*maybe.unique.id.to.fetch-default-notes") {
	# readonly account
	#$action = $ACTION_VIEW;	
}
$FILENAME = $username.'.json';
if ($FILENAME == $START_LOG) {
	throw new Exception('Invalid username', 100);
}

# check if file exists, if not create it
if(!is_file($FILENAME)) {
	# read the startup file
	$defaultJson = file_get_contents($START_LOG);
	file_put_contents($FILENAME, $defaultJson);
}

$message = $_GET['message'];
$dateStr = date("Y-m-d")."  ".date("H:i:s");
$title = $_GET['title'];
$tags = $_GET['tags'];
$id = $_GET[$KEY_ID];

#read the current archive of notes
$json_data = file_get_contents($FILENAME);
$json = json_decode($json_data, true);
$array = $json['data'];

switch ($action) {
case $ACTION_VIEW:

	break;
case $ACTION_DELETE:
	$pos = getNoteIndex($id, $array);
	if ($pos > -1) {
		array_splice($array, $pos, 1);
	}
	break;
case $ACTION_EDIT:
	$pos = getNoteIndex($id, $array);
	if ($pos > -1) {
		$noteToEdit = $array[$pos];
		$editedNote = createNewNote($id, $title, $message, $noteToEdit['date_created'], $dateStr, $tags, $array);
		$array[$pos] = $editedNote;
	}
	break;
case $ACTION_NEW:
default:
	if ($id == null) $id = uniqid();
	$newNote = createNewNote($id, $title, $message, $dateStr, $dateStr, $tags, $array);
	array_push($array, $newNote);
}

$json['data'] = $array;
# Convert JSON data from an array to a string
$jsonString = json_encode($json, JSON_PRETTY_PRINT);

# Write in the file
if ($action != $ACTION_VIEW) {
	$fp = fopen($FILENAME, 'w');
	fwrite($fp, $jsonString);
	fclose($fp);
}

header('Content-Type: application/json');
echo $jsonString;

function createNewNote($newId, $newTitle, $newMessage, $newCreateDate, $newModifiedDate, $newTags, $jsonArray) {
	$newObj = array();    
	$newObj['id'] = $newId;
	$newObj['title'] = $newTitle;
	$newObj['message'] = $newMessage;	
	$newObj['tags'] = $newTags;
	$newObj['date_created'] = $newCreateDate;	
	$newObj['date_modified'] = $newModifiedDate;
	return $newObj;
}

function getNoteIndex($noteId, $jsonArray) {
	$p = -1;
	$i = 0;
	while($i < count($jsonArray) && $p == -1) {
		if ($jsonArray[$i]['id'] == $noteId) {
			$p = $i;	
		}
		++$i;
	}
	return $p;
}
?>
