package bank.controller;

import bank.model.Account;
import bank.service.BankService;
import java.util.List;

public class TransactionController {
    private BankService bankService;
    private AccountController accountController;

    public TransactionController(AccountController accountController) {
        this.bankService = new BankService();
        this.accountController = accountController;
    }

    public boolean deposit(String accountNumber, double amount) {
        System.out.println("💰 Processing deposit: " + accountNumber + " amount: P " + amount);

        boolean result = bankService.deposit(accountNumber, amount);
        if (result) {
            System.out.println("✅ Deposit successful");
        } else {
            System.out.println("❌ Deposit failed");
        }
        return result;
    }

    public boolean withdraw(String accountNumber, double amount) {
        System.out.println("💸 Processing withdrawal: " + accountNumber + " amount: P " + amount);

        boolean result = bankService.withdraw(accountNumber, amount);
        if (result) {
            System.out.println("✅ Withdrawal successful");
        } else {
            System.out.println("❌ Withdrawal failed");
        }
        return result;
    }

    public List<Account> getWithdrawableAccounts() {
        List<Account> allAccounts = accountController.getCustomerAccounts();
        List<Account> withdrawableAccounts = allAccounts.stream()
                .filter(Account::canWithdraw)
                .toList();

        System.out.println("📋 Withdrawable accounts: " + withdrawableAccounts.size() + " out of " + allAccounts.size() + " total accounts");
        for (Account account : withdrawableAccounts) {
            System.out.println("   ✅ " + account.getAccountNumber() + " - " + account.getAccountType() + " - P " + account.getBalance());
        }

        return withdrawableAccounts;
    }

    public List<Account> getAllCustomerAccounts() {
        List<Account> allAccounts = accountController.getCustomerAccounts();
        System.out.println("📋 All customer accounts for selection: " + allAccounts.size());
        for (Account account : allAccounts) {
            System.out.println("   📝 " + account.getAccountNumber() + " - " + account.getAccountType() + " - P " + account.getBalance());
        }
        return allAccounts;
    }
}