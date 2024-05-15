<!DOCTYPE html>
<html>
<head>
    <style>
        body {
	    background-color: black;
            color: white;
	}
    </style>
</head>
<body>
<?php
// Get the value of 'param1' from the URL, if it exists
$param_value1 = isset($_GET['collection']) ? $_GET['collection'] : '';


// Construct the URL with the provided parameter values
$url = 'http://localhost/freenotes/api.php?action=view&username=' . urlencode($param_value1);

// Fetch JSON data from the URL
$json_data = file_get_contents($url);
$data = json_decode($json_data, true);

// Check if JSON data was successfully retrieved
if ($data === null) {
    echo "Error fetching JSON data";
    exit;
}

// Display JSON data in a table
echo '<table border="1">';
echo '<tr><th>Title</th><th>Message</th><th>Date Created</th><th>Date Modified</th></tr>';
foreach ($data['data'] as $item) {
    echo '<tr>';
    //echo '<td>' . htmlspecialchars($item['id']) . '</td>';
    echo '<td>' . htmlspecialchars($item['title']) . '</td>';
    echo '<td>' . htmlspecialchars($item['message']) . '</td>';
    echo '<td>' . date('Y-m-d H:i:s', strtotime($item['date_created'])) . '</td>';
    echo '<td>' . date('Y-m-d H:i:s', strtotime($item['date_modified'])) . '</td>';
    echo '</tr>';
}
echo '</table>';
?>
</body>
</html>

