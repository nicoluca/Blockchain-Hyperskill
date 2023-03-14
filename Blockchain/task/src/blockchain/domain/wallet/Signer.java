package blockchain.domain.wallet;

import blockchain.domain.transactions.Verifiable;

public interface Signer {
    byte[] createSignature(Verifiable verifiable);
}
