package blockchain.domain.messages;

public interface Signer {
    byte[] createSignature(byte[] data);
}
