package sbp.school.kafka;

import sbp.school.kafka.config.KafkaConsumerConfig;
import sbp.school.kafka.listener.KafkaConsumerService;

import java.util.Properties;

public class Main {
    /**
     * Точка входа. Запуск listen() у consumer.
     * Хук на завершение работы приложения,
     * чтобы поток нормально завершился через вызов close() метода у листенера.
     * @param args Аргументы консоли
     */
    public static void main(String[] args) {
        Properties properties = new KafkaConsumerConfig().getProperties();

        try(KafkaConsumerService consumerService = new KafkaConsumerService(properties)){
            Runtime.getRuntime().addShutdownHook(new Thread(consumerService::close));
            consumerService.listen();
        }
    }
}
