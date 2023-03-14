package blockchain.domain;

import blockchain.Config;
import blockchain.domain.block.Block;
import blockchain.domain.block.BlockWithMessage;
import blockchain.domain.block.BlockWithMessageFactory;
import blockchain.domain.block.Blockchain;
import blockchain.domain.messages.CryptoWallet;
import blockchain.utils.CryptographyUtil;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block> {
    private final Blockchain blockchain;
    private final int minerId;
    private final CryptoWallet cryptoWallet;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public Miner(Blockchain blockchain, int minerId) throws NoSuchAlgorithmException {
        this.blockchain = blockchain;
        this.minerId = minerId;
        this.cryptoWallet = new CryptoWallet();

        KeyPair keyPair = CryptographyUtil.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    @Override
    public BlockWithMessage call() {
        Optional<Block> nextBlock = BlockWithMessageFactory.getInstance().tryToMineBlock(blockchain, minerId);

        if (nextBlock.isPresent() && nextBlock.get() instanceof BlockWithMessage)
            return (BlockWithMessage) nextBlock.get();
        else
            throw new RuntimeException("Miner " + this.minerId + " was interrupted.");
    }
}
