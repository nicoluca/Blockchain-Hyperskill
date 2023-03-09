package blockchain.domain;

import blockchain.domain.messages.MessageReceptionService;
import blockchain.utils.MineUtil;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;

public class Blockchain {
    private static Blockchain instance;
    private final Deque<Block> chain = new ConcurrentLinkedDeque<>();
    static int difficulty = 0;
    private final ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    public static Blockchain getInstance() {
        if (Blockchain.instance == null)
            Blockchain.instance = new Blockchain();
        return Blockchain.instance;
    }

    public static int getDifficulty() {
        return Blockchain.difficulty;
    }

    public synchronized void addAndPrintBlock(Block block) {
        synchronized (this.lock.writeLock()) {
            addBlock(block);
            System.out.println(block);
            setDifficulty();
        }
    }

    private void addBlock(Block block) {
        if (isValidNextBlock(block)) {
            this.chain.add(block);
        } else
            throw new IllegalArgumentException("Block is not valid.");
    }

    private boolean isValidNextBlock(Block block) {
        if (block == null)
            return false;
        else if (this.getChainSize() == 0)
            return block.getPreviousHash().equals("0");
        else
            return (this.getLastBlock().getHash().equals(block.getPreviousHash()) &&
                    MineUtil.startsWithValidZeros(block.getHash(), Blockchain.difficulty));
    }

    public Block getLastBlock() {
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
        for (Block block : this.chain)
            sb.append(block.toString()).append("\n");
        return sb.toString();
    }
}
