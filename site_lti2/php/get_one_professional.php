<?php
 
/*
 * Following code gets info of just one professional
 */
 
// include db connect class
require_once __DIR__ . '/db_config.php';

// array for JSON response
$response = array();
$i = 0;

if(isset($_GET['nif'])){
    // connecting to db
    $nif_professional = $_GET['nif'];
    $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");

     
    // get all products from products table
    $query = "SELECT nif, name, mail FROM professional WHERE nif = '$nif_professional'" or die("Error query has failed:".mysqli_error($link));
    $result = $link->query($query); 
     
    // check for empty result
    if ($result->num_rows > 0) {
        // looping through all results
        while($row = $result->fetch_assoc()) {        // temp user array
            $response[$i++] = $row;
        }
        // success
        $response["success"] = 1;

    } 
    else {
        $response["success"] = 0;
        $response["message"] = "No professional found";
    }

    // cleaning process
    mysqli_free_result($result);

    // close connection;
    mysqli_close($link);

}else
    echo json_encode("GET is missing something!!");

// echoing JSON response
echo json_encode($response, JSON_FORCE_OBJECT);

?>