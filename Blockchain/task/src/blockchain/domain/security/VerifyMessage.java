package blockchain.domain.security;

import blockchain.Config;
import blockchain.domain.messages.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/*
Adapted from: https://mkyong.com/java/java-digital-signatures-example/
 */

public class VerifyMessage {
    public static boolean verifyMessage(Message message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance(Config.SIGNATURE_ALGORITHM);
        sig.initVerify(message.getPublicKey());
        byte[] messageBytes = (message.getMessageText() + message.getMessageId()).getBytes();
        sig.update(messageBytes);
        return sig.verify(message.getSignature());
    }
}