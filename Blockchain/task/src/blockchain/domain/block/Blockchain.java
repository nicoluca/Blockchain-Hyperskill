package blockchain.domain.block;

import blockchain.Config;
import blockchain.utils.MineUtil;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;

public class Blockchain {
    private static Blockchain instance;
    private final Deque<BlockInterface> chain = new ConcurrentLinkedDeque<>();
    static int difficulty = Config.INITIAL_DIFFICULTY;
    private final ReadWriteLock lock = new java.util.concurrent.locks.ReentrantReadWriteLock();

    public synchronized static Blockchain getInstance() {
        if (Blockchain.instance == null)
            Blockchain.instance = new Blockchain();
        return Blockchain.instance;
    }

    public static synchronized int getDifficulty() {
        return Blockchain.difficulty;
    }

    public void addAndPrintBlock(BlockInterface block) {
        synchronized (this.lock.writeLock()) {
            addBlock(block);
            System.out.println(block);
            setDifficulty();
        }
    }

    private void addBlock(BlockInterface block) {
        if (isValidNextBlock(block)) {
            this.chain.add(block);
        } else
            throw new IllegalArgumentException("Block is not valid.");
    }

    private boolean isValidNextBlock(BlockInterface block) {
        if (block == null)
            return false;
        else if (this.getChainSize() == 0)
            return block.getPreviousHash().equals("0");
        else
            return (this.getLastBlock().getHash().equals(block.getPreviousHash()) &&
                    MineUtil.startsWithValidZeros(block.getHash(), Blockchain.difficulty));
    }

    public BlockInterface getLastBlock() {
        if (this.getChainSize() == 0)
            throw new IllegalStateException("Blockchain is empty.");
        else
            return this.chain.getLast();
    }

    private void setDifficulty() { // TODO Requirements unclear, guessing something reasonable...
        if (Blockchain.difficulty >= Config.MAXIMUM_DIFFICULTY) {
            Blockchain.difficulty--;
            System.out.println("N was decreased by 1\n");
            return;
        }

        if (this.getLastBlock().getBlockCreationTime() < Config.BLOCK_CREATION_TIME_THRESHOLD_INCREASE) {
            Blockchain.difficulty++;
            System.out.println("N was increased to " + Blockchain.difficulty);
        } else if (this.getLastBlock().getBlockCreationTime() < Config.BLOCK_CREATION_TIME_THRESHOLD_DECREASE) {
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
        for (BlockInterface block : this.chain)
            sb.append(block.toString()).append("\n");
        return sb.toString();
    }

    public boolean isValid() {
        synchronized (this.lock.readLock()) {
            if (this.getChainSize() == 0)
                return true;
            else
               return validateBlockchain();
        }
    }

    private boolean validateBlockchain() {
        List<BlockInterface> blocks = List.copyOf(this.chain);
        BlockInterface previousBlock = blocks.get(0);

        for (BlockInterface block : blocks) {
            if (block.equals(previousBlock))
               continue;

            if (!isBlockOrderValid(previousBlock, block))
                return false;

            if (!block.isValid()) {
                System.err.println("Block with id " + block.getId() + " is not valid.");
                return false;
            }

            previousBlock = block;
        }
        return true;
    }

    private boolean isBlockOrderValid(BlockInterface first, BlockInterface second) {
        if (first.getId() >= second.getId()) {
            System.err.println("Block with id " + first.getId() + " is not valid.");
            return false;
        }

        if (!second.getPreviousHash().equals(first.getHash())) {
            System.err.println("Previous hash does not match for block with id " + second.getId());
            return false;
        }

        return true;
    }

    public Iterable<BlockInterface> getBlocks() {
        return new LinkedList<>(this.chain);
    }
}
