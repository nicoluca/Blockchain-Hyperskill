package blockchain.domain.messages;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface Verifiable {
    boolean verify() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}
