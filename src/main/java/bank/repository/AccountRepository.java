package bank.repository;

import bank.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    public void saveAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, branch, balance, company_name, company_address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getOwner().getCustomerId());
            stmt.setString(3, account.getAccountType());
            stmt.setString(4, account.getBranch());
            stmt.setDouble(5, account.getBalance());

            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                stmt.setString(6, chequeAccount.getCompanyName());
                stmt.setString(7, chequeAccount.getCompanyAddress());
            } else {
                stmt.setString(6, null);
                stmt.setString(7, null);
            }

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Account saved successfully: " + account.getAccountNumber() + " for customer: " + account.getOwner().getUsername());
                System.out.println("📊 Account details - Type: " + account.getAccountType() + ", Balance: P " + account.getBalance() + ", Branch: " + account.getBranch());
            } else {
                System.err.println("❌ Failed to save account: " + account.getAccountNumber());
                throw new RuntimeException("Failed to save account to database");
            }

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in saveAccount: " + e.getMessage());
            System.err.println("❌ SQL State: " + e.getSQLState());
            System.err.println("❌ Error Code: " + e.getErrorCode());
            throw new RuntimeException("Database error while saving account: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public Account findAccountByNumber(String accountNumber) {
        String sql = "SELECT a.*, c.* FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE a.account_number = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccount(rs);
            }
            System.out.println("ℹ️ No account found with number: " + accountNumber);
            return null;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in findAccountByNumber: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<Account> getAccountsByCustomer(String customerId) {
        String sql = "SELECT a.*, c.* FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE a.customer_id = ?";
        List<Account> accounts = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                accounts.add(account);
                System.out.println("✅ Loaded account: " + account.getAccountNumber() + " - " + account.getAccountType() + " - P " + account.getBalance());
            }

            System.out.println("📊 Total accounts loaded for customer " + customerId + ": " + accounts.size());
            return accounts;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getAccountsByCustomer: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public List<Account> getAllAccounts() {
        String sql = "SELECT a.*, c.* FROM accounts a JOIN customers c ON a.customer_id = c.customer_id";
        List<Account> accounts = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
            return accounts;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getAllAccounts: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }


    public String generateAccountNumber() {
        String sql = "SELECT MAX(CAST(SUBSTRING(account_number, 4) AS UNSIGNED)) FROM accounts WHERE account_number LIKE 'ACC%'";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            int nextNumber = 1000001;

            if (rs.next()) {
                int maxNumber = rs.getInt(1);
                if (!rs.wasNull() && maxNumber >= 1000000) {
                    nextNumber = maxNumber + 1;
                }
            }

            String accountNumber = "ACC" + nextNumber;
            System.out.println("🔢 Generated account number: " + accountNumber);
            return accountNumber;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in generateAccountNumber: " + e.getMessage());

            String fallbackNumber = "ACC" + System.currentTimeMillis();
            System.out.println("⚠️ Using fallback account number: " + fallbackNumber);
            return fallbackNumber;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    public void updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Updated balance for account " + accountNumber + " to P " + newBalance);
            } else {
                System.err.println("❌ Failed to update balance for account: " + accountNumber);
            }

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in updateAccountBalance: " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {

        Customer customer = createCustomerFromResultSet(rs);

        String accountType = rs.getString("account_type");
        String accountNumber = rs.getString("account_number");
        String branch = rs.getString("branch");
        double balance = rs.getDouble("balance");

        Account account = switch (accountType) {
            case "SAVINGS" -> new SavingsAccount(accountNumber, branch, customer, balance);
            case "INVESTMENT" -> new InvestmentAccount(accountNumber, branch, customer, balance);
            case "CHEQUE" -> new ChequeAccount(accountNumber, branch, customer, balance,
                    rs.getString("company_name"), rs.getString("company_address"));
            default -> throw new IllegalArgumentException("Unknown account type: " + accountType);
        };


        loadTransactionsForAccount(account);
        return account;
    }

    private Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        String customerType = rs.getString("customer_type");
        String customerId = rs.getString("customer_id");
        String firstName = rs.getString("first_name");
        String surname = rs.getString("surname");
        String address = rs.getString("address");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");

        if ("INDIVIDUAL".equals(customerType)) {
            return new IndividualCustomer(
                    customerId, firstName, surname, address,
                    username, password, email,
                    rs.getString("id_number"),
                    rs.getString("occupation"),
                    rs.getString("employment_status")
            );
        } else if ("COMPANY".equals(customerType)) {
            return new CompanyCustomer(
                    customerId, firstName, surname, address,
                    username, password, email,
                    rs.getString("registration_number"),
                    rs.getString("business_type"),
                    rs.getString("contact_person"),
                    rs.getString("company_size")
            );
        } else {

            System.err.println("⚠️ Unknown customer type: " + customerType + ", creating as Individual");
            return new IndividualCustomer(
                    customerId, firstName, surname, address,
                    username, password, email,
                    "", "", ""
            );
        }
    }

    private void loadTransactionsForAccount(Account account) {
        try {
            TransactionRepository transactionRepo = new TransactionRepository();
            List<Transaction> transactions = transactionRepo.getTransactionsByAccount(account.getAccountNumber());
            account.setTransactions(transactions);
            System.out.println("✅ Loaded " + transactions.size() + " transactions for account " + account.getAccountNumber());
        } catch (Exception e) {
            System.err.println("❌ Error loading transactions for account " + account.getAccountNumber() + ": " + e.getMessage());
        }
    }
}
