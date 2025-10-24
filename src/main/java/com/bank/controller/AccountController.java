package com.bank.controller;

import com.bank.model.*;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;

import java.util.List;

public class AccountController {
    private AccountService accountService;
    private TransactionService transactionService;
    
    public AccountController() {
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
    }
    
    public boolean openSavingsAccount(Customer customer) {
        return accountService.openSavingsAccount(customer, "Main Branch");
    }
    
    public boolean openInvestmentAccount(Customer customer, double initialDeposit) {
        return accountService.openInvestmentAccount(customer, "Main Branch", initialDeposit);
    }
    
    public boolean openChequeAccount(Customer customer, String companyName, String companyAddress) {
        return accountService.openChequeAccount(customer, "Main Branch", companyName, companyAddress);
    }
    
    public boolean deposit(Account account, double amount) {
        return accountService.deposit(account, amount);
    }
    
    public boolean withdraw(Account account, double amount) {
        return accountService.withdraw(account, amount);
    }
    
    public List<Account> getCustomerAccounts(String customerId) {
        return accountService.getCustomerAccounts(customerId);
    }
    
    public List<Transaction> getAccountTransactions(String accountNumber) {
        return transactionService.getAccountTransactions(accountNumber);
    }
    
    public double getTotalBalance(List<Account> accounts) {
        return accounts.stream().mapToDouble(Account::getBalance).sum();
    }
    
    public void applyMonthlyInterest(Customer customer) {
        for (Account account : customer.getAccounts()) {
            if (account instanceof InterestBearing) {
                accountService.applyInterest((InterestBearing) account);
            }
        }
    }
}