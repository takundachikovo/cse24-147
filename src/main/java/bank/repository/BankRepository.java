package bank.repository;

import bank.model.Bank;
import java.sql.*;

public class BankRepository {

    public Bank getBank() {
        String sql = "SELECT * FROM bank WHERE bank_id = 'BANK001'";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                return new Bank(
                        rs.getString("bank_id"),
                        rs.getString("bank_name"),
                        rs.getString("headquarters"),
                        rs.getString("contact_number"),
                        rs.getString("email")
                );
            }


            return new Bank();

        } catch (SQLException e) {
            System.err.println("❌ Error getting bank info: " + e.getMessage());

            return new Bank();
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

    public void updateBank(Bank bank) {
        String sql = "UPDATE bank SET bank_name = ?, headquarters = ?, contact_number = ?, email = ? WHERE bank_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, bank.getBankName());
            stmt.setString(2, bank.getHeadquarters());
            stmt.setString(3, bank.getContactNumber());
            stmt.setString(4, bank.getEmail());
            stmt.setString(5, bank.getBankId());

            stmt.executeUpdate();
            System.out.println("✅ Bank information updated successfully");

        } catch (SQLException e) {
            System.err.println("❌ Error updating bank info: " + e.getMessage());
            throw new RuntimeException("Failed to update bank information", e);
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
}
