package blockchain.domain.messages;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageReceptionService {
    private static MessageReceptionService instance;
    private final Deque<String> messages;
    private final ReadWriteLock readWriteLock;

    private MessageReceptionService() {
        this.messages = new ConcurrentLinkedDeque<>();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public static MessageReceptionService getInstance() {
        if (instance == null) {
            instance = new MessageReceptionService();
        }
        return instance;
    }

    void addMessage(String message) {
        synchronized (readWriteLock.writeLock()) {
            this.messages.add(message);
        }
    }

    public synchronized String readMessages() {
        synchronized (readWriteLock.readLock()) {
            return String.join("\n", this.messages);
        }
    }

     public void clearMessages() {
        synchronized (readWriteLock.writeLock()) {
            this.messages.clear();
        }
    }


}
