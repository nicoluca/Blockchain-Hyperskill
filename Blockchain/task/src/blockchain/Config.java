package blockchain;

import blockchain.domain.block.BlockFactoryInterface;
import blockchain.domain.block.BlockWithTransactionFactory;

public class Config {
    public static final int BLOCKCHAIN_SIZE = 15;
    public static final int NUMBER_OF_MINERS = 10;
    public static final int INITIAL_DIFFICULTY = 0;
    public static final int KEY_LENGTH = 1024;
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String KEY_FACTORY_ALGORITHM = "RSA";
    public static final long INITIAL_WALLET_BALANCE = 100;
    public static final BlockFactoryInterface BLOCK_FACTORY = BlockWithTransactionFactory.getInstance();
    public static final int MAXIMUM_TRANSACTION_AMOUNT = 100;
    public static final long TRANSACTIONS_PER_SECOND = 3;
    public static final int BLOCK_CREATION_TIME_THRESHOLD_INCREASE = 10;
    public static final int BLOCK_CREATION_TIME_THRESHOLD_DECREASE = 10;
    public static final int MAXIMUM_DIFFICULTY = 5;
    public static final long MINING_REWARD = 100;
}
