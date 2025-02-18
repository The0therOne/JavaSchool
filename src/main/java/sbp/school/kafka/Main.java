package sbp.school.kafka;

import sbp.school.kafka.config.KafkaConsumerConfig;
import sbp.school.kafka.listener.ThreadListener;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Properties properties = new KafkaConsumerConfig().getProperties();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new ThreadListener(properties));
        executorService.submit(new ThreadListener(properties));
    }
}
