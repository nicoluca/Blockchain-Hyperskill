package blockchain.domain.messages;

import blockchain.utils.StringUtil;

import java.util.List;
import java.util.Random;

public class Messenger implements Runnable {
    private static final int MESSAGES_PER_SECOND = 10;
    @Override
    public void run() {
        List<String> messages = StringUtil.getMockMessages();
        Random random = new Random();
        while (true) {
            try {
                Thread.sleep(1000 / MESSAGES_PER_SECOND);
                String message = messages.get(random.nextInt(messages.size()));
                MessageReceptionService.getInstance().addMessage(message);
            } catch (InterruptedException e) {
                throw new RuntimeException("Messenger was interrupted.");
            }
        }
    }
}
