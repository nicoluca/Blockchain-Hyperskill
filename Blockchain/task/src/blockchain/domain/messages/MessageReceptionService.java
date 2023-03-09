package blockchain.domain.messages;

import blockchain.domain.security.VerifyMessage;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageReceptionService {
    private static MessageReceptionService instance;
    private final Deque<Message> messages;
    private final ReadWriteLock readWriteLock;

    private MessageReceptionService() {
        this.messages = new ConcurrentLinkedDeque<>();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public synchronized static MessageReceptionService getInstance() {
        if (instance == null) {
            instance = new MessageReceptionService();
        }
        return instance;
    }

    void addMessage(Message message) {
        synchronized (readWriteLock.writeLock()) {
            verifyAndAddMessage(message);
        }
    }

    private void verifyAndAddMessage(Message message) {
        try {
            if (!VerifyMessage.verifyMessage(message))
                throw new InvalidKeyException("Message is not valid.");
            if (!(message.getMessageId() > this.messages.size()))
                System.err.println("Message ID is not valid for message: " + message);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }

        this.messages.add(message);
    }

    public synchronized Deque<Message> getMessages() {
        synchronized (readWriteLock.readLock()) {
            return this.messages;
        }
    }

     public void clearMessages() {
        synchronized (readWriteLock.writeLock()) {
            this.messages.clear();
        }
    }


}
