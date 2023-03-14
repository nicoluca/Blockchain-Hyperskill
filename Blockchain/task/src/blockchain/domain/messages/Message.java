package blockchain.domain.messages;

import blockchain.Config;

import java.io.*;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;

/*
Adapted from: https://mkyong.com/java/java-digital-signatures-example/
 */

public class Message  implements Verifiable {
    private final String messageText;
    private final long messageId;
    private final byte[] signature;
    private final PublicKey publicKey;

    public Message (String messageText, long messageId, PrivateKey privateKey, PublicKey publicKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        this.messageText = messageText;
        this.messageId = messageId;
        this.signature = sign(messageText + messageId, privateKey);
        this.publicKey = publicKey;
    }


    // Sign with private key
    private byte[] sign(String data, PrivateKey privateKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        Signature rsa = Signature.getInstance(Config.SIGNATURE_ALGORITHM);
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        return rsa.sign();
    }

    public static PrivateKey getPrivateKeyFromFile(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(Config.KEY_FACTORY_ALGORITHM);
        return kf.generatePrivate(spec);
    }

    public String getMessageText() {
        return this.messageText;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public long getMessageId() {
        return this.messageId;
    }

    @Override
    public boolean verify() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(Config.SIGNATURE_ALGORITHM);
        sig.initVerify(this.publicKey);
        byte[] messageBytes = (this.messageText + this.messageId).getBytes();
        sig.update(messageBytes);
        return sig.verify(this.signature);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String toString() {
        return "Message: " + this.messageText + " ID: " + this.messageId;
    }
}
