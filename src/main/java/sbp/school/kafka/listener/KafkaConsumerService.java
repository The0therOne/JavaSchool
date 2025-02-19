package sbp.school.kafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbp.school.kafka.dto.TransactionDto;

import java.time.Duration;
import java.util.*;

public class KafkaConsumerService implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    private final String topic;
    private final KafkaConsumer<String, TransactionDto> consumer;

    private final Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
    public KafkaConsumerService(Properties kafkaConsumerProperties){
        this.consumer = new KafkaConsumer<>(kafkaConsumerProperties);
        this.topic = kafkaConsumerProperties.getProperty("topic.name");
    }

    /**
     * Listening kafka topic to consume messages (records)
     */
    public void listen(){
        this.consumer.subscribe(Collections.singletonList(topic));
        log.info("Listening : {}", topic);

        try{
            while (true){
                ConsumerRecords<String, TransactionDto> transactionRecords = consumer.poll(Duration.ofMillis(100));

                for(ConsumerRecord<String, TransactionDto> transactionRecord : transactionRecords){
                    try {
                        log.info("Transaction RECORD: {}", transactionRecord);
                        processConsumedRecord(transactionRecord);

                        currentOffset.put(
                                new TopicPartition(transactionRecord.topic(), transactionRecord.partition()),
                                new OffsetAndMetadata(transactionRecord.offset() + 1)
                        );
                    } catch (Exception e) {
                        log.error("Error processing message: {}", transactionRecord, e);
                    }
                }

                if (!currentOffset.isEmpty()){
                    consumer.commitAsync(KafkaConsumerService::onCommitComplete);
                }
            }
        } catch (Exception e) {
            log.error("Unknown error while consuming record : {}, {}", e, e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try{
                if (!currentOffset.isEmpty()) {
                    consumer.commitSync(currentOffset);
                }
            } finally {
                log.info("Closing consumer...");
                currentOffset.clear();
                consumer.close();
            }
        }
    }

    private static void onCommitComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        if (exception != null) {
            log.error("Error while async commit - offset={}", offsets, exception);
        }
    }

    private void processConsumedRecord(ConsumerRecord<String, TransactionDto> record){
        TransactionDto transactionDto = record.value();
        if (transactionDto != null){
            log.info("Message successfully processed: {}, offset={}", transactionDto, record.offset());
        } else {
            log.error("Message processing failed. Transaction is null : {}", record.offset());
        }
    }

    /**
     * Безопасный выход консюмера из потока, судя по документации Multi-threaded Processing
     * ...which can safely be used from an external thread to interrupt an active operation
     */
    @Override
    public void close() {
        log.info("Consumer closed");
        consumer.wakeup();
    }
}
