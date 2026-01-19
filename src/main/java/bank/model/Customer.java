package bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private String username;
    private String password;
    private String email;
    private List<Account> accounts;
    protected String customerType;

    public Customer(String customerId, String firstName, String surname, String address,
                    String username, String password, String email, String customerType) {
        this.customerId = customerId;
        this.firstName = firstName != null ? firstName : "";
        this.surname = surname != null ? surname : "";
        this.address = address != null ? address : "";
        this.username = username != null ? username : "";
        this.password = password != null ? password : "";
        this.email = email != null ? email : "";
        this.accounts = new ArrayList<>();
        this.customerType = customerType;

        System.out.println("Customer object created - ID: " + customerId + ", Type: " + customerType + ", Username: " + username);
    }


    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getAddress() { return address; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getCustomerType() { return customerType; }
    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account account) {
        accounts.add(account);
        System.out.println("Account added to customer " + username + ": " + account.getAccountNumber());
    }

    public String getFullName() {
        return firstName + " " + surname;
    }

    public abstract String getDisplayInfo();
}