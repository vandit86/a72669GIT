
<?php
/*
* this scrip gets the last result entered by one guest
*/
require_once '/db_conf.php';
echo "bla";

//$response = array();
//$i = 0;
if(isset($_GET['id_guest'])){
	$id = $_GET['id_guest'];
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die ("Error with the connection: ".mysqli_error());
	$link->set_charset("utf8");
	echo"OK";
	$query = "select max(index_Guest), id_guest, cod_exercicio, time_exe, time_est, num_perf_plays,
	 num_total_plays, num_correct_catch, num_wrong_catch, num_imag, num_sound, num_both, num_correct_img,
	 num_correct_sound, num_correct_both, data_real, hora, level, pontuation from nepum_db.exercise_m_guest where id_guest='$id'";
	$result = $link->query($query);
	$row = $result->fetch_assoc();
	var_dump($row);
	mysqli_close($link);
}

//echo json_encode($response, JSON_FORCE_OBJECT);*/
?>
