import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;

import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class VerifySignature {
    public static void main(String[] args) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("digital-sign-with-rsa-sha256/Recieved/publicKey.pub"));
            X509EncodedKeySpec ks = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pub = kf.generatePublic(ks);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, pub);
            try (FileInputStream in = new FileInputStream("digital-sign-with-rsa-sha256/Recieved/dig-sign.txt");
            FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt")) {
            processFile(cipher, in, out);

            String inputPath="digital-sign-with-rsa-sha256/Recieved/recieved-input.txt";
            String outputPath="digital-sign-with-rsa-sha256/Verification/hashed-recieved-input.txt";
            GenerateHash.generateHash(inputPath, outputPath);

            int res = matchFiles();
            String verificationResult="Signature verification falied..!";
            if(res==-1){
                verificationResult="Signature verified...!";
                System.out.println(verificationResult);
                
            }

            Files.write(Paths.get("digital-sign-with-rsa-sha256/Verification/verification-result.txt"), verificationResult.getBytes());
            

        }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    static int matchFiles() throws IOException{
        boolean isMatching = false;
        Path digSign = Paths.get("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        Path hashedinput = Paths.get("digital-sign-with-rsa-sha256/Verification/hashed-recieved-input.txt");
        //File inputFile = new File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        //File outputFile = new File("digital-sign-with-rsa-sha256/Verification/decrypted-dig-sign.txt");
        
        try (BufferedReader bf1 = Files.newBufferedReader(digSign);
         BufferedReader bf2 = Files.newBufferedReader(hashedinput)) {
        
        int lineNumber = 1;
        String line1 = "", line2 = "";
        while ((line1 = bf1.readLine()) != null) {
            line2 = bf2.readLine();
            if (line2 == null || !line1.equals(line2)) {
                return lineNumber;
            }
            lineNumber++;
        }
        if (bf2.readLine() == null) {
            return -1;
        }
        else {
            return lineNumber;
        }
    }
    }

    static private void processFile(Cipher ci,InputStream in,OutputStream out)
        throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException
        {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = ci.update(ibuf, 0, len);
                if ( obuf != null ) out.write(obuf);
            }
            byte[] obuf = ci.doFinal();
            if ( obuf != null ) out.write(obuf);
        }
}
