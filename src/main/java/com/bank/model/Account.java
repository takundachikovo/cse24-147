package com.bank.model;

public abstract class Account {
    protected String accountNumber;
    protected String branch;
    protected Customer owner;
    protected double balance;
    
    public Account(String branch, Customer owner) {
        this.branch = branch;
        this.owner = owner;
        this.balance = 0.0;
        this.accountNumber = generateAccountNumber();
    }
    
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
    
    
    public abstract String getAccountType();
    public abstract boolean canWithdraw();
    
   
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (canWithdraw() && amount > 0 && balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    
    public Customer getOwner() { return owner; }
    public void setOwner(Customer owner) { this.owner = owner; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}