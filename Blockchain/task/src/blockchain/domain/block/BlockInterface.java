package blockchain.domain.block;

public interface BlockInterface {
    String getPreviousHash();
    int getBlockId();
    long getMinerId();
    long getMagicNumber();
    String getHash();
}
