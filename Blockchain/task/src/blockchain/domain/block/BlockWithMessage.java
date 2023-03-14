package blockchain.domain.block;

import blockchain.domain.messages.Message;
import blockchain.utils.StringUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BlockWithMessage extends Block implements BlockInterface {
    private final Deque<Message> messages;

    public BlockWithMessage(Block block, Deque<Message> messages) {
        super(block.getPreviousHash(), block.getBlockId(), block.getMinerId(), block.getMagicNumber(), block.getHash(), block.getBlockCreationTime());
        this.messages = messages.isEmpty() ? new ConcurrentLinkedDeque<>() : messages;
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
                "Block data: " + this.getMessagesText() + "\n" +
                "Block was generating for " + this.getBlockCreationTime() + " seconds";
    }

    private String getMessagesText() {
        if (messages.isEmpty())
            return "no messages";

        return messages.stream()
                .map(Message::getMessageText)
                .reduce("", (a, b) -> a + "\n" + b);
    }

    public Deque<Message> getMessages() {
        return new ArrayDeque<>(this.messages);
    }

    public static String calculateHash(int blockId, String previousHash, long timeStamp, long magicNumber, Deque<Message> messages) {
        return StringUtil.applySha256(
                blockId +
                        previousHash +
                        timeStamp +
                        magicNumber +
                        messages.stream()
                                .map(Message::getMessageText)
                                .reduce("", (a, b) -> a + b)
        );
    }

    @Override
    public boolean isValid() {
        if (!super.isValid())
            return false;
        
        if (!areMessagesvalid())
            return false;
        
        return true;
    }

    private boolean areMessagesvalid() {
        Deque<Message> messages = new ArrayDeque<>(this.messages);
        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.poll();
            if (!message.isValid())
                return false;
            if (i == 0)
                continue;
            
        }
        return true;
    }

}
