<?php
 
/*
 * Following code returns the patient's results from an specific exercise
 */
 
// include db connect class
require_once __DIR__ . '/db_config.php';

// array for JSON response
$response = array();
$i = 0;


if(isset($_GET['id_patient']) && isset($_GET['cod_exercicio'])){
    // connecting to db
    $nif_patient = $_GET['id_patient'];
    $cod = $_GET['cod_exercicio'];
    $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");

     
    
    $query = "SELECT * FROM exercise_m_patient WHERE id_patient = '$nif_patient' and cod_exercicio = '$cod'" or die("Error query has failed:".mysqli_error($link));
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
        $response["message"] = "No patients found";
     
        // echo no users JSON
        echo json_encode($response, JSON_FORCE_OBJECT);
    }

    // close connection;
    mysqli_close($link);

    // cleaning process
    mysqli_free_result($result); 

}else
    echo json_encode("GET is missing something!!", JSON_FORCE_OBJECT);
   

?>