package bank.service;

import bank.model.Transaction;
import bank.repository.TransactionRepository;
import java.util.List;

public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService() {
        this.transactionRepository = new TransactionRepository();
    }

    public void recordTransaction(String accountNumber, Transaction transaction) {
        try {
            transactionRepository.saveTransaction(accountNumber, transaction);
            System.out.println("✅ Transaction recorded: " + transaction.getTransactionId() + " for account " + accountNumber);
        } catch (Exception e) {
            System.err.println("❌ Error recording transaction: " + e.getMessage());
            throw new RuntimeException("Failed to record transaction: " + e.getMessage(), e);
        }
    }

    public List<Transaction> getAccountTransactions(String accountNumber) {
        try {
            List<Transaction> transactions = transactionRepository.getTransactionsByAccount(accountNumber);
            System.out.println("✅ Retrieved " + transactions.size() + " transactions for account " + accountNumber);
            return transactions;
        } catch (Exception e) {
            System.err.println("❌ Error getting transactions: " + e.getMessage());
            throw new RuntimeException("Failed to get transactions: " + e.getMessage(), e);
        }
    }
}