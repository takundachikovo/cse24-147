package bank.controller;

import bank.model.Account;
import bank.model.Customer;
import java.util.List;

public class DashboardController {
    private LoginController loginController;
    private AccountController accountController;

    public DashboardController(LoginController loginController, AccountController accountController) {
        this.loginController = loginController;
        this.accountController = accountController;
    }

    public Customer getCurrentCustomer() {
        return loginController.getCurrentCustomer();
    }

    public List<Account> getCustomerAccounts() {
        if (loginController.getCurrentCustomer() != null) {
            List<Account> accounts = accountController.getCustomerAccounts();
            System.out.println("DashboardController: Loaded " + accounts.size() + " accounts for customer");
            return accounts;
        }
        return List.of();
    }

    public double getTotalBalance() {
        return accountController.getTotalBalance();
    }

    public void logout() {
        loginController.logout();
    }
}