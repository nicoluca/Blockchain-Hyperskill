package blockchain;

import blockchain.domain.*;
import blockchain.domain.messages.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    private static final int BLOCKCHAIN_SIZE = 5;
    private static final int NUMBER_OF_MINERS = 5;
    private static final int NUMBER_OF_MESSENGERS = 2;
    private static final List<Miner> miners = new ArrayList<>();

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        Main.startMiningAndMessaging(blockchain);
    }

    private static void startMiningAndMessaging(Blockchain blockchain) {
        ExecutorService messengerExecutor = Main.startMessaging();
        Main.startMining(blockchain);
        messengerExecutor.shutdownNow();
    }

    private static ExecutorService startMessaging() {
        ExecutorService threadPool = Executors.newFixedThreadPool(NUMBER_OF_MESSENGERS);
        IntStream.range(0, NUMBER_OF_MESSENGERS).
                forEach(i -> threadPool.submit(new Messenger()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return threadPool;
    }

    private static void startMining(Blockchain blockchain) {
        while (blockchain.getChainSize() < BLOCKCHAIN_SIZE) {
            Main.addMinersToPool(NUMBER_OF_MINERS, blockchain);
            Main.mineOneBlock(blockchain, miners);
        }
    }

    private static void addMinersToPool(int numberOfMiners, Blockchain blockchain) {
        for (int i = 0; i < numberOfMiners; i++)
            miners.add(new Miner(blockchain, i));
    }

    private static void mineOneBlock (Blockchain blockchain, List<Miner> miners) {
        ExecutorService threadPool = Executors.newFixedThreadPool(miners.size());

        // Shut down all miners once one of them finds a valid block
        try {
            Block resultingBlock = threadPool.invokeAny(miners);
            blockchain.addAndPrintBlock(resultingBlock);
            threadPool.shutdownNow();
            miners.clear();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
