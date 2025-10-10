package com.bank.model;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date date;
    private double amount;
    private String type; 
    private Account account;
    private double balanceAfter;
    
    public Transaction(double amount, String type, Account account) {
        this.transactionId = "TXN" + System.currentTimeMillis();
        this.date = new Date();
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.balanceAfter = account.getBalance();
    }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    
    public double getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(double balanceAfter) { this.balanceAfter = balanceAfter; }
}