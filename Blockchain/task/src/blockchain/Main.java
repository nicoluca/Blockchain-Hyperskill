package blockchain;

import blockchain.domain.block.BlockInterface;
import blockchain.domain.block.BlockWithMessage;
import blockchain.domain.block.Blockchain;
import blockchain.domain.messages.Messenger;
import blockchain.domain.miner.Miner;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    private static final List<Miner> miners = new ArrayList<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Blockchain blockchain = Blockchain.getInstance();
        Main.startMiningAndMessaging(blockchain);

        if (!blockchain.isValid())
            throw new IllegalStateException("Blockchain is not valid.");
    }

    private static void startMiningAndMessaging(Blockchain blockchain) throws NoSuchAlgorithmException {
        ExecutorService messengerExecutor = Main.startMessaging();
        Main.startMining(blockchain);
        messengerExecutor.shutdownNow();
    }

    private static ExecutorService startMessaging() {
        ExecutorService threadPool = Executors.newFixedThreadPool(Config.NUMBER_OF_MESSENGERS);
        IntStream.range(0, Config.NUMBER_OF_MESSENGERS).
                forEach(i -> threadPool.submit(new Messenger()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return threadPool;
    }

    private static void startMining(Blockchain blockchain) throws NoSuchAlgorithmException {
        while (blockchain.getChainSize() < Config.BLOCKCHAIN_SIZE) {
            Main.addMinersToPool(Config.NUMBER_OF_MINERS, blockchain);
            Main.mineOneBlock(blockchain, miners);
        }
    }

    private static void addMinersToPool(int numberOfMiners, Blockchain blockchain) throws NoSuchAlgorithmException {
        for (int i = 0; i < numberOfMiners; i++)
            miners.add(new Miner(blockchain, i));
    }

    private static void mineOneBlock (Blockchain blockchain, List<Miner> miners) {
        ExecutorService threadPool = Executors.newFixedThreadPool(miners.size());

        // Shut down all miners once one of them finds a valid block
        try {
            BlockInterface resultingBlock = (BlockWithMessage) threadPool.invokeAny(miners);
            blockchain.addAndPrintBlock(resultingBlock);
            threadPool.shutdownNow();
            miners.clear();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
