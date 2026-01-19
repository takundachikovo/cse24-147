package bank.controller;

import bank.model.Customer;

public class CustomerController {
    private LoginController loginController;
    
    public CustomerController(LoginController loginController) {
        this.loginController = loginController;
    }
    
    public Customer getCurrentCustomer() {
        return loginController.getCurrentCustomer();
    }
}