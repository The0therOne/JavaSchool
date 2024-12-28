package sbp.school.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Kafka Properties Class
 * Required kafka-producer.properties file in src/main/resources/ with kafka producer properties
 */
public class KafkaProducerConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerConfig.class);
    private final Properties properties = new Properties();

    public KafkaProducerConfig(){
        try {
            properties.load(KafkaProducerConfig.class.getClassLoader().getResourceAsStream("kafka-producer.properties"));
        } catch (Exception e){
            log.error("Loading kafka configuration file failed : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }
}
