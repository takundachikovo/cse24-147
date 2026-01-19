package bank.repository;

import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.CompanyCustomer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {

    public Customer saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, first_name, surname, address, username, password, email, " +
                "customer_type, id_number, occupation, employment_status, registration_number, business_type, " +
                "contact_person, company_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            String customerId = generateCustomerId();

            stmt.setString(1, customerId);
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getSurname());
            stmt.setString(4, customer.getAddress());
            stmt.setString(5, customer.getUsername());
            stmt.setString(6, customer.getPassword());
            stmt.setString(7, customer.getEmail());
            stmt.setString(8, customer.getCustomerType());

            if (customer instanceof IndividualCustomer) {
                IndividualCustomer individual = (IndividualCustomer) customer;
                stmt.setString(9, individual.getIdNumber());
                stmt.setString(10, individual.getOccupation());
                stmt.setString(11, individual.getEmploymentStatus());
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.setString(15, null);
            } else if (customer instanceof CompanyCustomer) {
                CompanyCustomer company = (CompanyCustomer) customer;
                stmt.setString(9, null);
                stmt.setString(10, null);
                stmt.setString(11, null);
                stmt.setString(12, company.getRegistrationNumber());
                stmt.setString(13, company.getBusinessType());
                stmt.setString(14, company.getContactPerson());
                stmt.setString(15, company.getCompanySize());
            } else {
                stmt.setString(9, null);
                stmt.setString(10, null);
                stmt.setString(11, null);
                stmt.setString(12, null);
                stmt.setString(13, null);
                stmt.setString(14, null);
                stmt.setString(15, null);
            }

            stmt.executeUpdate();


            Customer savedCustomer;
            if (customer instanceof IndividualCustomer) {
                IndividualCustomer individual = (IndividualCustomer) customer;
                savedCustomer = new IndividualCustomer(customerId, individual.getFirstName(), individual.getSurname(),
                        individual.getAddress(), individual.getUsername(), individual.getPassword(),
                        individual.getEmail(), individual.getIdNumber(), individual.getOccupation(),
                        individual.getEmploymentStatus());
            } else if (customer instanceof CompanyCustomer) {
                CompanyCustomer company = (CompanyCustomer) customer;
                savedCustomer = new CompanyCustomer(customerId, company.getFirstName(), company.getSurname(),
                        company.getAddress(), company.getUsername(), company.getPassword(),
                        company.getEmail(), company.getRegistrationNumber(), company.getBusinessType(),
                        company.getContactPerson(), company.getCompanySize());
            } else {
                savedCustomer = new IndividualCustomer(customerId, customer.getFirstName(), customer.getSurname(),
                        customer.getAddress(), customer.getUsername(), customer.getPassword(),
                        customer.getEmail(), "", "", "");
            }

            System.out.println("✅ Customer saved: " + customerId + " - " + customer.getUsername() + " - " + customer.getCustomerType());
            return savedCustomer;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in saveCustomer: " + e.getMessage());
            System.err.println("❌ SQL State: " + e.getSQLState());
            System.err.println("❌ Error Code: " + e.getErrorCode());

            if (e.getMessage().contains("Column \"CUSTOMER_TYPE\" not found")) {
                throw new RuntimeException("Database schema error. Please restart the application to fix this issue.", e);
            }
            if (e.getMessage().contains("PRIMARY_KEY") || e.getMessage().contains("23505")) {
                throw new RuntimeException("Customer ID conflict. Please try registering again.", e);
            }

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

    private String generateCustomerId() {
        String sql = "SELECT MAX(CAST(SUBSTRING(customer_id, 2) AS INT)) FROM customers WHERE customer_id LIKE 'C%'";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                int maxId = rs.getInt(1);
                if (rs.wasNull()) {
                    return "C2000";
                } else {
                    return "C" + (maxId + 1);
                }
            }
            return "C2000";

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in generateCustomerId: " + e.getMessage());
            return "C" + System.currentTimeMillis();
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

    public Customer findCustomerByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in findCustomerByUsername: " + e.getMessage());
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

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        String customerType = rs.getString("customer_type");
        String customerId = rs.getString("customer_id");
        String firstName = rs.getString("first_name");
        String surname = rs.getString("surname");
        String address = rs.getString("address");
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");

        if ("INDIVIDUAL".equals(customerType)) {
            return new IndividualCustomer(customerId, firstName, surname, address, username, password, email,
                    rs.getString("id_number"), rs.getString("occupation"), rs.getString("employment_status"));
        } else if ("COMPANY".equals(customerType)) {
            return new CompanyCustomer(customerId, firstName, surname, address, username, password, email,
                    rs.getString("registration_number"), rs.getString("business_type"),
                    rs.getString("contact_person"), rs.getString("company_size"));
        } else {
            return new IndividualCustomer(customerId, firstName, surname, address, username, password, email,
                    "", "", "");
        }
    }

    public Customer findCustomerById(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
            return null;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in findCustomerById: " + e.getMessage());
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

    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            return customers;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in getAllCustomers: " + e.getMessage());
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

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM customers WHERE username = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error in usernameExists: " + e.getMessage());
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
}