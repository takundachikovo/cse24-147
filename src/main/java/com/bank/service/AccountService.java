package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;
import com.bank.model.*;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
    }

    public boolean openSavingsAccount(Customer customer, String branch) {
        SavingsAccount account = new SavingsAccount(branch, customer);
        accountDAO.addAccount(account);
        loadCustomerAccounts(customer);
        return true;
    }

    public boolean openInvestmentAccount(Customer customer, String branch, double initialDeposit) {
        if (initialDeposit < InvestmentAccount.getMinOpeningBalance()) {
            return false;
        }

        InvestmentAccount account = new InvestmentAccount(branch, customer, initialDeposit);
        accountDAO.addAccount(account);
        loadCustomerAccounts(customer);

        Transaction transaction = new Transaction(initialDeposit, "DEPOSIT", account);
        transactionDAO.addTransaction(transaction);

        return true;
    }

    public boolean openChequeAccount(Customer customer, String branch, String companyName, String companyAddress) {
        ChequeAccount account = new ChequeAccount(branch, customer, companyName, companyAddress);
        accountDAO.addAccount(account);
        loadCustomerAccounts(customer);
        return true;
    }

    public boolean deposit(Account account, double amount) {
        if (amount <= 0) return false;

        account.deposit(amount);
        accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance());

        Transaction transaction = new Transaction(amount, "DEPOSIT", account);
        transactionDAO.addTransaction(transaction);

        return true;
    }

    public boolean withdraw(Account account, double amount) {
        if (amount <= 0 || !account.canWithdraw()) return false;

        boolean success = account.withdraw(amount);
        if (success) {
            accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance());

            Transaction transaction = new Transaction(amount, "WITHDRAWAL", account);
            transactionDAO.addTransaction(transaction);
        }

        return success;
    }

    public List<Account> getCustomerAccounts(String customerId) {
        return accountDAO.getAccountsByCustomerId(customerId);
    }

    public void loadCustomerAccounts(Customer customer) {
        List<Account> accounts = getCustomerAccounts(customer.getCustomerId());
        customer.setAccounts(accounts);
        System.out.println("Loaded " + accounts.size() + " accounts for customer: " + customer.getCustomerId());
    }

    public void applyInterest(InterestBearing account) {
        if (account instanceof SavingsAccount) {
            ((SavingsAccount) account).applyInterest();
        } else if (account instanceof InvestmentAccount) {
            ((InvestmentAccount) account).applyInterest();
        }
    }
}