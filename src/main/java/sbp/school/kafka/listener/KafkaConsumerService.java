package sbp.school.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerService implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final String topic;
    private final KafkaConsumer<String, String> consumer;
    public KafkaConsumerService(Properties kafkaConsumerProperties){
        this.consumer = new KafkaConsumer<>(kafkaConsumerProperties);
        this.topic = kafkaConsumerProperties.getProperty("topic.name");
    }

    public void listen(){
        this.consumer.subscribe(List.of(this.topic));
        try{
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for(ConsumerRecord<String, String> record : records){
                    log.info("topic = {}; partition = {}: offset = {}; value = {};",
                            record.topic(), record.partition(), record.offset(), record.value());
                }
            }
        } catch (Exception e) {
            log.error("Error while consuming record : {}, {}", e, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
        log.info("Consumer exit");
        consumer.wakeup();
    }
}
