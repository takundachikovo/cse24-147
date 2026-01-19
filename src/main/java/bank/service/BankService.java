package bank.service;

import bank.model.*;
import java.util.List;

public class BankService {
    private CustomerService customerService;
    private AccountService accountService;
    private TransactionService transactionService;
    private InterestService interestService;

    public BankService() {
        this.customerService = new CustomerService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.interestService = new InterestService();
    }


    public Customer registerIndividualCustomer(String firstName, String surname, String address,
                                               String username, String password, String email,
                                               String idNumber, String occupation, String employmentStatus) {
        return customerService.registerIndividualCustomer(firstName, surname, address, username,
                password, email, idNumber, occupation, employmentStatus);
    }

    public Customer registerCompanyCustomer(String firstName, String surname, String address,
                                            String username, String password, String email,
                                            String registrationNumber, String businessType,
                                            String contactPerson, String companySize) {
        return customerService.registerCompanyCustomer(firstName, surname, address, username,
                password, email, registrationNumber, businessType, contactPerson, companySize);
    }

    public Customer login(String username, String password) {
        return customerService.login(username, password);
    }


    public Account openSavingsAccount(Customer customer, double initialDeposit, String branch) {
        return accountService.openSavingsAccount(customer, initialDeposit, branch);
    }

    public Account openInvestmentAccount(Customer customer, double initialDeposit, String branch) {
        return accountService.openInvestmentAccount(customer, initialDeposit, branch);
    }

    public Account openChequeAccount(Customer customer, double initialDeposit, String branch,
                                     String companyName, String companyAddress) {
        return accountService.openChequeAccount(customer, initialDeposit, branch, companyName, companyAddress);
    }

    public boolean deposit(String accountNumber, double amount) {
        return accountService.deposit(accountNumber, amount);
    }

    public boolean withdraw(String accountNumber, double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    public double getTotalBalance(String customerId) {
        return accountService.getTotalBalance(customerId);
    }

    public List<Account> getCustomerAccounts(String customerId) {
        return accountService.getCustomerAccounts(customerId);
    }


    public boolean createFirstAccount(Customer customer, String accountType, double initialDeposit, String branch) {
        try {
            System.out.println("🏦 Creating first account for new customer: " + customer.getUsername());
            System.out.println("💳 Account Type: " + accountType);
            System.out.println("💰 Initial Deposit: P " + initialDeposit);

            Account account = null;

            switch (accountType) {
                case "Savings Account":
                    account = openSavingsAccount(customer, initialDeposit, branch);
                    break;
                case "Investment Account":
                    account = openInvestmentAccount(customer, initialDeposit, branch);
                    break;
                case "Cheque Account":

                    if (customer instanceof CompanyCustomer) {
                        CompanyCustomer companyCustomer = (CompanyCustomer) customer;
                        account = openChequeAccount(customer, initialDeposit, branch,
                                companyCustomer.getBusinessType(), customer.getAddress());
                    } else if (customer instanceof IndividualCustomer) {
                        IndividualCustomer individual = (IndividualCustomer) customer;
                        account = openChequeAccount(customer, initialDeposit, branch,
                                individual.getOccupation(), customer.getAddress());
                    }
                    break;
            }

            if (account != null) {
                System.out.println("✅ First account created successfully: " + account.getAccountNumber());
                return true;
            } else {
                System.out.println("❌ Failed to create first account");
                return false;
            }

        } catch (Exception e) {
            System.err.println("❌ Error creating first account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}