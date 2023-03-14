package blockchain.domain.wallet;

import blockchain.domain.CryptoOwner;
import blockchain.domain.block.Blockchain;
import blockchain.domain.transactions.TransactionReceptionService;
import blockchain.domain.transactions.Verifiable;
import blockchain.utils.CryptographyUtil;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class CryptoWallet implements CryptoWalletInterface, Signer {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private final Blockchain blockchain;
    private final CryptoOwner owner;

    public CryptoWallet(Blockchain blockchain, CryptoOwner owner) {
        this.blockchain = blockchain;
        KeyPair keyPair = CryptographyUtil.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
        this.owner = owner;
    }

    @Override
    public BigDecimal getBalance() {
        return TransactionReceptionService.getBalance(this.blockchain, this);
    }

    @Override
    public byte[] createSignature(Verifiable verifiable) {
        return CryptographyUtil.generateSignature(verifiable.toString().getBytes(), privateKey);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public CryptoOwner getOwner() {
        return this.owner;
    }
}
