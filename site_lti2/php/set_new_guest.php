<?php

/*
* this code creates two new entries, one in the guest table and the other in the user table
*/

// include db connect class
require_once __DIR__ . '/db_config.php';

$result = array();

if(isset($_GET['academic_qual']) && isset($_GET['age']) && isset($_GET['email']) && isset($_GET['gender']) 
    && isset($_GET['username'])){
	// set vars
	$acad = $_GET['academic_qual'];
	$age = $_GET['age'];
	$email = $_GET['email'];
	$gender = $_GET['gender'];
	$user = $_GET['username'];
	$pw = md5('12345');
    $user_level = '6';

	// create connection
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die("Error with connection:" .mysqli_error());
    $link->set_charset("utf8");
    

    // define queries
    $query = "INSERT INTO guest (academic_qual, date_birth, email, gender) 
                VALUES ('$acad', '$age', '$email', '$gender');";
    $query .= "INSERT INTO user (guest_id, pass, username, user_level) VALUES ((select id from guest where email = '$email'), '$pw', '$user', '$user_level')";


    if($link->multi_query($query) === TRUE){
    	$result['success'] = 1;
    	$result['message'] = "all went good :)";
    }
    else{
    	$result['success'] = 0;
    	$result['message'] = "one of the queries didn't insert the data";
    }

    // close connection;
    mysqli_close($link);

}else{
	$result['success'] = 0;
	$result['message'] = "not enough data in GET";
}

    echo json_encode($result, JSON_FORCE_OBJECT);
function generatePassword($length = 5) {
    $chars = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    $count = mb_strlen($chars);

    for ($i = 0, $result = ''; $i < $length; $i++) {
        $index = rand(0, $count - 1);
        $result .= mb_substr($chars, $index, 1);
    }

    return $result;
}

?>