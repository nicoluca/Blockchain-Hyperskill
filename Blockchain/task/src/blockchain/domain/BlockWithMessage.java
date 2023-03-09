package blockchain.domain;

import blockchain.utils.StringUtil;

public class BlockWithMessage extends Block implements BlockInterface {
    private final String message;

    public BlockWithMessage(Block block, String message) {
        super(block.getPreviousHash(), block.getBlockId(), block.getMinerId(), block.getMagicNumber(), block.getHash(), block.getBlockCreationTime());
        this.message = message.isEmpty() ? "no messages" : "\n" + message;
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by miner # " + this.getMinerId() + "\n" +
                "Id: " + this.getBlockId() + "\n" +
                "Timestamp: " + this.getTimeStamp() + "\n" +
                "Magic number: " + this.getMagicNumber() + "\n" +
                "Hash of the previous block: \n" + this.getPreviousHash() + "\n" +
                "Hash of the block: \n" + this.getHash() + "\n" +
                "Block data: " + this.message + "\n" +
                "Block was generating for " + this.getBlockCreationTime() + " seconds";
    }

    public static String calculateHash(int blockId, String previousHash, long timeStamp, long magicNumber, String message) {
        return StringUtil.applySha256(
                blockId +
                        previousHash +
                        timeStamp +
                        magicNumber +
                        message
        );
    }
}
