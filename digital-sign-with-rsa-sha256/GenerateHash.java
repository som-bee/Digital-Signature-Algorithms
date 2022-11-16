import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class GenerateHash {
    public static void generateHash(String inputFilePath, String OutputFilePath) throws IOException, NoSuchAlgorithmException{
        File file = new File(inputFilePath);
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        BufferedReader br
            = new BufferedReader(new FileReader(file));
 
        // Declaring a string variable
        String input="";
        String line;
        // Condition holds true till
        // there is character in a string
        while ((line = br.readLine()) != null){
            input += line;
        }
            // Print the string
            //System.out.println(input); // printing the original input
            String hash=toHexString(getSHA(input));
            //System.out.println(getSHA(input).length); //size of hash
            //System.out.println(hash ); // printing the hash
            // System.out.println(toHexString(getSHA("hi there")) );
            Files.write(Paths.get(OutputFilePath), hash.getBytes());
    }
    public static void main(String[] args) {
        try {
            generateHash("digital-sign-with-rsa-sha256/Input/input.txt", "digital-sign-with-rsa-sha256/Input/hashed-input.txt");
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        
    }
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
     
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);
 
        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));
 
        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
 
        return hexString.toString();
    }
}
