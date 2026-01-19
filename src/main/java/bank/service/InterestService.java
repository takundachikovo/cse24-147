package bank.service;

import bank.model.Account;
import bank.model.InterestBearing;
import bank.repository.AccountRepository;
import java.util.List;

public class InterestService {
    private AccountRepository accountRepository;

    public InterestService() {
        this.accountRepository = new AccountRepository();
    }

    public void applyMonthlyInterest() {
        try {
            List<Account> allAccounts = accountRepository.getAllAccounts();
            System.out.println("💰 Applying monthly interest to " + allAccounts.size() + " accounts");

            int interestApplied = 0;
            for (Account account : allAccounts) {
                if (account instanceof InterestBearing) {
                    double oldBalance = account.getBalance();
                    ((InterestBearing) account).applyInterest();
                    double newBalance = account.getBalance();


                    accountRepository.updateAccountBalance(account.getAccountNumber(), newBalance);

                    double interestAmount = newBalance - oldBalance;
                    if (interestAmount > 0) {
                        interestApplied++;
                        System.out.println("✅ Applied interest to " + account.getAccountNumber() +
                                ": P " + String.format("%.2f", interestAmount) +
                                " (Old: P " + String.format("%.2f", oldBalance) +
                                ", New: P " + String.format("%.2f", newBalance) + ")");
                    }
                }
            }

            System.out.println("💰 Monthly interest applied to " + interestApplied + " accounts");

        } catch (Exception e) {
            System.err.println("❌ Error while applying monthly interest: " + e.getMessage());
            throw new RuntimeException("Failed to apply monthly interest: " + e.getMessage(), e);
        }
    }
}

