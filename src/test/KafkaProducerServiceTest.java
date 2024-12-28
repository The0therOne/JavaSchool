import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sbp.school.kafka.config.KafkaProducerConfig;
import sbp.school.kafka.dto.OperationType;
import sbp.school.kafka.dto.TransactionDto;
import sbp.school.kafka.producer.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class KafkaProducerServiceTest {
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void testInit(){
        KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();
        kafkaProducerService = new KafkaProducerService(kafkaProducerConfig.getProperties());
    }

    @Test
    void sendAllTypesTransactions(){
        List<TransactionDto> testTransactions = testTransactionsDtoList();
        assertDoesNotThrow(() -> testTransactions.forEach(testTransaction -> {
            kafkaProducerService.send(testTransaction);
            System.out.println("[INFO] Test transaction sent: " + testTransaction);
        }));
    }

    public static List<TransactionDto> testTransactionsDtoList(){
        return Arrays.asList(
                new TransactionDto(
                        0,
                        OperationType.COMMISSIONS,
                        200.0,
                        "1234123412341234",
                        LocalDateTime.now()
                ),
                new TransactionDto(
                        1,
                        OperationType.DEPOSIT,
                        167.0,
                        "9999444433331111",
                        LocalDateTime.now()
                ),
                new TransactionDto(
                        2,
                        OperationType.TRANSFER,
                        2167.4,
                        "1685924392596969",
                        LocalDateTime.now()
                ),
                new TransactionDto(
                        3,
                        OperationType.WITHDRAWAL,
                        162.2,
                        "9999444433331111",
                        LocalDateTime.now()
                ),
                new TransactionDto(
                        4,
                        OperationType.DZ,
                        754.0,
                        "9999444433331111",
                        LocalDateTime.now()
                ),
                new TransactionDto(
                        5,
                        OperationType.VSZ,
                        1963.1,
                        "1234123412341234",
                        LocalDateTime.now()
                )

        );
    }

    @AfterEach
    void producerClose(){
        if (kafkaProducerService != null){
            kafkaProducerService.close();
        }
    }
}
