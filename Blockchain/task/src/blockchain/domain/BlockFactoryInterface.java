package blockchain.domain;

import java.util.Optional;

public interface BlockFactoryInterface {
    Optional<Block> tryToMineBlock(Blockchain blockchain, int minerId);
}
