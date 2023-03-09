package blockchain.domain;

import blockchain.utils.MineUtil;
import blockchain.utils.StringUtil;

import java.util.Date;

public class Block implements BlockInterface {
    private final int blockId;
    protected final String hash;
    private final String previousHash;
    private final long timeStamp;
    private long magicNumber;
    private final int blockCreationTime;
    private final long minerId;

    Block(String previousHash, int blockId, long minerId, long magicNumber, String hash, int blockCreationTime) {
        this.minerId = minerId;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
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

    int getBlockCreationTime() {
        return blockCreationTime;
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + this.minerId + "\n" +
                "Id: " + this.blockId + "\n" +
                "Timestamp: " + timeStamp + "\n" +
                "Magic number: " + this.magicNumber + "\n" +
                "Hash of the previous block: \n" + previousHash + "\n" +
                "Hash of the block: \n" + hash + "\n" +
                "Block was generating for " + this.blockCreationTime + " seconds";
    }

    public int getBlockId() {
        return this.blockId;
    }

    public long getMinerId() {
        return this.minerId;
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
