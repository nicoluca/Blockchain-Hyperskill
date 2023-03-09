package blockchain.domain.block;

import java.util.Optional;

public interface BlockFactoryInterface {
    Optional<Block> tryToMineBlock(Blockchain blockchain, int minerId);
}
