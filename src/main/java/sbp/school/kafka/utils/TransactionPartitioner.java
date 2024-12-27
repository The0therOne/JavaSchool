package sbp.school.kafka.utils;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import sbp.school.kafka.dto.OperationType;
import sbp.school.kafka.dto.TransactionDto;

import java.util.List;
import java.util.Map;

/**
 * Custom Transaction partitioner. Num of partitions and num of operation types must be equal.
 */
public class TransactionPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int partitionSize = partitionInfos.size();

        if (OperationType.values().length != partitionSize){
            throw new RuntimeException("[ERROR] Size of operation types not equal to partition size!");
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
