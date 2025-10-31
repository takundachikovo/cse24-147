package com.bank.dao;

import com.bank.model.Transaction;
import java.util.List;

public class TransactionDAO {

    public void addTransaction(Transaction transaction) {
        TextFileDatabase.saveTransaction(transaction);
        System.out.println("Transaction saved: " + transaction.getTransactionId());
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        return TextFileDatabase.getTransactionsByAccountNumber(accountNumber);
    }
}