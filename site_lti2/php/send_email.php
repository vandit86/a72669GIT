
<?php
/**
* This code sends an email via Google's Gmail servers.
*/
//SMTP needs accurate times, and the PHP time zone MUST be set

date_default_timezone_set('europe/lisbon');

$message = "Hello, I can send emails now !!!";  // this is suposes to be an html message
require_once __DIR__ . '/PHPMailerAutoload.php';

if(isset($_GET['email']) && isset($_GET['username'])){
$email = $_GET['email'];
$user = $_GET['username'];

$mail = new PHPMailer;
$mail->isSMTP();
// 2 = client and server messages
$mail->SMTPDebug = 2;
//Ask for HTML-friendly debug output
$mail->Debugoutput = 'html';
//Set the hostname of the mail server
$mail->Host = 'smtp.gmail.com';
//Set the SMTP port number - 587 for authenticated TLS
$mail->Port = 587;
//Set the encryption 
$mail->SMTPSecure = 'tls';
//Whether to use SMTP authentication
$mail->SMTPAuth = true;
//Username to use for SMTP authentication - use full email address for gmail
$mail->Username = "g2.android.development@gmail.com";
$mail->Password = "androidg2";
$mail->setFrom('g2.android.development@gmail.com', 'Nepum Mobile Password Service');
//Set who the message is to be sent to
$mail->addAddress($email,$user);
$mail->Subject = 'Register Validation';
$mail->msgHTML($message);
//Attach an image file
//$mail->addAttachment('images/phpmailer_mini.png');
//send the message, check for errors
if (!$mail->send()) {
echo "Mailer Error: " . $mail->ErrorInfo;
} else {
echo "Message sent!";
}
}
else echo "No email or user";
?>