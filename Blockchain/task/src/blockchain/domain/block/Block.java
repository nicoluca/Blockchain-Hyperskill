package blockchain.domain.block;

import blockchain.domain.miner.MinerInterface;
import blockchain.utils.StringUtil;

public class Block implements BlockInterface {
    private final int blockId;
    protected final String hash;
    private final String previousHash;
    private final long timeStamp;
    private long magicNumber;
    private final int blockCreationTime;
    private final MinerInterface miner;

    Block(String previousHash, int blockId, MinerInterface miner, long timeStamp, long magicNumber, String hash, int blockCreationTime) {
        this.miner = miner;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.magicNumber = magicNumber;
        this.hash = hash;
        this.blockId = blockId;
        this.blockCreationTime = blockCreationTime;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public int getBlockCreationTime() {
        return blockCreationTime;
    }

    @Override
    public boolean isValid() {
        return this.hash.equals(calculateHash(this.blockId, this.previousHash, this.timeStamp, this.magicNumber));
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + this.miner.getId() + "\n" +
                "Id: " + this.blockId + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + this.magicNumber + "\n" +
                "Hash of the previous block: \n" + previousHash + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block was generating for " + this.blockCreationTime + " seconds";
    }

    public int getId() {
        return this.blockId;
    }

    @Override
    public MinerInterface getMiner() {
        return this.miner;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public long getMagicNumber() {
        return this.magicNumber;
    }


    public static String calculateHash(int blockId, String previousHash, long timeStamp, long magicNumber) {
        return StringUtil.applySha256(
                blockId +
                        previousHash +
                        timeStamp +
                        magicNumber);
    }
}
