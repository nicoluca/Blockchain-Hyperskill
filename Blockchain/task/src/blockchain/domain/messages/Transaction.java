package blockchain.domain.messages;

import blockchain.utils.CryptographyUtil;

import java.math.BigDecimal;
import java.security.PublicKey;

public class Transaction implements Verifiable {
    private final PublicKey sender;
    private final PublicKey recipient;
    private final BigDecimal amount;
    private final byte[] data;
    private byte[] signature = null;

    public Transaction(PublicKey sender, PublicKey recipient, BigDecimal amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.data = (sender.toString() + recipient.toString() + amount).getBytes();
    }

    public void signTransaction (CryptoWalletInterface sender) {
        signature = sender.createSignature(this.data);
    }

    @Override
    public boolean isValid() {
        if (signature == null)
            return false;

        return CryptographyUtil.verifySignature(this.data, this.signature, this.sender);
    }

    public PublicKey getSender() {
        return this.sender;
    }

    public PublicKey getRecipient() {
        return this.recipient;
    }
}
