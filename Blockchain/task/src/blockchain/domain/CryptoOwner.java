package blockchain.domain;

import blockchain.domain.block.Blockchain;
import blockchain.domain.wallet.CryptoWallet;
import blockchain.domain.transactions.TransactionFactory;
import blockchain.domain.miner.Miner;

import java.math.BigDecimal;
import java.util.Random;

public class CryptoOwner {
    private final String name;
    private final CryptoWallet cryptoWallet;
    private final Miner miner;

    public CryptoOwner(String name, Blockchain blockchain) {
        this.name = name;
        this.cryptoWallet = new CryptoWallet(blockchain, this);
        Random random = new Random();
        this.miner = new Miner(blockchain, random.nextInt(11), this);
    }

    public String getName() {
        return this.name;
    }

    public CryptoWallet getCryptoWallet() {
        return this.cryptoWallet;
    }

    public Miner getMiner() {
        return this.miner;
    }
}
