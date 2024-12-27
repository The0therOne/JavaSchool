package sbp.school.kafka.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.File;

/**
 * Json validator by json schema file.
 */
public class JsonSchemaValidator {
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
                throw new RuntimeException("JSON is not valid");
            }
        } catch (Exception e){
            System.out.println("[ERROR] Validation error : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
