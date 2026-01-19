package bank.controller;

import bank.model.Account;
import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.CompanyCustomer;
import bank.service.BankService;
import java.util.List;

public class AccountController {
    private BankService bankService;
    private LoginController loginController;

    public AccountController(LoginController loginController) {
        this.bankService = new BankService();
        this.loginController = loginController;
    }

    public Account openSavingsAccount(double initialDeposit, String branch) {
        Customer customer = loginController.getCurrentCustomer();
        if (customer != null) {
            System.out.println("💰 Creating Savings Account ===");
            System.out.println("👤 Customer: " + customer.getUsername() + " (" + customer.getCustomerType() + ")");
            System.out.println("💵 Initial Deposit: P " + initialDeposit);
            System.out.println("🏢 Branch: " + branch);


            Account account = bankService.openSavingsAccount(customer, initialDeposit, branch);

            if (account != null) {
                System.out.println("✅ SUCCESS: Savings Account created - " + account.getAccountNumber());
                System.out.println("📊 New balance: P " + account.getBalance());
                System.out.println("🎉 Account opened successfully! You can now view it in your dashboard.");
            } else {
                System.out.println("❌ FAILED: Savings Account creation returned null");
            }

            return account;
        }
        System.out.println("❌ FAILED: No customer logged in for savings account");
        return null;
    }

    public Account openInvestmentAccount(double initialDeposit, String branch) {
        Customer customer = loginController.getCurrentCustomer();
        if (customer != null) {
            System.out.println("📈 Creating Investment Account ===");
            System.out.println("👤 Customer: " + customer.getUsername() + " (" + customer.getCustomerType() + ")");
            System.out.println("💵 Initial Deposit: P " + initialDeposit);
            System.out.println("🏢 Branch: " + branch);


            if (initialDeposit < 500) {
                System.out.println("❌ FAILED: Investment account requires minimum P 500.00 deposit");
                return null;
            }


            Account account = bankService.openInvestmentAccount(customer, initialDeposit, branch);

            if (account != null) {
                System.out.println("✅ SUCCESS: Investment Account created - " + account.getAccountNumber());
                System.out.println("📊 New balance: P " + account.getBalance());
                System.out.println("🎉 Investment account opened successfully! Enjoy 5% monthly interest.");
            } else {
                System.out.println("❌ FAILED: Investment Account creation returned null");
            }

            return account;
        }
        System.out.println("❌ FAILED: No customer logged in for investment account");
        return null;
    }

    public Account openChequeAccount(double initialDeposit, String branch,
                                     String companyName, String companyAddress) {
        Customer customer = loginController.getCurrentCustomer();
        if (customer != null) {
            System.out.println("💳 Creating Cheque Account ===");
            System.out.println("👤 Customer: " + customer.getUsername() + " (" + customer.getCustomerType() + ")");
            System.out.println("💵 Initial Deposit: P " + initialDeposit);
            System.out.println("🏢 Branch: " + branch);
            System.out.println("🏢 Company/Employment: " + companyName);


            Account account = bankService.openChequeAccount(customer, initialDeposit, branch,
                    companyName, companyAddress);

            if (account != null) {
                System.out.println("✅ SUCCESS: Cheque Account created - " + account.getAccountNumber());
                System.out.println("📊 New balance: P " + account.getBalance());
                System.out.println("🎉 Cheque account opened successfully! Perfect for salary deposits.");
            } else {
                System.out.println("❌ FAILED: Cheque Account creation returned null");
            }

            return account;
        }
        System.out.println("❌ FAILED: No customer logged in for cheque account");
        return null;
    }

    public List<Account> getCustomerAccounts() {
        Customer customer = loginController.getCurrentCustomer();
        if (customer != null) {
            System.out.println("📋 AccountController: Getting accounts for customer: " + customer.getCustomerId() + " (" + customer.getCustomerType() + ")");

            List<Account> accounts = bankService.getCustomerAccounts(customer.getCustomerId());
            System.out.println("📊 Retrieved " + accounts.size() + " accounts for customer: " + customer.getUsername());

            if (accounts.isEmpty()) {
                System.out.println("💡 No accounts found for customer. They need to create accounts first.");
            } else {
                for (Account account : accounts) {
                    System.out.println("   ✅ " + account.getAccountNumber() + " : " + account.getAccountType() + " : P " + account.getBalance());
                }
            }
            return accounts;
        }
        System.out.println("❌ AccountController: No customer logged in");
        return List.of();
    }

    public double getTotalBalance() {
        Customer customer = loginController.getCurrentCustomer();
        if (customer != null) {

            double total = bankService.getTotalBalance(customer.getCustomerId());
            System.out.println("💰 AccountController: Total balance for " + customer.getUsername() + " (" + customer.getCustomerType() + ") = P " + total);
            return total;
        }
        System.out.println("❌ AccountController: No customer logged in for balance calculation");
        return 0.0;
    }


    public boolean createAccountDuringRegistration(Customer customer, String accountType, double initialDeposit, String branch) {
        try {
            System.out.println("💰 Creating Account During Registration ===");
            System.out.println("👤 Customer: " + customer.getUsername() + " (" + customer.getCustomerType() + ")");
            System.out.println("💳 Account Type: " + accountType);
            System.out.println("💵 Initial Deposit: P " + initialDeposit);
            System.out.println("🏢 Branch: " + branch);


            boolean success = bankService.createFirstAccount(customer, accountType, initialDeposit, branch);

            if (success) {
                System.out.println("✅ SUCCESS: Account created during registration");

                loginController.setCurrentCustomer(customer);
            } else {
                System.out.println("❌ FAILED: Account creation during registration failed");
            }

            return success;

        } catch (Exception e) {
            System.err.println("❌ Error creating account during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}