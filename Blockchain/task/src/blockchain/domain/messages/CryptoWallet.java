package blockchain.domain.messages;

import blockchain.Config;
import blockchain.utils.CryptographyUtil;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptoWallet implements CryptoWalletInterface {
    private BigDecimal balance;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public CryptoWallet() {
        this.balance = BigDecimal.valueOf(Config.INITIAL_WALLET_BALANCE);
        KeyPair keyPair = CryptographyUtil.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
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

    @Override
    public byte[] createSignature(byte[] dataToSign) {
        return CryptographyUtil.generateSignature(dataToSign, privateKey);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
