package sbp.school.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbp.school.kafka.dto.TransactionDto;

import java.util.Properties;

/**
 * Kafka Producer Class
 */
public class KafkaProducerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private final String topic;
    private final KafkaProducer<String, TransactionDto> producer;

    public KafkaProducerService(Properties kafkaProducerProperties){
        this.producer = new KafkaProducer<>(kafkaProducerProperties);
        this.topic = kafkaProducerProperties.getProperty("topic.name");
    }

    /**
     * Send transaction to kafka
     * @param transactionDto - transaction
     */
    public void send(TransactionDto transactionDto){
        ProducerRecord<String, TransactionDto> record = new ProducerRecord<>(topic, transactionDto);

        producer.send(record, ((recordMetadata, e) -> {
            if (e != null) {
                log.error("Sending message failed. Partition: {}, Offset: {}", recordMetadata.partition(), recordMetadata.offset());
            } else {
                log.error("Message successfully sent. Partition: {}, Offset: {}", recordMetadata.partition(), recordMetadata.offset());
            }
        }));
    }

    /**
     * Sending last messages in batch and closing producer to save resources.
     */
    public void close(){
        producer.close();
    }

}
