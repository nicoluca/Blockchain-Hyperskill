package blockchain.domain.block;

import blockchain.domain.transactions.Transaction;
import blockchain.utils.StringUtil;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockWithTransactions extends Block implements BlockInterface {
    private final Deque<Transaction> transactions;

    public BlockWithTransactions(Block block, Deque<Transaction> transactions) {
        super(block.getPreviousHash(), block.getBlockId(), block.getMiner(), block.getTimeStamp(), block.getMagicNumber(), block.getHash(), block.getBlockCreationTime());
        this.transactions = transactions;
    }

    public Iterable<Transaction> readTransactions() {
        return new ArrayDeque<>(this.transactions);
    }

    public static String calculateHash(int blockId, String previousHash, long timeStamp, long magicNumber, Deque<Transaction> transactions) {
        return StringUtil.applySha256(
                blockId +
                        previousHash +
                        timeStamp +
                        magicNumber +
                        transactions.stream()
                                .map(Transaction::toString)
                                .reduce("", (a, b) -> a + b)
        );
    }

    @Override
    public boolean isValid() {
        if (!this.hash.equals(calculateHash(this.getBlockId(), this.getPreviousHash(), this.getTimeStamp(), this.getMagicNumber(), this.transactions))) {
            System.err.println("Block hash is not valid");
            System.err.println("Expected: " + calculateHash(this.getBlockId(), this.getPreviousHash(), this.getTimeStamp(), this.getMagicNumber(), this.transactions));
            System.err.println("Actual: " + this.getHash());
            return false;
        }

        return transactions.stream()
                .allMatch(Transaction::isValid);
    }

    @Override
    public String toString() {
        return "Block:\n" +
                "Created by: miner" + this.getMiner().getId() + "\n" +
                "miner" + this.getMiner().getId() + " gets 100 VC\n" +
                "Id: " + this.getBlockId() + "\n" +
                "Timestamp: " + this.getTimeStamp() + "\n" +
                "Magic number: " + this.getMagicNumber() + "\n" +
                "Hash of the previous block: \n" + this.getPreviousHash() + "\n" +
                "Hash of the block: \n" + this.getHash() + "\n" +
                "Block data: " + this.getTransactionTexts() + "\n" +
                "Block was generating for " + this.getBlockCreationTime() + " seconds";
    }

    private String getTransactionTexts() {
        if (transactions.isEmpty())
            return "\nNo transactions";

        return transactions.stream()
                .map(Transaction::toString)
                .reduce("", (a, b) -> a + "\n" + b);
    }
}
