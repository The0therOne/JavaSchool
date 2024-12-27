package sbp.school.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import sbp.school.kafka.dto.TransactionDto;

import java.util.Properties;

/**
 * Kafka Producer Class
 */
public class KafkaProducerService {
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
                System.out.printf("[ERROR] Error while sending message.\nPartition: %s\nOffset: %s\n", recordMetadata.partition(), recordMetadata.offset());
            } else {
                System.out.printf("[INFO] Message successfully sent.\nPartition: %s\nOffset: %s\n", recordMetadata.partition(), recordMetadata.offset());
            }
        }));
    }

    /**
     * Sending last messages in batch and closing producer to save resources.
     */
    public void close(){
        System.out.println("[INFO] Sending last messages.");
        producer.flush();
        System.out.println("[INFO] Closing producer.");
        producer.close();
    }

}
