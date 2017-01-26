<?php
 
/*
 * Following code returns the not done sessions of a given patient
 */
 
// include db connect class
require_once __DIR__ . '/db_config.php';

// array for JSON response
$response = array();
$i = 0;

if(isset($_GET['id_patient'])){
    // connecting to db
    $id = $_GET['id_patient'];
    $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");

     
    // get all products from products table
    $query = "SELECT * FROM mobile_session WHERE id_patient = '$id' and done = '0'" or die("Error query has failed:".mysqli_error($link));
    $result = $link->query($query); 
     
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
        $response["message"] = "No sessions for this patient";
     
        // echo no users JSON
        echo json_encode($response, JSON_FORCE_OBJECT);
    }

    // close connection;
    mysqli_close($link);

    // cleaning process
    mysqli_free_result($result); 

}else
    echo json_encode("GET: is missing something!!", JSON_UNESCAPED_UNICODE);

   
?>