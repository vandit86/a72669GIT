<?php

/*
*this lines of code set a mobile session as done
*/

require_once __DIR__ . '/db_config.php';

// vars for the response json
$response = array();

if(isset($_GET['id_patient']) && isset($_GET['index_m_session'])){

	$id = $_GET['id_patient'];
	$session = $_GET['index_m_session'];
	$set = 1;

	// create a connection
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die ("Error with the connection: ".mysqli_error());
	$link->set_charset("utf8");

	// define query
	$query = "UPDATE mobile_session set done = '$set' where id_patient = '$id' and index_m_session = '$session'";
	if($link->query($query)){
		$response['success'] = 1;
		$response['message'] = "Session was maked has done";
	}
	else{
		$response['success'] = 0;
		$response['message'] = "Query failed";
	}
	mysqli_close($link);
}
else{
	$response['success'] = 0;
	$response['message'] = "id wasn't received";
}
echo json_encode($response, JSON_FORCE_OBJECT);
?>