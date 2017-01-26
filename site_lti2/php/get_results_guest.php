<?php
 
/*
 * Following returns all the result entries made by a given guest
 */
 
// include db config class
require_once __DIR__ . '/db_config.php';



// here we need to check the data in Post
if(isset($_GET['id_guest'])){

	$guest_id = $_GET['id_guest'];

	// array for JSON response
	$response = array();
	$i = 0;

	// first we must creat a link the variables for this link are provided by db_config.php
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die ("Error with the connection: ".mysqli_error());
	$link->set_charset("utf8");

	// now lets creat a query
	// this is the instruction that will run on the database
	$query = "SELECT * FROM exercise_m_guest WHERE id_guest = '$guest_id'" or die("Error running the query: ".mysqli_error($link));
	$result = $link->query($query);   // run the query

	// check for empty result
	if ($result->num_rows > 0) {
	    // looping through all results
	    while($row = $result->fetch_assoc()) {        // temp user array
	        $response[$i++] = $row;
	    }

	    // success
	    $response["success"] = 1;
	 
	    // echoing JSON response
	    echo json_encode($response, JSON_FORCE_OBJECT);

	} else {
		$response["success"] = 0;
		$response["message"] = "No entries whith that id";
		echo json_encode($response, JSON_FORCE_OBJECT);
	}

	// cleaning process
	mysqli_free_result($result);

	// close connection;
	mysqli_close($link);

}else{
	$response["success"] = 0;
	$response["message"] = "No guest id, required field missing";
	 
	echo json_encode($response, JSON_FORCE_OBJECT);
}

?>