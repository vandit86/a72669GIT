<?php
 
/*
 * the following code will list all patients of a given professional
 */
 
// include db connect class
require_once __DIR__ . '/db_config.php';

// array for JSON response
$response = array();
$i = 0;


if(isset($_GET['professional_id'])){
    // connecting to db
    $id = $_GET['professional_id'];
    $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");             // set de enconding to UTF8, so it can be compatible with json_encode()
     
    // get all products from products table
    $query = "SELECT professional_nif, email, name, date_of_birth, nif, gender FROM patient WHERE professional_nif = '$id' order by name" or die("Error query has failed:".mysqli_error($link));
    $result = $link->query($query); 
     
    // check for empty result
    if ($result->num_rows > 0) {
        // looping through all results
        while($row = $result->fetch_assoc()) {        // making an associative array
            $response[$i++] = $row;
        }
        // success
        $response['success'] = 1;                        
    } 
    else {
        $response['success'] = 0;
        $response['message'] = "No patients found";
    }

    // close connection;
    mysqli_close($link);

    // cleaning process
    mysqli_free_result($result);
}
else{
    $response['success'] = 0;
    $response['message'] = "GET has no professional id!!";
}
// send json
echo json_encode($response, JSON_FORCE_OBJECT);    

?>