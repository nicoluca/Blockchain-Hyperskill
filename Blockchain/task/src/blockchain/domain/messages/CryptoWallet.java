package blockchain.domain.messages;

import blockchain.Config;

import java.math.BigDecimal;

public class CryptoWallet implements CryptoWalletInterface {
    private BigDecimal balance;

    public CryptoWallet() {
        this.balance = BigDecimal.valueOf(Config.INITIAL_WALLET_BALANCE);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance;
    }

    @Override
    public void addBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    @Override
    public void subtractBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

}
