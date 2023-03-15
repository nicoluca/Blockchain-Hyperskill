package blockchain.domain.block;

import blockchain.domain.miner.MinerInterface;
import blockchain.domain.transactions.Verifiable;

public interface BlockInterface extends Verifiable {
    String getPreviousHash();
    int getId();
    MinerInterface getMiner();
    long getMagicNumber();
    String getHash();
    int getBlockCreationTime();
    boolean isValid();
}
