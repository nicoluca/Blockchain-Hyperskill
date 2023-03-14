package blockchain.domain.block;

import blockchain.domain.miner.Miner;
import blockchain.domain.miner.MinerInterface;
import blockchain.domain.transactions.Verifiable;

public interface BlockInterface extends Verifiable {
    String getPreviousHash();
    int getBlockId();
    MinerInterface getMiner();
    long getMagicNumber();
    String getHash();
    int getBlockCreationTime();
    boolean isValid();
}
