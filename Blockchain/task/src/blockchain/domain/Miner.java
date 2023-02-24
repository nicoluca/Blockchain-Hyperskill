package blockchain.domain;

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
        return Block.createBlock(blockchain, minerId);
    }
}
