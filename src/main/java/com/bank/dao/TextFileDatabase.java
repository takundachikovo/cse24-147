package com.bank.dao;

import com.bank.model.Customer;
import com.bank.model.Account;
import com.bank.model.Transaction;
import java.io.*;
import java.util.*;

public class TextFileDatabase {
    private static final String DATA_DIR = "./bank_data/";
    private static final String CUSTOMERS_FILE = DATA_DIR + "customers.txt";
    private static final String ACCOUNTS_FILE = DATA_DIR + "accounts.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "transactions.txt";

    static {
        new File(DATA_DIR).mkdirs();
    }

    public static void saveCustomer(Customer customer) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE, true))) {
            writer.println(customer.getCustomerId() + "|" +
                    customer.getFirstName() + "|" +
                    customer.getSurname() + "|" +
                    customer.getAddress() + "|" +
                    customer.getUsername() + "|" +
                    customer.getPassword());
        } catch (IOException e) {
            System.err.println("Error saving customer: " + e.getMessage());
        }
    }

    public static Customer getCustomerByUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[4].equals(username)) {
                    Customer customer = new Customer();
                    customer.setCustomerId(parts[0]);
                    customer.setFirstName(parts[1]);
                    customer.setSurname(parts[2]);
                    customer.setAddress(parts[3]);
                    customer.setUsername(parts[4]);
                    customer.setPassword(parts[5]);
                    return customer;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
        return null;
    }

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    Customer customer = new Customer();
                    customer.setCustomerId(parts[0]);
                    customer.setFirstName(parts[1]);
                    customer.setSurname(parts[2]);
                    customer.setAddress(parts[3]);
                    customer.setUsername(parts[4]);
                    customer.setPassword(parts[5]);
                    customers.add(customer);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
        return customers;
    }

    public static void saveAccount(Account account) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            String companyName = "";
            String companyAddress = "";

            if (account instanceof com.bank.model.ChequeAccount) {
                com.bank.model.ChequeAccount chequeAccount = (com.bank.model.ChequeAccount) account;
                companyName = chequeAccount.getCompanyName();
                companyAddress = chequeAccount.getCompanyAddress();
            }

            writer.println(account.getAccountNumber() + "|" +
                    account.getOwner().getCustomerId() + "|" +
                    account.getAccountType() + "|" +
                    account.getBranch() + "|" +
                    account.getBalance() + "|" +
                    companyName + "|" +
                    companyAddress);
        } catch (IOException e) {
            System.err.println("Error saving account: " + e.getMessage());
        }
    }

    public static List<Account> getAccountsByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && parts[1].equals(customerId)) {
                    Customer customer = new Customer();
                    customer.setCustomerId(customerId);

                    Account account = null;
                    switch (parts[2]) {
                        case "SAVINGS":
                            account = new com.bank.model.SavingsAccount(parts[3], customer);
                            break;
                        case "INVESTMENT":
                            account = new com.bank.model.InvestmentAccount(parts[3], customer, Double.parseDouble(parts[4]));
                            break;
                        case "CHEQUE":
                            String companyName = parts.length > 5 ? parts[5] : "";
                            String companyAddress = parts.length > 6 ? parts[6] : "";
                            account = new com.bank.model.ChequeAccount(parts[3], customer, companyName, companyAddress);
                            break;
                    }

                    if (account != null) {
                        account.setAccountNumber(parts[0]);
                        account.setBalance(Double.parseDouble(parts[4]));
                        accounts.add(account);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading accounts: " + e.getMessage());
        }
        return accounts;
    }

    public static void updateAccountBalance(String accountNumber, double newBalance) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5 && parts[0].equals(accountNumber)) {
                    line = parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3] + "|" + newBalance;
                    if (parts.length > 5) {
                        line += "|" + parts[5];
                    }
                    if (parts.length > 6) {
                        line += "|" + parts[6];
                    }
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading accounts for update: " + e.getMessage());
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
        }
    }

    public static void saveTransaction(Transaction transaction) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.println(transaction.getTransactionId() + "|" +
                    transaction.getAccount().getAccountNumber() + "|" +
                    transaction.getDate().getTime() + "|" +
                    transaction.getAmount() + "|" +
                    transaction.getType() + "|" +
                    transaction.getBalanceAfter());
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    public static List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6 && parts[1].equals(accountNumber)) {
                    Transaction transaction = new Transaction(
                            Double.parseDouble(parts[3]),
                            parts[4],
                            new Account("Main Branch", null) {
                                public String getAccountType() { return ""; }
                                public boolean canWithdraw() { return false; }
                            }
                    );
                    transaction.setTransactionId(parts[0]);
                    transaction.setDate(new Date(Long.parseLong(parts[2])));
                    transaction.setBalanceAfter(Double.parseDouble(parts[5]));
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    public static void initializeSampleData() {
        if (new File(CUSTOMERS_FILE).exists() && new File(ACCOUNTS_FILE).exists()) {
            return;
        }

        String[] sampleCustomers = {
                "CUST001|John|Smith|123 Main St, Gaborone|johnsmith|password123",
                "CUST002|Mary|Johnson|456 Broad St, Francistown|maryj|password123",
                "CUST003|David|Brown|789 River Rd, Maun|davidb|password123"
        };

        String[] sampleAccounts = {
                "ACC1001|CUST001|SAVINGS|Main Branch|5000.00||",
                "ACC1002|CUST001|INVESTMENT|Main Branch|25000.00||",
                "ACC2001|CUST002|SAVINGS|Main Branch|8000.00||",
                "ACC3001|CUST003|CHEQUE|Main Branch|12000.00|ABC Company|123 Business St"
        };

        String[] sampleTransactions = {
                "TXN001|ACC1001|" + System.currentTimeMillis() + "|1000.00|DEPOSIT|6000.00",
                "TXN002|ACC1002|" + System.currentTimeMillis() + "|500.00|INTEREST|25500.00",
                "TXN003|ACC1001|" + System.currentTimeMillis() + "|-200.00|WITHDRAWAL|5800.00"
        };

        try {
            try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
                for (String customer : sampleCustomers) {
                    writer.println(customer);
                }
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
                for (String account : sampleAccounts) {
                    writer.println(account);
                }
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
                for (String transaction : sampleTransactions) {
                    writer.println(transaction);
                }
            }

            System.out.println("✅ Sample data initialized successfully");
            System.out.println("📁 Data files created in: " + DATA_DIR);

        } catch (IOException e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
        }
    }

    public static void viewAllData() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEXT FILE DATABASE CONTENTS");
        System.out.println("=".repeat(60));

        System.out.println("\n--- CUSTOMERS ---");
        List<Customer> customers = getAllCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            System.out.printf("%d. ID: %s, Name: %s %s, Username: %s, Password: %s%n",
                    i + 1, customer.getCustomerId(), customer.getFirstName(),
                    customer.getSurname(), customer.getUsername(), customer.getPassword());
        }
        System.out.println("Total customers: " + customers.size());

        System.out.println("\n--- ACCOUNTS ---");
        int accountCount = 0;
        for (Customer customer : customers) {
            List<Account> accounts = getAccountsByCustomerId(customer.getCustomerId());
            for (Account account : accounts) {
                accountCount++;
                System.out.printf("%d. Account: %s, Customer: %s, Type: %s, Balance: P%.2f%n",
                        accountCount, account.getAccountNumber(), customer.getCustomerId(),
                        account.getAccountType(), account.getBalance());
            }
        }
        System.out.println("Total accounts: " + accountCount);

        System.out.println("\n--- TRANSACTIONS ---");
        int transactionCount = 0;
        for (Customer customer : customers) {
            List<Account> accounts = getAccountsByCustomerId(customer.getCustomerId());
            for (Account account : accounts) {
                List<Transaction> transactions = getTransactionsByAccountNumber(account.getAccountNumber());
                for (Transaction transaction : transactions) {
                    transactionCount++;
                    System.out.printf("%d. TXN: %s, Account: %s, Type: %s, Amount: P%.2f%n",
                            transactionCount, transaction.getTransactionId(),
                            account.getAccountNumber(), transaction.getType(), transaction.getAmount());
                }
            }
        }
        System.out.println("Total transactions: " + transactionCount);
    }
}