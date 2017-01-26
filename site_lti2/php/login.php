<?php

/*
 * Following code is a draft to check a password
 */

require_once 'db_connect.php';

$val = array();
if(isset($_POST['email']) && $_POST['password']){
    // connecting to db
    $email = $_POST['email'];
    $pass = $_POST['password'];
    $link = getConnection();


    $query = "SELECT password FROM login WHERE email = '$email'"
    or die("Error query has failed:".mysqli_error($link));
    $result = $link->query($query);

    // check for empty result
    if ($result->num_rows > 0) {
      $val = $result->fetch_assoc();
      if($val['password']  === $pass){
        //TODO : Call a php script thar constructs a profile web page
        header("Location: ../profile.html");
      }else{
        echo "password is INCORRECT!!";
      }
    }else
      echo "null returned";

    // cleaning process
    mysqli_free_result($result);

    // close connection;
    mysqli_close($link);

}else{
  if(isset($_POST['email']))
    echo "POST: got email"; //$_GET['email'];
  if(isset($_POST['password']))
    echo "POST: got password"; //$_GET['password'];
  if(!isset($_POST['email']) && !isset($_POST['password']))
    echo "POST: got nothing!!";
}

?>
