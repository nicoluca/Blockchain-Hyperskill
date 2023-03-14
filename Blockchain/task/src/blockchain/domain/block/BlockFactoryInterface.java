package blockchain.domain.block;

import blockchain.domain.miner.MinerInterface;

import java.util.Optional;

public interface BlockFactoryInterface {
    Optional<BlockInterface> tryToMineBlock(Blockchain blockchain, MinerInterface miner);
}
