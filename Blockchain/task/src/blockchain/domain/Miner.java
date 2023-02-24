package blockchain.domain;

import java.util.concurrent.Callable;

public class Miner implements Callable<Boolean> {
    private Blockchain blockchain = Blockchain.getInstance();
    private int minerid;

    public Miner(int minerid) {
        this.minerid = minerid;
    }

    @Override
    public Boolean call() throws Exception {
        Block block = Block.createBlock(blockchain, minerid);
        blockchain.addBlock(block);
        return null;
    }
}
