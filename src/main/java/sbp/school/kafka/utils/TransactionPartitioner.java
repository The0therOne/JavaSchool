package sbp.school.kafka.utils;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sbp.school.kafka.dto.OperationType;
import sbp.school.kafka.dto.TransactionDto;

import java.util.List;
import java.util.Map;

/**
 * Custom Transaction partitioner. Num of partitions and num of operation types must be equal.
 */
public class TransactionPartitioner implements Partitioner {
    private static final Logger log = LoggerFactory.getLogger(TransactionPartitioner.class);
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int partitionSize = partitionInfos.size();

        if (OperationType.values().length != partitionSize){
            log.error("Size of operation types not equal to partitions size.");
            throw new RuntimeException("Size of operation types not equal to partition size");
        } else {
            return ((TransactionDto) value).getOperationType().ordinal();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
