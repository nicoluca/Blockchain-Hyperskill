package blockchain.domain.messages;

import blockchain.domain.block.Blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageSendService {
    private static MessageSendService instance;
    private static ReadWriteLock radWriteLock;

    public synchronized static MessageSendService getInstance() {
        if (instance == null)
            instance = new MessageSendService();
        return instance;
    }

    private MessageSendService() {
        this.radWriteLock = new ReentrantReadWriteLock();
    }

    void sendMessageToReceptionService(String messageText, Messenger messenger, PrivateKey privateKey) {
        synchronized (radWriteLock.writeLock()) {
            try {
                long messageId = Blockchain.getInstance().getNextMessageId();
                Message message = new Message(messageText, messageId, privateKey, messenger.getPublicKey());
                MessageReceptionService.getInstance().addMessage(message);
            } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
                System.err.println("Message could not be sent.");
                throw new RuntimeException(e);
            }
        }
    }
}
