//package blockchain.domain.messages;
//
//import blockchain.domain.block.Blockchain;
//
//import java.math.BigDecimal;
//
//public class TransactionService {
//    public static void generateTransaction(Blockchain blockchain, CryptoWallet sender, CryptoWallet recipient, BigDecimal amount) {
//        Transaction transaction = new Transaction(sender.getPublicKey(), recipient.getPublicKey(), amount);
//        transaction.signTransaction(sender);
//        blockchain.addTransaction(transaction);
//    }
//
//    public static BigDecimal getBalance(Blockchain blockchain, CryptoWallet cryptoWallet) {
//        BigDecimal balance = BigDecimal.ZERO;
//
//        blockchain.getBlocks()
//                .forEach(block -> {
//                    block.getTransactions()
//                            .forEach(transaction -> {
//                                if (transaction.getSender().equals(cryptoWallet.getPublicKey())) {
//                                    balance = balance.subtract(transaction.getAmount());
//                                }
//
//                                if (transaction.getRecipient().equals(cryptoWallet.getPublicKey())) {
//                                    balance = balance.add(transaction.getAmount());
//                                }
//                            });
//                });
//
//
//        return balance;
//    }
//}
