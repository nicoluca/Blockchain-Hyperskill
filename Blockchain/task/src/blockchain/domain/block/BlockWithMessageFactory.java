package blockchain.domain.block;

import blockchain.domain.messages.Message;
import blockchain.domain.messages.MessageReceptionService;
import blockchain.utils.MineUtil;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

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
        return new BlockWithMessage(new Block("0", 1, 0, magicNumber, hash, MineUtil.timeSinceInSeconds(startTime)), new LinkedList<>());
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
            Deque<Message> messages = new ConcurrentLinkedDeque<>(MessageReceptionService.getInstance().getMessages());
            //System.out.println("Trying to mine block " + blockId + " with " + messages.size() + " messages");
            String hash = BlockWithMessage.calculateHash(blockId, previousHash, timeStamp, magicNumber, messages);
            if (MineUtil.startsWithValidZeros(hash, Blockchain.getDifficulty())) {
                int blockCreationTime = MineUtil.timeSinceInSeconds(startTime);
                MessageReceptionService.getInstance().clearMessages();
                return new BlockWithMessage(new Block(previousHash, blockId, minerId, magicNumber, hash, blockCreationTime), messages);
            }
        }
        throw new InterruptedException("Miner " + minerId + " was interrupted.");
    }



}
