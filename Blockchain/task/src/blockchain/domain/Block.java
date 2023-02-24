package blockchain.domain;

import blockchain.utils.MineUtil;
import blockchain.utils.StringUtil;

import java.util.Date;
import java.util.Random;

public class Block {
    private static int id = 1;
    private final int blockId;
    private final String hash;
    private final String previousHash;
    private final long timeStamp;
    private long magicNumber;
    private final int blockCreationTime;
    private final long minerId;

    private Block(String previousHash, int blockId, long minerId) {
        long startTime = System.currentTimeMillis();
        this.minerId = minerId;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.magicNumber = MineUtil.getRandomMagicLong();
        this.hash = calculateValidHash();
        this.blockId = blockId;
        this.blockCreationTime = MineUtil.timeSinceInSeconds(startTime);
    }

    public static Block createBlock(Blockchain blockchain , int minerId) {
        return new Block(blockchain.getLastBlockHash(), blockchain.getLastBlockId() + 1, minerId);
    }

    private String calculateValidHash() {
        String currentHash = this.calculateCurrentHash();
        while (!MineUtil.startsWithValidZeros(currentHash, Blockchain.numberOfHashZeros)) {
            this.magicNumber = MineUtil.getRandomMagicLong(); // Brute force randomly, not incrementally, as per requirement
            currentHash = this.calculateCurrentHash();
        }
        return currentHash;
    }

    private String calculateCurrentHash() {
        return StringUtil.applySha256(
                this.blockId +
                        this.previousHash +
                        this.timeStamp +
                        this.magicNumber);
    }

    String getHash() {
        return this.hash;
    }

    String getPreviousHash() {
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
}
