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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction() {
    }

    public Transaction(String accountNumber, Double amount, String string, String note, LocalDateTime now, Type type) {
    }
}
