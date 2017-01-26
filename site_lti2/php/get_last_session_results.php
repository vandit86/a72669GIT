<?php
 
/*
 * Following code returns the patient's results from an specific exercise
 */
 
// include db connect class
require_once __DIR__ . '/db_config.php';

// array for JSON response
$response = array();
$i = 0;


if(isset($_GET['id_patient'])){
    // connecting to db
    $nif_patient = $_GET['id_patient'];

    $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");

    
    $query = "SELECT MAX(index_m_session) FROM mobile_session WHERE id_patient = '$nif_patient' and done = '1'" or die("Error query has failed:".mysqli_error($link));
    $result = $link->query($query); 
     
    // check for empty result
    if ($result->num_rows > 0) {
        $row = $result->fetch_assoc();       
        $mindex = $row['index_m_session'];
        mysqli_free_result($result);
        mysqli_close($link); 
        $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
        $link->set_charset("utf8");
        $query = "SELECT * FROM exercise_m_patient WHERE session = $mindex";
        $result = $link->query($query);
        if($result->num_rows > 0){
            while($row = $result->fetch_assoc())
                $response[$i++] = $row;

            // success
            $response["success"] = 1;
         
            // echoing JSON response
            echo json_encode($response,JSON_FORCE_OBJECT);

        }
    }else{   
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