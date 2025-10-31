package com.bank.dao;

import com.bank.model.Account;
import java.util.List;

public class AccountDAO {

    public void addAccount(Account account) {
        TextFileDatabase.saveAccount(account);
        System.out.println("Account saved: " + account.getAccountNumber());
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        return TextFileDatabase.getAccountsByCustomerId(customerId);
    }

    public void updateAccountBalance(String accountNumber, double newBalance) {
        TextFileDatabase.updateAccountBalance(accountNumber, newBalance);
        System.out.println("Account balance updated: " + accountNumber + " = " + newBalance);
    }
}