package bank.repository;

import bank.model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    public void saveTransaction(String accountNumber, Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_id, account_number, type, amount, balance_after, description, transaction_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, accountNumber);
            stmt.setString(3, transaction.getType());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setDouble(5, transaction.getBalanceAfter());
            stmt.setString(6, transaction.getDescription());
            stmt.setTimestamp(7, Timestamp.valueOf(transaction.getDate()));

            stmt.executeUpdate();
            System.out.println("✅ Transaction saved: " + transaction.getTransactionId() + " for account: " + accountNumber);

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in saveTransaction: " + e.getMessage());
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

    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
            System.out.println("✅ Loaded " + transactions.size() + " transactions for account: " + accountNumber);
            return transactions;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getTransactionsByAccount: " + e.getMessage());
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

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        double amount = rs.getDouble("amount");
        double balanceAfter = rs.getDouble("balance_after");
        String description = rs.getString("description");


        Transaction transaction = new Transaction(type, amount, balanceAfter, description);


        return new DatabaseTransaction(
                rs.getString("transaction_id"),
                transaction,
                rs.getTimestamp("transaction_date").toLocalDateTime()
        );
    }


    private static class DatabaseTransaction extends Transaction {
        private final String dbTransactionId;
        private final java.time.LocalDateTime dbDate;

        public DatabaseTransaction(String transactionId, Transaction transaction, java.time.LocalDateTime date) {
            super(transaction.getType(), transaction.getAmount(), transaction.getBalanceAfter(), transaction.getDescription());
            this.dbTransactionId = transactionId;
            this.dbDate = date;
        }

        @Override
        public String getTransactionId() {
            return dbTransactionId;
        }

        @Override
        public java.time.LocalDateTime getDate() {
            return dbDate;
        }
    }
}