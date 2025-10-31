package com.bank.service;

import com.bank.dao.TransactionDAO;
import com.bank.model.Transaction;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO;

    public TransactionService() {
        this.transactionDAO = new TransactionDAO();
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        return transactionDAO.getTransactionsByAccountNumber(accountNumber);
    }
}