package com.bank.dao;

import com.bank.model.Customer;
import java.util.List;

public class CustomerDAO {

    public void addCustomer(Customer customer) {
        TextFileDatabase.saveCustomer(customer);
        System.out.println("Customer saved: " + customer.getUsername());
    }

    public Customer getCustomerByUsername(String username) {
        return TextFileDatabase.getCustomerByUsername(username);
    }

    public List<Customer> getAllCustomers() {
        return TextFileDatabase.getAllCustomers();
    }

    public boolean customerExists(String username) {
        return getCustomerByUsername(username) != null;
    }
}