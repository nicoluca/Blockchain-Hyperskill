package blockchain.domain.miner;

import blockchain.Config;
import blockchain.domain.CryptoOwner;
import blockchain.domain.block.*;
import java.util.Optional;
import java.util.concurrent.Callable;

/*
Miner is a callable thread that tries to mine a block, using the block factory. Belongs to one owner.
 */

public class Miner implements MinerInterface, Callable<BlockInterface> {
    private final Blockchain blockchain;
    private final int minerId;
    private final CryptoOwner owner;

    public Miner(Blockchain blockchain, int minerId, CryptoOwner owner) {
        this.blockchain = blockchain;
        this.minerId = minerId;
        this.owner = owner;
    }

    @Override
    public BlockInterface call() {
        Optional<BlockInterface> nextBlock = Config.BLOCK_FACTORY.tryToMineBlock(blockchain, this);

        if (nextBlock.isPresent())
            return nextBlock.get();
        else
            throw new RuntimeException("Miner " + this.minerId + " was interrupted.");
    }

    @Override
    public int getId() {
        return this.minerId;
    }

    @Override
    public CryptoOwner getOwner() {
        return this.owner;
    }
}
