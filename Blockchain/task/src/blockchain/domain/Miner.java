package blockchain.domain;

import java.util.Optional;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {
    private final Blockchain blockchain;
    private final int minerId;

    public Miner(Blockchain blockchain, int minerId) {
        this.blockchain = blockchain;
        this.minerId = minerId;
    }

    @Override
    public Block call() {
        Optional<Block> nextBlock = BlockWithMessageFactory.getInstance().tryToMineBlock(blockchain, minerId);

        if (nextBlock.isPresent())
            return nextBlock.get();
        else
            throw new RuntimeException("Miner " + this.minerId + " was interrupted.");
    }
}
