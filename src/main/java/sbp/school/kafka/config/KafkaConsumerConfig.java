package sbp.school.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaConsumerConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    private final Properties properties = new Properties();

    public KafkaConsumerConfig(){
        try {
            properties.load(KafkaConsumerConfig.class.getClassLoader().getResourceAsStream("kafka-consumer.properties"));
        } catch (Exception e){
            log.error("Loading kafka configuration file failed : {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }
}
