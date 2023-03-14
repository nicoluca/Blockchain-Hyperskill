package blockchain;

public class Config {
    public static final int BLOCKCHAIN_SIZE = 5;
    public static final int NUMBER_OF_MINERS = 10;
    public static final int NUMBER_OF_MESSENGERS = 2;
    public static final int MESSAGES_PER_SECOND_PER_MESSENGER = 5;
    public static final int INITIAL_DIFFICULTY = 0;
    public static final int KEY_LENGTH = 1024;
    public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    public static final String KEY_FACTORY_ALGORITHM = "RSA";
    public static final long INITIAL_WALLET_BALANCE = 100;
}
