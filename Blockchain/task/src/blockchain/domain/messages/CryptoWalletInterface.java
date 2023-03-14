package blockchain.domain.messages;

import java.math.BigDecimal;
import java.security.PublicKey;

public interface CryptoWalletInterface {
    BigDecimal getBalance();
    void addBalance(BigDecimal amount);
    void subtractBalance(BigDecimal amount);
}
