package com.bank.service;

import com.bank.dao.CustomerDAO;
import com.bank.model.Customer;

public class AuthenticationService {
    private CustomerDAO customerDAO;

    public AuthenticationService() {
        this.customerDAO = new CustomerDAO();
    }

    public Customer login(String username, String password) {
        Customer customer = customerDAO.getCustomerByUsername(username);
        if (customer != null && customer.getPassword().equals(password)) {
            System.out.println("Login successful for user: " + username);
            return customer;
        }
        System.out.println("Login failed for user: " + username);
        return null;
    }

    public boolean register(Customer customer) {
        if (customerDAO.customerExists(customer.getUsername())) {
            System.out.println("Registration failed - username exists: " + customer.getUsername());
            return false;
        }

        customerDAO.addCustomer(customer);
        System.out.println("Registration successful for user: " + customer.getUsername());
        return true;
    }
}