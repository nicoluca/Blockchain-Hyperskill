package blockchain.domain.block;

import blockchain.Config;
import blockchain.domain.messages.Message;
import blockchain.utils.MineUtil;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;

public class Blockchain {
    private static Blockchain instance;
    private final Deque<BlockWithMessage> chain = new ConcurrentLinkedDeque<>();
    static int difficulty = Config.INITIAL_DIFFICULTY;
    private final ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();
    private static long messageId = 1;

    public static Blockchain getInstance() {
        if (Blockchain.instance == null)
            Blockchain.instance = new Blockchain();
        return Blockchain.instance;
    }

    public static int getDifficulty() {
        return Blockchain.difficulty;
    }

    public synchronized void addAndPrintBlock(BlockWithMessage block) {
        synchronized (this.lock.writeLock()) {
            addBlock(block);
            System.out.println(block);
            setDifficulty();
        }
    }

    private void addBlock(BlockWithMessage block) {
        if (isValidNextBlock(block)) {
            this.chain.add(block);
        } else
            throw new IllegalArgumentException("Block is not valid.");
    }

    private boolean isValidNextBlock(BlockWithMessage block) {
        if (block == null)
            return false;
        else if (this.getChainSize() == 0)
            return block.getPreviousHash().equals("0");
        else
            return (this.getLastBlock().getHash().equals(block.getPreviousHash()) &&
                    MineUtil.startsWithValidZeros(block.getHash(), Blockchain.difficulty));
    }

    public BlockWithMessage getLastBlock() {
        if (this.getChainSize() == 0)
            throw new IllegalStateException("Blockchain is empty.");
        else
            return this.chain.getLast();
    }

    private void setDifficulty() { // TODO Requirements unclear, guessing something reasonable...
        if (this.getLastBlock().getBlockCreationTime() < 10) {
            Blockchain.difficulty++;
            System.out.println("N was increased to " + Blockchain.difficulty);
        } else if (this.getLastBlock().getBlockCreationTime() < 60) {
            System.out.println("N stays the same");
        } else {
            Blockchain.difficulty--;
            System.out.println("N was decreased by 1");
        }
        System.out.println();
    }

    public int getChainSize() {
        return this.chain.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BlockWithMessage block : this.chain)
            sb.append(block.toString()).append("\n");
        return sb.toString();
    }

    public long getNextMessageId() {
        synchronized (this.lock.writeLock()) {
            return messageId++;
        }
    }

    public boolean isValid() {
        synchronized (this.lock.readLock()) {
            if (this.getChainSize() == 0)
                return true;
            else {
               return checkBlocks();
            }
        }
    }

    private boolean checkBlocks() {
        BlockWithMessage previousBlock = this.chain.getFirst();
        long currentMessageId = 0;
        for (BlockWithMessage block : this.chain) {
            if (!block.getPreviousHash().equals(previousBlock.getHash()) && block.getPreviousHash() != "0") {
                System.err.println("Previous hash does not match for block with id " + block.getBlockId());
                return false;
            }

            for (Message message : block.getMessages()) {
                if (message.getMessageId() < currentMessageId) {
                    System.err.println("Message id is not in valid order: " + message);
                    return false;
                }
                else
                    currentMessageId = message.getMessageId();
            }

            previousBlock = block;
        }
        return true;
    }
}
