package bank.service;

import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.CompanyCustomer;
import bank.repository.CustomerRepository;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }

    public Customer registerIndividualCustomer(String firstName, String surname, String address,
                                               String username, String password, String email,
                                               String idNumber, String occupation, String employmentStatus) {

        System.out.println("=== CUSTOMER SERVICE: Starting INDIVIDUAL registration for: " + username);


        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }
        if (idNumber == null || idNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Number cannot be empty");
        }

        try {
            System.out.println("Checking if username exists: " + username);
            if (customerRepository.usernameExists(username)) {
                throw new IllegalArgumentException("Username '" + username + "' already exists");
            }

            IndividualCustomer customer = new IndividualCustomer(null, firstName, surname, address,
                    username, password, email, idNumber, occupation, employmentStatus);

            System.out.println("IndividualCustomer object created, calling repository...");

            Customer savedCustomer = customerRepository.saveCustomer(customer);

            if (savedCustomer == null) {
                throw new RuntimeException("Registration failed - customer not saved");
            }

            System.out.println("=== INDIVIDUAL REGISTRATION SUCCESSFUL! Customer ID: " + savedCustomer.getCustomerId());
            return savedCustomer;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public Customer registerCompanyCustomer(String firstName, String surname, String address,
                                            String username, String password, String email,
                                            String registrationNumber, String businessType,
                                            String contactPerson, String companySize) {

        System.out.println("=== CUSTOMER SERVICE: Starting COMPANY registration for: " + username);


        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration Number cannot be empty");
        }

        try {
            System.out.println("Checking if username exists: " + username);
            if (customerRepository.usernameExists(username)) {
                throw new IllegalArgumentException("Username '" + username + "' already exists");
            }

            CompanyCustomer customer = new CompanyCustomer(null, firstName, surname, address,
                    username, password, email, registrationNumber, businessType, contactPerson, companySize);

            System.out.println("CompanyCustomer object created, calling repository...");

            Customer savedCustomer = customerRepository.saveCustomer(customer);

            if (savedCustomer == null) {
                throw new RuntimeException("Registration failed - customer not saved");
            }

            System.out.println("=== COMPANY REGISTRATION SUCCESSFUL! Customer ID: " + savedCustomer.getCustomerId());
            return savedCustomer;

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public Customer login(String username, String password) {
        try {
            Customer customer = customerRepository.findCustomerByUsername(username);
            if (customer != null && customer.getPassword().equals(password)) {
                System.out.println("Login successful for: " + username + " - Type: " + customer.getCustomerType());
                return customer;
            }
            System.out.println("Login failed for: " + username);
            return null;
        } catch (Exception e) {
            System.err.println("Database error during login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean usernameExists(String username) {
        try {
            return customerRepository.usernameExists(username);
        } catch (Exception e) {
            System.err.println("Database error checking username: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}