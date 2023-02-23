package blockchain;

import java.util.Date;
import java.util.Random;

public class Block {

    private static int numberofPrefixedHashZeros;
    private static int id = 1;
    private final int blockId;
    private String hash;
    private final String previousHash;
    private final long timeStamp;
    private long magicNumber;

    public Block(String previousHash) {
        this.blockId = id++;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.magicNumber = Block.getRandomMagicLong();
        this.hash = calculateValidHash();
    }

    public static void setNumberofPrefixedHashZeros(int mumberofPrefixedHashZeros) {
        Block.numberofPrefixedHashZeros = mumberofPrefixedHashZeros;
    }

    private static long getRandomMagicLong() {
        Random random = new Random();
        return random.nextLong();
    }

    private String calculateValidHash() {
        String currentHash = this.calculateCurrentHash();
        while (!startsWithValidZeros(currentHash)) {
            this.magicNumber = Block.getRandomMagicLong();
            currentHash = this.calculateCurrentHash();
        }
        return currentHash;
    }

    private boolean startsWithValidZeros(String currentHash) {
        String regex = "^0{" + Block.numberofPrefixedHashZeros + "}.*";
        return currentHash.matches(regex);
    }

    private String calculateCurrentHash() {
        return StringUtil.applySha256(
                this.blockId +
                        this.previousHash +
                        this.timeStamp +
                        this.magicNumber);
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Block:\n");
        sb.append("Id: ").append(blockId).append("\n");
        sb.append("Timestamp: ").append(timeStamp).append("\n");
        sb.append("Hash of the previous block: \n").append(previousHash).append("\n");
        sb.append("Hash of the block: \n").append(hash);
        return sb.toString();
    }
}
