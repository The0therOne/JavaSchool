package sbp.school.kafka.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbp.school.kafka.dto.TransactionDto;

import java.nio.charset.StandardCharsets;

/**
 * По аналогии с Serializer, кастомный Deserializer
 */
public class TransactionDeserializer implements Deserializer<TransactionDto> {
    private static final Logger log = LoggerFactory.getLogger(TransactionDeserializer.class);

    private final ObjectMapper objectMapper;
    private final JsonSchemaValidator jsonValidator;

    public TransactionDeserializer() {
        this.objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        this.jsonValidator = new JsonSchemaValidator();
    }

    /**
     *
     * @param topic имя топика
     * @param data сериализованное сообщение в байты
     * @return объект TransactionDto
     */
    @Override
    public TransactionDto deserialize(String topic, byte[] data) {
        if (data != null){
            try {
                String jsonString = new String(data, StandardCharsets.UTF_8);
                String jsonSchemaPath = Constants.JSON_SCHEMA_FILE;
                jsonValidator.validateJson(jsonString, jsonSchemaPath);
                return objectMapper.readValue(jsonString, TransactionDto.class);
            } catch (Exception e){
                log.error("Deserialization error: {}", e.getMessage());
                return null;
            }
        } else {
            log.error("Transaction is null");
            throw new RuntimeException("Transaction is null");
        }
    }
}
