package sbp.school.kafka.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Json validator by json schema file.
 */
public class JsonSchemaValidator {
    private static final Logger log = LoggerFactory.getLogger(JsonSchemaValidator.class);
    public void validateJson(String json, String jsonSchemaPath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            File schemaFile = new File(jsonSchemaPath);
            JsonNode schemaNode = objectMapper.readTree(schemaFile);

            JsonNode jsonNode = objectMapper.readTree(json);

            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            ProcessingReport report = schema.validate(jsonNode);
            if (!report.isSuccess()) {
                log.error("JSON is not valid!");
                throw new RuntimeException("JSON is not valid");
            }
        } catch (Exception e){
            log.error("Validation error : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
