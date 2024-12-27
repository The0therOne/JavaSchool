package sbp.school.kafka.config;

import java.util.Properties;


/**
 * Kafka Properties Class
 * Required kafka-producer.properties file in src/main/resources/ with kafka producer properties
 */
public class KafkaProducerConfig {
    private final Properties properties = new Properties();

    public KafkaProducerConfig(){
        try {
            properties.load(KafkaProducerConfig.class.getClassLoader().getResourceAsStream("kafka-producer.properties"));
        } catch (Exception e){
            System.out.println("Error while loading kafka configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }
}
