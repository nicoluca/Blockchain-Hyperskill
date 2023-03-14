package blockchain.utils;

import blockchain.Config;

import java.security.*;

public class CryptographyUtil {

    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Config.KEY_FACTORY_ALGORITHM);
            keyGen.initialize(Config.KEY_LENGTH);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateSignature(byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(Config.SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifySignature(byte[] dataToVerify, byte[] sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(Config.SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(dataToVerify);
            return signature.verify(sign);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

}
