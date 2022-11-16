import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class GenerateKeyPair {
    public static void main(String[] args) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        //PublicKey pub = kp.getPublic();
        //PrivateKey priv = kp.getPrivate();
        try (FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Keys/privateKey" + ".key")) {
            out.write(kp.getPrivate().getEncoded());
        }
        
        try (FileOutputStream out = new FileOutputStream("digital-sign-with-rsa-sha256/Keys/publicKey" + ".pub")) {
            out.write(kp.getPublic().getEncoded());
        }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
        
    }
}
