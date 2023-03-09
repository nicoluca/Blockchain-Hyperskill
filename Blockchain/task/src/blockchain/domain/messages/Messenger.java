package blockchain.domain.messages;

import blockchain.Config;
import blockchain.domain.security.GenerateKeys;
import blockchain.utils.StringUtil;

import java.security.*;
import java.util.List;
import java.util.Random;

public class Messenger implements Runnable {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Messenger() {
        try {
            setUpMessenger();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpMessenger() throws NoSuchAlgorithmException {
        GenerateKeys generateKeys = new GenerateKeys(Config.KEY_LENGTH);
        generateKeys.createKeys();
        this.privateKey = generateKeys.getPrivateKey();
        this.publicKey = generateKeys.getPublicKey();
    }

    @Override
    public void run() {
        List<String> mockMessageData = StringUtil.getMockMessages();
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(1000 / Config.MESSAGES_PER_SECOND_PER_MESSENGER);
                sendRandomMessage(mockMessageData, random);
            } catch (InterruptedException e) {
                throw new RuntimeException("Messenger was interrupted.");
            }
        }
    }

    private void sendRandomMessage(List<String> mockMessageData, Random random) {
            String data = mockMessageData.get(random.nextInt(mockMessageData.size()));
            MessageSendService.getInstance().sendMessageToReceptionService(data, this, privateKey);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
