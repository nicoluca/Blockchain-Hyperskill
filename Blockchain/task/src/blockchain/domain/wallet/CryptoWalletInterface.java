package blockchain.domain.wallet;

import blockchain.domain.CryptoOwner;

import java.math.BigDecimal;
import java.security.PublicKey;

public interface CryptoWalletInterface extends Signer {
    PublicKey getPublicKey();
    CryptoOwner getOwner();
    public BigDecimal getBalance();
}
