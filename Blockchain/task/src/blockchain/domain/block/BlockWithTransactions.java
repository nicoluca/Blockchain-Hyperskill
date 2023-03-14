package blockchain.domain.block;

import blockchain.domain.messages.Transaction;
import blockchain.utils.StringUtil;

import java.util.ArrayDeque;
import java.util.Deque;

public class BlockWithTransactions extends Block implements BlockInterface {
    private final Deque<Transaction> transactions;

    public BlockWithTransactions(Block block, Deque<Transaction> transactions) {
        super(block.getPreviousHash(), block.getBlockId(), block.getMinerId(), block.getMagicNumber(), block.getHash(), block.getBlockCreationTime());
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
        if (!super.isValid())
            return false;

        return transactions.stream()
                .allMatch(Transaction::isValid);
    }

}
