package blockchain.domain.block;

import blockchain.domain.transactions.Transaction;
import blockchain.domain.transactions.TransactionReceptionService;
import blockchain.domain.miner.MinerInterface;
import blockchain.utils.MineUtil;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Optional;

public class BlockWithTransactionFactory implements BlockFactoryInterface {
    private static BlockWithTransactionFactory instance;

    public synchronized static BlockWithTransactionFactory getInstance() {
        if (instance == null)
            instance = new BlockWithTransactionFactory();
        return instance;
    }

    @Override
    public Optional<BlockInterface> tryToMineBlock(Blockchain blockchain, MinerInterface miner) {
        try {
            return Optional.of(mineBlock(blockchain, miner));
        } catch (InterruptedException e) {
            return Optional.empty();
        }

    }

    private BlockWithTransactions mineBlock(Blockchain blockchain, MinerInterface miner) throws InterruptedException {
        if (blockchain.getChainSize() == 0)
            return mineFirstBlock(miner);
        else
            return mineSubsequentBlock(blockchain, miner);
    }

    private BlockWithTransactions mineSubsequentBlock(Blockchain blockchain, MinerInterface miner) throws InterruptedException {
        long startTime = new Date().getTime();
        BlockInterface previousBlock = blockchain.getLastBlock();
        int blockId = previousBlock.getId() + 1;
        String previousHash = previousBlock.getHash();
        return startMining(blockId, previousHash, miner, startTime);
    }

    private BlockWithTransactions startMining(int blockId, String previousHash, MinerInterface miner, long startTime) throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            long magicNumber = MineUtil.getRandomMagicLong();
            long timeStamp = new Date().getTime();

            Deque<Transaction> transactions = TransactionReceptionService.getInstance().readTransactions();
            String hash = BlockWithTransactions.calculateHash(blockId, previousHash, timeStamp, magicNumber, transactions);

            if (MineUtil.startsWithValidZeros(hash, Blockchain.getDifficulty())) {
                int blockCreationTime = MineUtil.timeSinceInSeconds(startTime);
                TransactionReceptionService.getInstance().removeTransactions(transactions);
                return new BlockWithTransactions(
                        new Block(previousHash, blockId, miner, timeStamp, magicNumber, hash, blockCreationTime),
                        transactions);
            }
        }

        throw new InterruptedException("Miner " + miner.getId() + " was interrupted");
    }

    private BlockWithTransactions mineFirstBlock(MinerInterface miner) throws InterruptedException {
            if (Thread.currentThread().isInterrupted())
                throw new InterruptedException("Miner " + miner.getId() + " was interrupted");

            long startTime = new Date().getTime();
            long magicNumber = MineUtil.getRandomMagicLong();
            long timeStamp = new Date().getTime();
            String hash = Block.calculateHash(1, "0", timeStamp, magicNumber);
            return new BlockWithTransactions(
                    new Block("0", 1, miner, timeStamp, magicNumber, hash, MineUtil.timeSinceInSeconds(startTime)),
                    new ArrayDeque<>());
    }
}