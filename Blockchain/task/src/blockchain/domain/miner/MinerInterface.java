package blockchain.domain.miner;

import blockchain.domain.block.BlockInterface;

import java.util.concurrent.Callable;

public interface MinerInterface extends Callable<BlockInterface> {
    public BlockInterface call();
    public int getMinerId();

}
