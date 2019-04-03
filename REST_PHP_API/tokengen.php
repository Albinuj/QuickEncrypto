<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['uid']) && isset($_POST['email']) && isset($_POST['filename'])) {

    // receiving the post params
    $uid = $_POST['uid'];
    $email = $_POST['email'];
    $filename = $_POST['filename'];

    // check if token already existed with the same email and fn
    $token = $db->isTokenExisted($uid, $email, $filename);
  //  $token = $db->genToken($uid, $email, $filename);
    if ($token) {
     
            $response["error"] = FALSE;
            $response["csprn"] = $token;
            echo json_encode($response);

    } else {
       
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in token generation!";
            echo json_encode($response);
        }
    }
 else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (uid, email or filename) is missing!";
    echo json_encode($response);
}
?>

