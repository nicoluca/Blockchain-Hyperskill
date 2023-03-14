package blockchain.domain.transactions;

import blockchain.domain.CryptoOwner;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TransactionFactory {
    private static TransactionFactory instance;
    private static ReadWriteLock readWriteLock;

    public synchronized static TransactionFactory getInstance() {
        if (instance == null)
            instance = new TransactionFactory();
        return instance;
    }

    private TransactionFactory() {
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public void generateTransaction(CryptoOwner sender, CryptoOwner recipient, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive.");
        if (sender.equals(recipient))
            throw new IllegalArgumentException("Sender and recipient must be different.");

        synchronized (readWriteLock.writeLock()) {
                Transaction transaction = new Transaction(sender.getCryptoWallet(), recipient.getCryptoWallet(), amount);
                TransactionReceptionService.getInstance().addTransaction(transaction);
        }
    }
}
