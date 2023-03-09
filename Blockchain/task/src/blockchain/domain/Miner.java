package blockchain.domain;

import blockchain.domain.block.Block;
import blockchain.domain.block.BlockWithMessage;
import blockchain.domain.block.BlockWithMessageFactory;
import blockchain.domain.block.Blockchain;

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
    public BlockWithMessage call() {
        Optional<Block> nextBlock = BlockWithMessageFactory.getInstance().tryToMineBlock(blockchain, minerId);

        if (nextBlock.isPresent() && nextBlock.get() instanceof BlockWithMessage)
            return (BlockWithMessage) nextBlock.get();
        else
            throw new RuntimeException("Miner " + this.minerId + " was interrupted.");
    }
}
