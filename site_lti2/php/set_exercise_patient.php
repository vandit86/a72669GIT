<?php

/*
* this inserts into DB the results of an exercise(patient)
*/

// include db connect class
require_once __DIR__ . '/db_config.php';

// here we need to check the data in Post
if(isset($_GET['id_patient']) && isset($_GET['id_professional']) && isset($_GET['cod_exercicio'])
	&& isset($_GET['time_exe']) && isset($_GET['time_est']) && isset($_GET['num_perf_play']) 
	&& isset($_GET['num_total_plays']) && isset($_GET['num_correct_catch']) && isset($_GET['num_wrong_catch'])
	&& isset($_GET['num_img']) && isset($_GET['num_sound']) && isset($_GET['num_both']) 
	&& isset($_GET['num_correct_img']) && isset($_GET['num_correct_sound']) && isset($_GET['num_correct_both'])
	&& isset($_GET['data_real']) && isset($_GET['hora']) && isset($_GET['level']) 
	&& isset($_GET['accuracy']) && isset($_GET['session'])){

	$id_patient = $_GET['id_patient'];
	$id_professional = $GET['id_professional'];
	$cod_exercicio = $_GET['cod_exercicio'];
	$time_exe = $_GET['time_exe'];
	$time_est = $_GET['time_est'];
	$num_perf_play = $_GET['num_perf_play'];
	$num_total_plays = $_GET['num_total_plays'];
	$num_correct_catch = $_GET['num_correct_catch'];
	$num_wrong_catch = $_GET['num_wrong_catch'];
	$num_img = $_GET['num_img'];
	$num_sound = $_GET['num_sound'];
	$num_both = $_GET['num_both'];
	$num_correct_img = $_GET['num_correct_img'];
	$num_correct_sound = $_GET['num_correct_sound'];
	$num_correct_both = $_GET['num_correct_both'];
	$data_real = $_GET['data_real'];
	$hora = $_GET['hora'];
	$level = $_GET['level'];
	$accuracy = $_GET['accuracy'];
	$session = $_GET['session'];

	// array for JSON response
	$response = array();

	// first we must creat a link the variables for this link are provided by db_config.php
	$link = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die ("Error with the connection: ".mysqli_error());
	$link->set_charset("utf8");

	// now lets creat a query
	// this is the instruction that will run on the database

	$query = "INSERT INTO exercise_m_patient(id_patient, id_professional,
		cod_exercicio, time_exe, time_est, num_perf_plays, num_total_plays,
		num_correct_catch, num_wrong_catch, num_imag, num_sound, num_both,
		num_correct_img, num_correct_sound, num_correct_both, data_real, 
		hora, level, accuracy, session) 
		VALUES('$id_patient', '$id_professional',
		'$cod_exercicio', '$time_exe', '$time_est', '$num_perf_play', '$num_total_plays',
		'$num_correct_catch', '$num_wrong_catch', '$num_img', '$num_sound', '$num_both',
		'$num_correct_img', '$num_correct_sound', '$num_correct_both', '$data_real', 
		'$hora', '$level', '$ac	curacy', '$session')" or die("Error running the query: ".mysqli_error($link));   
	if($link->query($query)) // run the query
		$response["success"] = 1;
	else
		$response["success"] = 0;
		
	echo json_encode($response, JSON_FORCE_OBJECT);
	// close connection;
	mysqli_close($link);
}
else
	echo json_encode("GET is missing something!");
?>