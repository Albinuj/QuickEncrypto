<?php

include 'AES.php';
class DB_Functions {

    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $shared_key= (bin2hex(random_bytes(25)));
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, encrypted_password, salt, shared_key, created_at) VALUES(?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssssss", $uuid, $name, $email, $encrypted_password, $salt, $shared_key);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
public function isTokenExisted($uid, $email, $filename) {
        $stmt = $this->conn->prepare("SELECT * FROM tokens WHERE unique_id = ? AND email = ? AND fn = ?");
        $stmt->bind_param("sss", $uid, $email, $filename);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // token existed 
           $stmt->close();
  $stmnt = $this->conn->prepare("SELECT * FROM tokens WHERE unique_id = ? AND email = ? AND fn = ?");
            $stmnt->bind_param("sss", $uid, $email, $filename);
            $stmnt->execute();
            $token = $stmnt->get_result()->fetch_assoc();


             $s = $token["seed"];
             $sn = $token["sequence"];

            $cs=1000; 
            $csprn = null;
            $n = ceil($cs/192);
while($n > 0){

  $imputText = $sn;
  $imputKey = $s;
  $blockSize = 128;
  $aes = new AES($imputText, $imputKey, $blockSize);
  $enc = $aes->encrypt();
  $csprn = $csprn . $enc;
  $sn=$sn+1;
  $n--;
}
            
            $stmnt->close();
            return $csprn;
        } else {
            // token not existed
            $stmt->close();
//$csprn = "defdefdfdsfdsf43dfsdvdfvv";
// $csprn = genToken($uid, $email, $filename);
$s=random_int (10,999999999) ; //key
$sn=random_int (10,99999999) ; // Sequence no



        $stmt = $this->conn->prepare("INSERT INTO tokens(unique_id, email, fn, seed, sequence, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uid, $email, $filename, $s, $sn);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
        $stmt = $this->conn->prepare("SELECT * FROM tokens WHERE unique_id = ? AND email = ? AND fn = ?");
        $stmt->bind_param("sss", $uid, $email, $filename);
            $stmt->execute();
            $token = $stmt->get_result()->fetch_assoc();

             $s = $token["seed"];
             $sn = $token["sequence"];

            $cs=1000; 
            $csprn = null;
            $n = ceil($cs/192);
while($n > 0){

  $imputText = $sn;
  $imputKey = $s;
  $blockSize = 128;
  $aes = new AES($imputText, $imputKey, $blockSize);
  $enc = $aes->encrypt();
  $csprn = $csprn . $enc;
  $sn=$sn+1;
  $n--;
}
            $stmt->close();

          //  return $token;
            return $csprn;
        } else {
           
            return false;
        }
    //return false;
       }
    }


public function genToken($uid, $email, $filename) {


      
$s=random_int (10,999999999) ; //key
$sn=random_int (10,99999999) ; // Sequence no



        $stmt = $this->conn->prepare("INSERT INTO tokens(unique_id, email, fn, seed, sequence, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $uid, $email, $filename, $s, $sn);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
        $stmt = $this->conn->prepare("SELECT * FROM tokens WHERE unique_id = ? AND email = ? AND fn = ?");
        $stmt->bind_param("sss", $uid, $email, $filename);
            $stmt->execute();
            $token = $stmt->get_result()->fetch_assoc();

             $s = $token["seed"];
             $sn = $token["sequence"];

            $cs=1000; 
            $csprn = null;
            $n = ceil($cs/192);
while($n > 0){

  $imputText = $sn;
  $imputKey = $s;
  $blockSize = 128;
  $aes = new AES($imputText, $imputKey, $blockSize);
  $enc = $aes->encrypt();
  $csprn = $csprn . $enc;
  $sn=$sn+1;
  $n--;
}
            $stmt->close();

          //  return $token;
            return $csprn;
        } else {
           
            return false;
        }

   }


 


    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
