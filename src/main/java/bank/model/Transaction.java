package bank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String type;
    private double amount;
    private double balanceAfter;
    private String description;
    private LocalDateTime date;

    public Transaction(String type, double amount, double balanceAfter, String description) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.date = LocalDateTime.now();
    }


    public String getTransactionId() { return transactionId; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }
    public LocalDateTime getDate() { return date; }

    public String getFormattedAmount() {
        return String.format("P %.2f", amount);
    }

    public String getFormattedBalanceAfter() {
        return String.format("P %.2f", balanceAfter);
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}