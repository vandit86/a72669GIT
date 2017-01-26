<?php
/*
* this code sends a manssage to the data base mailbox
*/

// import configurations for server access
require_once __DIR__ . '/db_config.php';

// array form json encoder
$response = array();
$subject = "sent from mobile";
date_default_timezone_set('europe/lisbon');
$date = date('Y/m/d');
$time = date('H:i:s');

// we must check if the gets were sent correctly
if(isset($_GET['message']) && isset($_GET['sender']) && isset($_GET['receiver'])){

	// set variables
	$message = $_GET['message'];
	$sender = $_GET['sender'];
	$receiver = $_GET['receiver'];

	//now we must creat the connection
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME);
	$link->set_charset("utf8");
	if(!$link){                      // connection failed
		$response['success'] = 0;
		$response['message'] = "connection failed";
	}
	else{
		// set query
		$query = "insert into mailbox(message, sender, receiver, status, subject, send_date, send_time) values ('$message', '$sender', '$receiver', '1', '$subject', '$date', '$time')";
		// run the query
		if($link->query($query)){ // success in insertion
			$reponse['success'] = 1;
			$response['message'] = "message sent";
		}else{
			$reponse['success'] = 0;
			$response['message'] = "message not sent";
		}
	}
	mysqli_close($link);
}
else{
	$reponse['success'] = 0;
	$response['message'] = "Not enough data in GET";
}
echo json_encode($response, JSON_FORCE_OBJECT);
?>