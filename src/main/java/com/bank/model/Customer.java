package com.bank.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String username;
    private String password;
    private List<Account> accounts;
    
    public Customer() {
        this.accounts = new ArrayList<>();
    }
    
    public Customer(String firstName, String surname, String address, String username, String password) {
        this();
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.username = username;
        this.password = password;
        this.customerId = generateCustomerId();
    }
    
    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }
    
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
    
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    
    public String getFullName() {
        return firstName + " " + surname;
    }
}