package blockchain;

import blockchain.domain.CryptoOwner;
import blockchain.domain.block.BlockInterface;
import blockchain.domain.block.BlockWithTransactions;
import blockchain.domain.block.Blockchain;
import blockchain.utils.TransactionUtil;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {
    private static final List<CryptoOwner> owners = new ArrayList<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Blockchain blockchain = Blockchain.getInstance();
        Main.startMiningAndMessaging(blockchain);

        if (!blockchain.isValid())
            throw new IllegalStateException("Blockchain is not valid.");
    }

    private static void startMiningAndMessaging(Blockchain blockchain) throws NoSuchAlgorithmException {
        for (int i = 0; i < Config.NUMBER_OF_MINERS; i++)
            owners.add(new CryptoOwner("miner" + i, blockchain));

        Thread transctionThread = Main.startTransacting();
        Main.startMining(blockchain);
        transctionThread.interrupt();
    }

    private static Thread startTransacting() {
        Thread transactionsThread = new Thread(Main::generateTransactions);
        transactionsThread.start();

        return transactionsThread;
    }

    private static void generateTransactions() {
        while (!Thread.currentThread().isInterrupted()) {
            TransactionUtil.generateRandomTransaction(owners);
            try {
                Thread.sleep(1000 / Config.TRANSACTIONS_PER_SECOND);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private static void startMining(Blockchain blockchain) throws NoSuchAlgorithmException {
        while (blockchain.getChainSize() < Config.BLOCKCHAIN_SIZE)
            Main.mineOneBlock(blockchain);
    }

    private static void mineOneBlock (Blockchain blockchain) {
        ExecutorService threadPool = Executors.newFixedThreadPool(owners.size());

        // Shut down all miners once one of them finds a valid block
        try {
            BlockInterface resultingBlock = (BlockWithTransactions) threadPool
                    .invokeAny(
                            owners.stream()
                            .map(CryptoOwner::getMiner)
                            .collect(Collectors.toList()));

            blockchain.addAndPrintBlock(resultingBlock);
            threadPool.shutdownNow();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
