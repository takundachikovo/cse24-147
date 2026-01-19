package bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String accountNumber;
    protected String branch;
    protected Customer owner;
    protected double balance;
    protected List<Transaction> transactions;
    protected String accountType;
    protected Bank bank;

    public Account(String accountNumber, String branch, Customer owner, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.owner = owner;
        this.balance = balance;
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
        this.bank = new Bank(); // NEW: Default bank instance
    }

    public Account(String accountNumber, String branch, Customer owner, double balance, String accountType, Bank bank) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.owner = owner;
        this.balance = balance;
        this.accountType = accountType;
        this.transactions = new ArrayList<>();
        this.bank = bank;
    }

    public abstract boolean canWithdraw();
    public abstract String getAccountDetails();

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactions.add(new Transaction("DEPOSIT", amount, this.balance, "Deposit to " + bank.getBankName()));
        }
    }

    public boolean withdraw(double amount) {
        if (canWithdraw() && amount > 0 && balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction("WITHDRAWAL", -amount, this.balance, "Withdrawal from " + bank.getBankName()));
            return true;
        }
        return false;
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return transactions; }
    public String getAccountType() { return accountType; }
    public Bank getBank() { return bank; } // NEW: Getter for bank

    // NEW: Setter for bank
    public void setBank(Bank bank) { this.bank = bank; }

    public String getFormattedBalance() {
        return String.format("P %.2f", balance);
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }


    public String getFullAccountInfo() {
        return String.format("%s - %s\nBranch: %s\nBank: %s\nBalance: %s",
                accountNumber, accountType, branch, bank.getBankName(), getFormattedBalance());
    }
}