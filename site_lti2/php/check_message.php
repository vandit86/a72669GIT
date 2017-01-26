<?php
/*
* this code sends a manssage to the data base mailbox
*/

// import configurations for server access
require_once __DIR__ . '/db_config.php';

// array form json encoder
$response = array();
$i = 0;
$flag = 0;

// we must check if the gets were sent correctly
if(isset($_GET['receiver'])){

	// set variables
	$receiver = $_GET['receiver'];

	//now we must creat the connection
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME);
	$link->set_charset("utf8");


	if(!$link){                      // connection failed
		$response['success'] = 0;
		$response['message'] = "connection failed";
	}else{
		// set query
		$query = "SELECT message, sender, receiver, subject, send_date, send_time from mailbox where receiver = '$receiver' and status = '1'";
		// run the query
		$result = $link->query($query);
		if ($result->num_rows > 0) {
	    	// looping through all results
	    	while($row = $result->fetch_assoc()) {        // temp user array
	        	$response[$i++] = $row;
	    	}
			$reponse['success'] = 1;
			$flag = 1;
		}
		else{
			$reponse['success'] = 0;
			$response['message'] = "Zero messages found!";
		}
		mysqli_close($link);
		if($flag == 1){
			$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME);
			$link->set_charset("utf8");
			$query = "UPDATE mailbox set status = '0' where receiver = '$receiver' and status = '1'";
			if($link->query($query))
				$response['clean'] = 1;
			else
				$response['clean'] = 0;
			mysqli_close($link);
		}
	}
}else{
	$reponse['success'] = 0;
	$response['message'] = "Not enough data in GET";
}
echo json_encode($response,JSON_FORCE_OBJECT);
?>