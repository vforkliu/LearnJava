import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class RSAExample {

    public static PublicKey loadPublicKeyFromString(String pem) throws Exception {
        // 去掉 PEM 文件头和尾部的标记
        String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                                .replace("-----END PUBLIC KEY-----", "")
                                .replaceAll("\n", "")
                                .replaceAll("\r", "");

        // Base64 解码
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        // 使用 X509EncodedKeySpec 和 KeyFactory 生成公钥对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  // 你可以根据需要选择 "RSA" 或其他算法
        return keyFactory.generatePublic(keySpec);
    }

    // 生成 RSA 密钥对（公钥和私钥）
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);  // 可以选择密钥长度，2048位是推荐的
        return keyPairGenerator.generateKeyPair();
    }

    // 公钥加密
    public static String encrypt(String plainText, Key publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 私钥解密
    public static String decrypt(String encryptedText, Key privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            // 生成 RSA 密钥对
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 加密
            String originalText = "Hello, RSA Encryption!";
            System.out.println("Original Text: " + originalText);
            String encryptedText = encrypt(originalText, publicKey);
            System.out.println("Encrypted Text: " + encryptedText);

            // 解密
            String decryptedText = decrypt(encryptedText, privateKey);
            System.out.println("Decrypted Text: " + decryptedText);

            String pemPublicKey = "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5ykPZzQwwL58jID2Y1Wir+LiSR7lOm3LC6CSWLmkfGoaqjZ9eLysOQHeWIM6XGipzYednT3278MXV4xuyvwI5FGctlNqdhbKAsvZC3Vz35P2F6ju0WeufsluJ/03+wlAcyCcwp83WB455ueENBxgHhIZOUPcW0FCRUYHSX83+bwIDAQAB\n-----END PUBLIC KEY-----";

            PublicKey publicKey2 = loadPublicKeyFromString(pemPublicKey);
            String encData = "qR52GmsZ/+6amXmJJR417N0XQuB7tArgEx5vsS8okv2G3Z5wxunkg2lwP9FJYakmbiCgG48yjLEZJZ+o58Z10J+86BlPwQXG8jBHw6hMLhd80XXMngJVfN6703BMou+RB917CAsOPVllzqLG2OWTkb/ZsLtCydPFDo1sbUZt8XE=";
            String decData = decrypt(encData, publicKey2);
            System.out.println(decData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
