package sbp.school.kafka.listener;

import java.util.Properties;

public class ThreadListener extends Thread {
    private static KafkaConsumerService kafkaConsumerService;

    public ThreadListener(Properties properties) {
        kafkaConsumerService = new KafkaConsumerService(properties);
    }

    @Override
    public void run() {

        super.run();
    }
}
