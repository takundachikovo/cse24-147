package bank.service;

import bank.model.*;
import bank.repository.AccountRepository;
import bank.repository.TransactionRepository;
import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
        this.transactionRepository = new TransactionRepository();
    }


    public Account openSavingsAccount(Customer customer, double initialDeposit, String branch) {
        try {
            String accountNumber = accountRepository.generateAccountNumber();
            System.out.println("💰 Creating Savings Account with number: " + accountNumber);


            Bank bank = new Bank();
            SavingsAccount account = new SavingsAccount(accountNumber, branch, customer, initialDeposit, bank);
            accountRepository.saveAccount(account);

            System.out.println("✅ Savings account opened successfully: " + accountNumber + " at " + bank.getBankName());
            return account;
        } catch (Exception e) {
            System.err.println("❌ Error opening savings account: " + e.getMessage());
            throw new RuntimeException("Failed to open savings account: " + e.getMessage(), e);
        }
    }

    public Account openInvestmentAccount(Customer customer, double initialDeposit, String branch) {
        if (!InvestmentAccount.isValidOpeningBalance(initialDeposit)) {
            throw new IllegalArgumentException("Investment account requires minimum P 500.00 opening deposit");
        }

        try {
            String accountNumber = accountRepository.generateAccountNumber();
            System.out.println("📈 Creating Investment Account with number: " + accountNumber);


            Bank bank = new Bank();
            InvestmentAccount account = new InvestmentAccount(accountNumber, branch, customer, initialDeposit, bank);
            accountRepository.saveAccount(account);

            System.out.println("✅ Investment account opened successfully: " + accountNumber + " at " + bank.getBankName());
            return account;
        } catch (Exception e) {
            System.err.println("❌ Error opening investment account: " + e.getMessage());
            throw new RuntimeException("Failed to open investment account: " + e.getMessage(), e);
        }
    }

    public Account openChequeAccount(Customer customer, double initialDeposit, String branch,
                                     String companyName, String companyAddress) {
        try {
            String accountNumber = accountRepository.generateAccountNumber();
            System.out.println("💳 Creating Cheque Account with number: " + accountNumber);


            Bank bank = new Bank();
            ChequeAccount account = new ChequeAccount(accountNumber, branch, customer, initialDeposit, bank,
                    companyName, companyAddress);
            accountRepository.saveAccount(account);

            System.out.println("✅ Cheque account opened successfully: " + accountNumber + " at " + bank.getBankName());
            return account;
        } catch (Exception e) {
            System.err.println("❌ Error opening cheque account: " + e.getMessage());
            throw new RuntimeException("Failed to open cheque account: " + e.getMessage(), e);
        }
    }

    public boolean deposit(String accountNumber, double amount) {
        try {
            Account account = accountRepository.findAccountByNumber(accountNumber);
            if (account != null && amount > 0) {
                account.deposit(amount);

                if (!account.getTransactions().isEmpty()) {
                    Transaction latestTransaction = account.getTransactions().get(account.getTransactions().size() - 1);
                    transactionRepository.saveTransaction(accountNumber, latestTransaction);
                }

                accountRepository.updateAccountBalance(accountNumber, account.getBalance());
                System.out.println("✅ Deposit successful: P " + amount + " to " + accountNumber + " at " + account.getBank().getBankName());
                return true;
            }
            System.out.println("❌ Deposit failed: Invalid account or amount");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error during deposit: " + e.getMessage());
            throw new RuntimeException("Deposit failed: " + e.getMessage(), e);
        }
    }

    public boolean withdraw(String accountNumber, double amount) {
        try {
            Account account = accountRepository.findAccountByNumber(accountNumber);
            if (account != null && amount > 0) {
                boolean success = account.withdraw(amount);

                if (success) {
                    if (!account.getTransactions().isEmpty()) {
                        Transaction latestTransaction = account.getTransactions().get(account.getTransactions().size() - 1);
                        transactionRepository.saveTransaction(accountNumber, latestTransaction);
                    }

                    accountRepository.updateAccountBalance(accountNumber, account.getBalance());
                    System.out.println("✅ Withdrawal successful: P " + amount + " from " + accountNumber + " at " + account.getBank().getBankName());
                } else {
                    System.out.println("❌ Withdrawal failed: Insufficient funds or account doesn't allow withdrawals");
                }
                return success;
            }
            System.out.println("❌ Withdrawal failed: Invalid account or amount");
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error during withdrawal: " + e.getMessage());
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(), e);
        }
    }

    public List<Account> getCustomerAccounts(String customerId) {
        try {
            List<Account> accounts = accountRepository.getAccountsByCustomer(customerId);
            System.out.println("✅ Retrieved " + accounts.size() + " accounts for customer: " + customerId);
            return accounts;
        } catch (Exception e) {
            System.err.println("❌ Error getting customer accounts: " + e.getMessage());
            throw new RuntimeException("Failed to get customer accounts: " + e.getMessage(), e);
        }
    }

    public double getTotalBalance(String customerId) {
        try {
            double total = getCustomerAccounts(customerId).stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            System.out.println("✅ Total balance for customer " + customerId + ": P " + total);
            return total;
        } catch (Exception e) {
            System.err.println("❌ Error calculating total balance: " + e.getMessage());
            throw new RuntimeException("Failed to calculate total balance: " + e.getMessage(), e);
        }
    }
}