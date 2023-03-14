package blockchain.domain.transactions;

import blockchain.domain.wallet.CryptoWalletInterface;
import blockchain.utils.CryptographyUtil;

import java.math.BigDecimal;

public class Transaction implements Verifiable {
    private final CryptoWalletInterface sendingWallet;
    private final CryptoWalletInterface receivingWallet;
    private final BigDecimal amount;
    private byte[] signature = null;

    Transaction(CryptoWalletInterface sender, CryptoWalletInterface recipient, BigDecimal amount) {
        this.sendingWallet = sender;
        this.receivingWallet = recipient;
        this.amount = amount;
        this.signature = sender.createSignature(this);
    }

    @Override
    public boolean isValid() {
        if (signature == null)
            return false;

        return CryptographyUtil.verifySignature(this.toString().getBytes(), this.signature, this.sendingWallet.getPublicKey());
    }

    public CryptoWalletInterface getSendingWallet() {
        return this.sendingWallet;
    }

    public CryptoWalletInterface getReceivingWallet() {
        return this.receivingWallet;
    }

    @Override
    public String toString() {
        return this.sendingWallet.getOwner().getName() + " sent " + this.amount + " VC to " + this.receivingWallet.getOwner().getName();
    }

    public BigDecimal getAmount() {
        return this.amount;
    }
}
