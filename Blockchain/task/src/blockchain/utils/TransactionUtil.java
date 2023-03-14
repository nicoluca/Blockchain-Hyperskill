package blockchain.utils;

import blockchain.Config;
import blockchain.domain.CryptoOwner;
import blockchain.domain.transactions.TransactionFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class TransactionUtil {
    public static void generateRandomTransaction(List<CryptoOwner> owners) {
        Random random = new Random();
        CryptoOwner sender = owners.get(random.nextInt(owners.size()));
        CryptoOwner recipient = owners.get(random.nextInt(owners.size()));

        while (sender.equals(recipient)) {
            recipient = owners.get(random.nextInt(owners.size()));
        }

        BigDecimal amount = BigDecimal.valueOf(random.nextInt(Config.MAXIMUM_TRANSACTION_AMOUNT + 1));
        TransactionFactory.getInstance().generateTransaction(sender, recipient, amount);
    }
}
