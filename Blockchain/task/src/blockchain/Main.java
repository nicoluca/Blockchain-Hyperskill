package blockchain;

import blockchain.domain.Blockchain;
import blockchain.domain.Miner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int BLOCKCHAIN_SIZE = 5;
    private static final int NUMBER_OF_MINERS = 5;
    private static ExecutorService threadPool;
    private static List<Miner> miners = new ArrayList<>();

    public static void main(String[] args) {
        Blockchain blockchain = Blockchain.getInstance();
        Main.startMining(blockchain);
    }

    private static void startMining(Blockchain blockchain) {
        while (blockchain.getChainSize() < BLOCKCHAIN_SIZE) {
            threadPool = Executors.newFixedThreadPool(NUMBER_OF_MINERS);
            for (int i = 0; i < NUMBER_OF_MINERS; i++)
                miners.add(new Miner(i));

            // Shut down all miners once one of them finds a valid block
            try {
                Boolean result = threadPool.invokeAny(miners);
                threadPool.shutdownNow();
                miners.clear();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

}
