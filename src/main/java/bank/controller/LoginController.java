
package bank.controller;

import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.CompanyCustomer;
import bank.service.BankService;

public class LoginController {
    private BankService bankService;
    private Customer currentCustomer;

    public LoginController() {
        this.bankService = new BankService();
    }

    public Customer login(String username, String password) {
        try {
            System.out.println("🔐 Attempting login for: " + username);
            Customer customer = bankService.login(username, password);
            if (customer != null) {
                this.currentCustomer = customer;
                System.out.println("✅ LoginController: Login successful for " + username);
                System.out.println("👤 Customer ID: " + customer.getCustomerId());
                System.out.println("🏢 Customer Type: " + customer.getCustomerType());


                var accounts = bankService.getCustomerAccounts(customer.getCustomerId());
                System.out.println("📊 Number of accounts: " + accounts.size());

                for (var account : accounts) {
                    System.out.println("   - " + account.getAccountNumber() + " : " + account.getAccountType() + " : P " + account.getBalance());
                }
            } else {
                System.out.println("❌ LoginController: Login failed for " + username);
            }
            return customer;
        } catch (Exception e) {
            System.err.println("❌ LoginController: Login error - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Customer registerIndividual(String firstName, String surname, String address,
                                       String username, String password, String email,
                                       String idNumber, String occupation, String employmentStatus) {
        try {
            System.out.println("📝 Starting INDIVIDUAL registration for: " + username);
            Customer customer = bankService.registerIndividualCustomer(firstName, surname, address, username,
                    password, email, idNumber, occupation, employmentStatus);
            if (customer != null) {
                this.currentCustomer = customer;
                System.out.println("✅ LoginController: Individual registration successful for " + username);
                System.out.println("👤 Customer ID: " + customer.getCustomerId());
                System.out.println("🆔 ID Number: " + ((IndividualCustomer) customer).getIdNumber());
            } else {
                System.out.println("❌ LoginController: Individual registration failed for " + username);
            }
            return customer;
        } catch (Exception e) {
            System.err.println("❌ LoginController: Individual registration error - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Customer registerCompany(String firstName, String surname, String address,
                                    String username, String password, String email,
                                    String registrationNumber, String businessType,
                                    String contactPerson, String companySize) {
        try {
            System.out.println("📝 Starting COMPANY registration for: " + username);
            Customer customer = bankService.registerCompanyCustomer(firstName, surname, address, username,
                    password, email, registrationNumber, businessType, contactPerson, companySize);
            if (customer != null) {
                this.currentCustomer = customer;
                System.out.println("✅ LoginController: Company registration successful for " + username);
                System.out.println("👤 Customer ID: " + customer.getCustomerId());
                System.out.println("🏢 Registration Number: " + ((CompanyCustomer) customer).getRegistrationNumber());
            } else {
                System.out.println("❌ LoginController: Company registration failed for " + username);
            }
            return customer;
        } catch (Exception e) {
            System.err.println("❌ LoginController: Company registration error - " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }


    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
        System.out.println("✅ LoginController: Current customer set to " + (customer != null ? customer.getUsername() : "null"));
    }

    public void logout() {
        System.out.println("🚪 LoginController: Logging out " + (currentCustomer != null ? currentCustomer.getUsername() : "null"));
        this.currentCustomer = null;
    }
}
    
