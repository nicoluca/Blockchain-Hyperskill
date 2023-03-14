package blockchain.domain.miner;

import blockchain.domain.block.*;
import blockchain.domain.messages.CryptoWallet;
import blockchain.utils.CryptographyUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

public class Miner implements MinerInterface {
    private final Blockchain blockchain;
    private final int minerId;
    private final CryptoWallet cryptoWallet;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Miner(Blockchain blockchain, int minerId) {
        this.blockchain = blockchain;
        this.minerId = minerId;
        this.cryptoWallet = new CryptoWallet();

        KeyPair keyPair = CryptographyUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    @Override
    public BlockInterface call() {
        Optional<BlockInterface> nextBlock = BlockWithMessageFactory.getInstance().tryToMineBlock(blockchain, this);

        if (nextBlock.isPresent() && nextBlock.get() instanceof BlockWithMessage)
            return nextBlock.get();
        else
            throw new RuntimeException("Miner " + this.minerId + " was interrupted.");
    }

    @Override
    public int getMinerId() {
        return this.minerId;
    }
}
