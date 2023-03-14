package blockchain.domain.miner;

import blockchain.domain.block.BlockInterface;
import blockchain.domain.CryptoOwner;

import java.util.concurrent.Callable;

public interface MinerInterface extends Callable<BlockInterface> {
    BlockInterface call();
    int getId();
    CryptoOwner getOwner();
}
