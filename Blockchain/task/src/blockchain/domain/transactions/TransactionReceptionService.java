package blockchain.domain.transactions;

import blockchain.Config;
import blockchain.domain.block.BlockInterface;
import blockchain.domain.block.BlockWithTransactions;
import blockchain.domain.block.Blockchain;
import blockchain.domain.wallet.CryptoWalletInterface;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TransactionReceptionService {
    private static TransactionReceptionService instance;
    private final Deque<Transaction> transactions;
    private final ReadWriteLock readWriteLock;

    public synchronized static TransactionReceptionService getInstance() {
        if (instance == null) {
            instance = new TransactionReceptionService();
        }
        return instance;
    }

    private TransactionReceptionService() {
        this.transactions = new ConcurrentLinkedDeque<>();
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public void addTransaction(Transaction transaction) {
        synchronized (readWriteLock.writeLock()) {
            try {
                verifyAndAddTransaction(transaction);
            } catch (IllegalArgumentException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void verifyAndAddTransaction(Transaction transaction) throws InvalidKeyException {
        if (!transaction.isValid())
            throw new InvalidKeyException("Transaction is not valid.");

        if (transaction.getAmount()
                .compareTo(transaction.getSendingWallet().getBalance()) > 0)
            throw new IllegalArgumentException("Sender does not have enough VC.");

        this.transactions.add(transaction);
    }

    public synchronized Deque<Transaction> readTransactions() {
        synchronized (readWriteLock.readLock()) {
            return new ArrayDeque<>(this.transactions);
        }
    }

    public void removeTransactions(Deque<Transaction> transactionsToRemove) {
        synchronized (readWriteLock.writeLock()) {
            this.transactions.removeAll(transactionsToRemove);
        }
    }

    public static BigDecimal getBalance(Blockchain blockchain, CryptoWalletInterface cryptoWalletInterface) {
        BigDecimal balance = BigDecimal.valueOf(Config.INITIAL_WALLET_BALANCE);

        for (BlockInterface block : blockchain.getBlocks()) {
            if (!(block instanceof BlockWithTransactions))
                throw new RuntimeException("Block is not a BlockWithTransactions.");

            if (block.getMiner().getOwner().equals(cryptoWalletInterface.getOwner()))
                balance = balance.add(BigDecimal.valueOf(Config.MINING_REWARD));

            for (Transaction transaction : ((BlockWithTransactions) block).readTransactions()) {
                if (transaction.getSendingWallet().equals(cryptoWalletInterface))
                    balance = balance.subtract(transaction.getAmount());
                if (transaction.getReceivingWallet().equals(cryptoWalletInterface))
                    balance = balance.add(transaction.getAmount());
            }
        }

        return balance;
    }
}
