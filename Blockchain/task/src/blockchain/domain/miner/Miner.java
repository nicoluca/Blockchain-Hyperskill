package blockchain.domain.miner;

import blockchain.Config;
import blockchain.domain.CryptoOwner;
import blockchain.domain.block.*;
import java.util.Optional;

public class Miner implements MinerInterface {
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
