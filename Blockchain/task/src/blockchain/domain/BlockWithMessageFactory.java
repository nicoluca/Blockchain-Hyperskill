package blockchain.domain;

import blockchain.domain.messages.MessageReceptionService;
import blockchain.utils.MineUtil;

import java.util.Date;
import java.util.Optional;

public class BlockWithMessageFactory implements BlockFactoryInterface {
    private static BlockWithMessageFactory instance;

    public static BlockWithMessageFactory getInstance() {
        if (instance == null)
            instance = new BlockWithMessageFactory();
        return instance;
    }

    @Override
    public Optional<Block> tryToMineBlock(Blockchain blockchain, int minerId) {
        try {
            return Optional.of(mineBlock(blockchain, minerId));
        } catch (InterruptedException e) {
            return Optional.empty();
        }

    }

    private BlockWithMessage mineBlock(Blockchain blockchain, int minerId) throws InterruptedException {
        if (blockchain.getChainSize() == 0)
            return mineFirstBlock();
        else
            return mineSubsequentBlock(blockchain, minerId);
    }

    private BlockWithMessage mineFirstBlock() {
        long startTime = new Date().getTime();
        long magicNumber = MineUtil.getRandomMagicLong();
        long timeStamp = new Date().getTime();
        String hash = Block.calculateHash(1, "0", timeStamp, magicNumber);
        return new BlockWithMessage(new Block("0", 1, 0, magicNumber, hash, MineUtil.timeSinceInSeconds(startTime)), "");
    }

    private BlockWithMessage mineSubsequentBlock(Blockchain blockchain, int minerId) throws InterruptedException {
        long startTime = new Date().getTime();
        Block previousBlock = blockchain.getLastBlock();
        int blockId = previousBlock.getBlockId() + 1;
        String previousHash = previousBlock.getHash();
        return startMining(blockId, previousHash, minerId, startTime);
    }

    private BlockWithMessage startMining(int blockId, String previousHash, int minerId, long startTime) throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            long magicNumber = MineUtil.getRandomMagicLong();
            long timeStamp = new Date().getTime();
            String message = MessageReceptionService.getInstance().readMessages();
            String hash = BlockWithMessage.calculateHash(blockId, previousHash, timeStamp, magicNumber, message);
            if (MineUtil.startsWithValidZeros(hash, Blockchain.getDifficulty())) {
                int blockCreationTime = MineUtil.timeSinceInSeconds(startTime);
                MessageReceptionService.getInstance().clearMessages();
                return new BlockWithMessage(new Block(previousHash, blockId, minerId, magicNumber, hash, blockCreationTime), message);
            }
        }
        throw new InterruptedException("Miner " + minerId + " was interrupted.");
    }



}
