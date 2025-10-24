package com.bank.controller;

import com.bank.model.Customer;
import com.bank.service.AuthenticationService;
import com.bank.service.AccountService;

public class LoginController {
    private AuthenticationService authService;
    private AccountService accountService;
    private Customer currentCustomer;

    public LoginController() {
        this.authService = new AuthenticationService();
        this.accountService = new AccountService();
    }

    public Customer login(String username, String password) {
        Customer customer = authService.login(username, password);
        if (customer != null) {
            
            accountService.loadCustomerAccounts(customer);
            this.currentCustomer = customer;
        }
        return customer;
    }

    public boolean register(Customer customer) {
        boolean success = authService.register(customer);
        if (success) {
            this.currentCustomer = customer;
        }
        return success;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void logout() {
        this.currentCustomer = null;
    }
}