package sbp.school.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

/**
 * DTO class which describes Transaction
 */
public class TransactionDto {
    private final int id;
    private final OperationType operationType;
    private final double amount;
    private final String account;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "UTC"
    )
    private final LocalDateTime date;

    @JsonCreator
    public TransactionDto(@JsonProperty("id") int id, @JsonProperty("operationType") OperationType operationType,@JsonProperty("amount") double amount, @JsonProperty("account") String account, @JsonProperty("time") LocalDateTime date) {
        this.id = id;
        this.operationType = operationType;
        this.amount = amount;
        this.account = account;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public double getAmount() {
        return amount;
    }

    public String getAccount() {
        return account;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", operationType=" + operationType +
                ", amount=" + amount +
                ", account='" + account + '\'' +
                ", date=" + date +
                '}';
    }
}
