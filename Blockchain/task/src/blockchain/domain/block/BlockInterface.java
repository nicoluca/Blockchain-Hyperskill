package blockchain.domain.block;

import blockchain.domain.messages.Verifiable;

public interface BlockInterface extends Verifiable {
    String getPreviousHash();
    int getBlockId();
    long getMinerId();
    long getMagicNumber();
    String getHash();
    int getBlockCreationTime();
    boolean isValid();
}
