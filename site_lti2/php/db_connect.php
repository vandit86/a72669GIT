<?php

// include db connect class
require_once 'db_config.php';

function getConnection()
{
  $link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or
  die("Error with connection:" .mysqli_error());
  $link->set_charset("utf8");
  return $link;
}

?>
