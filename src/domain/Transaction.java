package domain;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String accountNumber;
    private Double amount;
    private LocalDateTime timestamp;
    private String note;

    public Transaction(Double amount,String accountNumber,String id ,LocalDateTime timestamp,String note) {
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.id = id;
        this.note = note;
        this.timestamp=timestamp;
    }
    public Transaction() {
    }
}
