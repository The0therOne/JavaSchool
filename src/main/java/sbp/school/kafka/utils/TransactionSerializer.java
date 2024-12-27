package sbp.school.kafka.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import sbp.school.kafka.dto.TransactionDto;

import java.nio.charset.StandardCharsets;

/**
 * Custom serializer for messages.
 */
public class TransactionSerializer implements Serializer<TransactionDto> {
    private final ObjectMapper objectMapper;
    private final JsonSchemaValidator jsonValidator;

    public TransactionSerializer(){
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        this.jsonValidator = new JsonSchemaValidator();
    }

    @Override
    public byte[] serialize(String topic, TransactionDto transactionDto) {
        if (transactionDto != null){
            try {
                String jsonString = objectMapper.writeValueAsString(transactionDto);
                String jsonSchemaPath = Constants.JSON_SCHEMA_FILE;
                jsonValidator.validateJson(jsonString, jsonSchemaPath);
                return jsonString.getBytes(StandardCharsets.UTF_8);
            } catch (Exception e){
                System.out.println("[ERROR] Serialization error: " + e.getMessage());
                throw new SerializationException(e);
            }
        } else {
            System.out.println("Transaction is null.");
            throw new RuntimeException("Transaction is null!");
        }
    }
}
