package bank.repository;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:file:./bank_database/bank_system;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static boolean initialized = false;

    static {
        initializeDatabase();
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
        return connection;
    }

    private static void initializeDatabase() {
        if (initialized) return;

        try {
            Class.forName("org.h2.Driver");
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                createTables(conn);
                System.out.println("✅ H2 file-based database initialized successfully");
                System.out.println("📁 Database location: ./bank_database/bank_system.mv.db");
                System.out.println("🔗 Database URL: " + URL);
                initialized = true;
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) throws SQLException {

        String createCustomersTable = """
            CREATE TABLE IF NOT EXISTS customers (
                customer_id VARCHAR(50) PRIMARY KEY,
                first_name VARCHAR(100) NOT NULL,
                surname VARCHAR(100) NOT NULL,
                address VARCHAR(1000),
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(200) NOT NULL,
                email VARCHAR(200),
                customer_type VARCHAR(20) NOT NULL,
                -- Individual customer fields
                id_number VARCHAR(50),
                occupation VARCHAR(100),
                employment_status VARCHAR(100),
                -- Company customer fields
                registration_number VARCHAR(100),
                business_type VARCHAR(100),
                contact_person VARCHAR(200),
                company_size VARCHAR(50),
                
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;


        String createBankTable = """
            CREATE TABLE IF NOT EXISTS bank (
                bank_id VARCHAR(50) PRIMARY KEY,
                bank_name VARCHAR(200) NOT NULL,
                headquarters VARCHAR(500),
                contact_number VARCHAR(50),
                email VARCHAR(200),
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;


        String createAccountsTable = """
            CREATE TABLE IF NOT EXISTS accounts (
                account_number VARCHAR(50) PRIMARY KEY,
                customer_id VARCHAR(50) NOT NULL,
                account_type VARCHAR(50) NOT NULL,
                branch VARCHAR(100) NOT NULL,
                balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
                company_name VARCHAR(200),
                company_address VARCHAR(1000),
                
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
            )
            """;


        String createTransactionsTable = """
            CREATE TABLE IF NOT EXISTS transactions (
                transaction_id VARCHAR(50) PRIMARY KEY,
                account_number VARCHAR(50) NOT NULL,
                type VARCHAR(50) NOT NULL,
                amount DECIMAL(15,2) NOT NULL,
                balance_after DECIMAL(15,2) NOT NULL,
                description VARCHAR(1000),
                transaction_date TIMESTAMP NOT NULL,
                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
            )
            """;

        try (Statement stmt = conn.createStatement()) {

            stmt.execute(createCustomersTable);
            stmt.execute(createBankTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createTransactionsTable);


            String createIndexes = """
                CREATE INDEX IF NOT EXISTS idx_customers_username ON customers(username);
                CREATE INDEX IF NOT EXISTS idx_customers_type ON customers(customer_type);
                CREATE INDEX IF NOT EXISTS idx_accounts_customer ON accounts(customer_id);
                CREATE INDEX IF NOT EXISTS idx_accounts_type ON accounts(account_type);
                CREATE INDEX IF NOT EXISTS idx_transactions_account ON transactions(account_number);
                CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);
                """;
            stmt.execute(createIndexes);

            System.out.println("✅ All database tables created successfully");
            insertSampleData(conn);
        }
    }

    private static void insertSampleData(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM customers");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("📝 Inserting sample data with 10+ records...");


                String insertBankData = """
                    INSERT INTO bank (bank_id, bank_name, headquarters, contact_number, email) VALUES
                    ('BANK001', 'Botswana National Bank', 'Gaborone, Botswana', '+267 123 4567', 'info@botswanabank.bw')
                    """;
                stmt.execute(insertBankData);


                String insertIndividualCustomers = """
                    INSERT INTO customers (customer_id, first_name, surname, address, username, password, email, customer_type, id_number, occupation, employment_status) VALUES
                    ('C2001', 'John', 'Simasiku', '123 Main St, Gaborone', 'johnsmith', 'password123', 'john.smith@email.com', 'INDIVIDUAL', '19901234567', 'Student', 'Full-time'),
                    ('C2002', 'Mary', 'Molapo', '456 Broad St, Francistown', 'marym', 'password123', 'mary.molapo@email.com', 'INDIVIDUAL', '19852345678', 'Employed', 'Full-time'),
                    ('C2003', 'David', 'Kgosiemang', '789 River Rd, Maun', 'davidk', 'password123', 'david.kgosiemang@email.com', 'INDIVIDUAL', '19923456789', 'Self-Employed', 'Contract'),
                    ('C2004', 'Sarah', 'Mogapi', '321 Hill View, Palapye', 'sarahm', 'password123', 'sarah.mogapi@email.com', 'INDIVIDUAL', '19884567890', 'Employed', 'Part-time'),
                    ('C2005', 'Peter', 'Ditlhong', '654 Valley Rd, Serowe', 'peterd', 'password123', 'peter.ditlhong@email.com', 'INDIVIDUAL', '19955678901', 'Other', 'Unemployed'),
                    ('C2006', 'Anna', 'Seboni', '987 Park Ave, Gaborone', 'annas', 'password123', 'anna.seboni@email.com', 'INDIVIDUAL', '19866789012', 'Retired', 'Full-time')
                    """;
                stmt.execute(insertIndividualCustomers);


                String insertCompanyCustomers = """
                    INSERT INTO customers (customer_id, first_name, surname, address, username, password, email, customer_type, registration_number, business_type, contact_person, company_size) VALUES
                    ('C2007', 'Tech', 'Solutions Ltd', '321 Innovation Dr, Gaborone', 'techsolutions', 'password123', 'info@techsolutions.bw', 'COMPANY', 'CORP-2023-001', 'Technology', 'Sarah Johnson', 'Medium (51-200)'),
                    ('C2008', 'Green', 'Farms Co', '654 Agriculture Ave, Palapye', 'greenfarms', 'password123', 'contact@greenfarms.bw', 'COMPANY', 'CORP-2022-045', 'Retail', 'Michael Chen', 'Small (1-50)'),
                    ('C2009', 'BuildRight', 'Construction', '789 Site Rd, Francistown', 'buildright', 'password123', 'admin@buildright.bw', 'COMPANY', 'CORP-2021-078', 'Manufacturing', 'James Brown', 'Large (201-1000)'),
                    ('C2010', 'MediCare', 'Clinics', '123 Health St, Gaborone', 'medicare', 'password123', 'info@medicare.bw', 'COMPANY', 'CORP-2020-123', 'Services', 'Dr. Susan Lee', 'Enterprise (1000+)')
                    """;
                stmt.execute(insertCompanyCustomers);


                String insertAccounts = """
                    INSERT INTO accounts (account_number, customer_id, account_type, branch, balance, company_name, company_address) VALUES
                    -- Individual customer accounts
                    ('ACC1000001', 'C2001', 'SAVINGS', 'Main Branch', 5000.00, NULL, NULL),
                    ('ACC1000002', 'C2001', 'CHEQUE', 'Main Branch', 2500.00, 'Tech Solutions Ltd', '321 Innovation Dr, Gaborone'),
                    ('ACC1000003', 'C2002', 'SAVINGS', 'Francistown Branch', 3000.00, NULL, NULL),
                    ('ACC1000004', 'C2002', 'INVESTMENT', 'Francistown Branch', 1500.00, NULL, NULL),
                    ('ACC1000005', 'C2003', 'SAVINGS', 'Maun Branch', 7500.00, NULL, NULL),
                    ('ACC1000006', 'C2004', 'INVESTMENT', 'Palapye Branch', 2000.00, NULL, NULL),
                    ('ACC1000007', 'C2005', 'CHEQUE', 'Serowe Branch', 3500.00, 'BuildRight Construction', '789 Site Rd, Francistown'),
                    ('ACC1000008', 'C2006', 'SAVINGS', 'Gaborone Branch', 4200.00, NULL, NULL),
                    -- Company customer accounts
                    ('ACC1000009', 'C2007', 'CHEQUE', 'Main Branch', 25000.00, 'Tech Solutions Ltd', '321 Innovation Dr, Gaborone'),
                    ('ACC1000010', 'C2008', 'INVESTMENT', 'Palapye Branch', 50000.00, 'Green Farms Co', '654 Agriculture Ave, Palapye'),
                    ('ACC1000011', 'C2009', 'CHEQUE', 'Francistown Branch', 18000.00, 'BuildRight Construction', '789 Site Rd, Francistown'),
                    ('ACC1000012', 'C2010', 'INVESTMENT', 'Main Branch', 35000.00, 'MediCare Clinics', '123 Health St, Gaborone')
                    """;
                stmt.execute(insertAccounts);


                String insertTransactions = """
                    INSERT INTO transactions (transaction_id, account_number, type, amount, balance_after, description, transaction_date) VALUES
                    ('TXN1001', 'ACC1000001', 'DEPOSIT', 5000.00, 5000.00, 'Initial deposit', '2025-01-15 10:30:00'),
                    ('TXN1002', 'ACC1000002', 'DEPOSIT', 2500.00, 2500.00, 'Salary deposit', '2025-01-20 09:15:00'),
                    ('TXN1003', 'ACC1000003', 'DEPOSIT', 3000.00, 3000.00, 'Savings deposit', '2025-01-25 14:20:00'),
                    ('TXN1004', 'ACC1000004', 'DEPOSIT', 1500.00, 1500.00, 'Investment deposit', '2025-02-01 11:45:00'),
                    ('TXN1005', 'ACC1000005', 'DEPOSIT', 7500.00, 7500.00, 'Initial deposit', '2025-02-05 16:30:00'),
                    ('TXN1006', 'ACC1000006', 'DEPOSIT', 2000.00, 2000.00, 'Investment deposit', '2025-02-10 13:10:00'),
                    ('TXN1007', 'ACC1000007', 'DEPOSIT', 3500.00, 3500.00, 'Salary deposit', '2025-02-15 09:45:00'),
                    ('TXN1008', 'ACC1000008', 'DEPOSIT', 4200.00, 4200.00, 'Savings deposit', '2025-02-20 11:20:00'),
                    ('TXN1009', 'ACC1000009', 'DEPOSIT', 25000.00, 25000.00, 'Business deposit', '2025-02-25 15:30:00'),
                    ('TXN1010', 'ACC1000010', 'DEPOSIT', 50000.00, 50000.00, 'Corporate investment', '2025-03-01 10:15:00'),
                    ('TXN1011', 'ACC1000011', 'DEPOSIT', 18000.00, 18000.00, 'Business deposit', '2025-03-05 14:45:00'),
                    ('TXN1012', 'ACC1000012', 'DEPOSIT', 35000.00, 35000.00, 'Corporate investment', '2025-03-10 16:20:00')
                    """;
                stmt.execute(insertTransactions);

                System.out.println("✅ All sample data inserted successfully!");
                System.out.println("📊 SUMMARY:");
                System.out.println("   🏦 1 Bank record");
                System.out.println("   👤 10 Customers (6 Individual + 4 Company)");
                System.out.println("   💳 12 Accounts");
                System.out.println("   📈 12 Transactions");
            } else {
                System.out.println("ℹ️ Sample data already exists, skipping insertion");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error inserting sample data: " + e.getMessage());
        }
    }

    public static void checkDatabaseStatus() {
        try {
            java.io.File dbFile = new java.io.File("./bank_database/bank_system.mv.db");
            System.out.println("=== DATABASE STATUS ===");
            System.out.println("Database file exists: " + dbFile.exists());
            System.out.println("Database file path: " + dbFile.getAbsolutePath());
            System.out.println("Database file size: " + (dbFile.exists() ? dbFile.length() + " bytes" : "N/A"));

            if (dbFile.exists()) {
                try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM bank");
                    if (rs.next()) System.out.println("Total banks: " + rs.getInt(1));
                    rs = stmt.executeQuery("SELECT COUNT(*) FROM customers");
                    if (rs.next()) System.out.println("Total customers: " + rs.getInt(1));
                    rs = stmt.executeQuery("SELECT COUNT(*) FROM accounts");
                    if (rs.next()) System.out.println("Total accounts: " + rs.getInt(1));
                    rs = stmt.executeQuery("SELECT COUNT(*) FROM transactions");
                    if (rs.next()) System.out.println("Total transactions: " + rs.getInt(1));
                }
            }
            System.out.println("======================");
        } catch (Exception e) {
            System.err.println("Error checking database status: " + e.getMessage());
        }
    }

    public static void displayDatabaseStats() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            System.out.println("\n=== DATABASE STATISTICS ===");
            ResultSet rs = stmt.executeQuery("SELECT bank_name, headquarters FROM bank WHERE bank_id = 'BANK001'");
            if (rs.next()) {
                System.out.println("🏦 Bank: " + rs.getString("bank_name") + " - " + rs.getString("headquarters"));
            }
            rs = stmt.executeQuery("SELECT COUNT(*) as count, customer_type FROM customers GROUP BY customer_type");
            while (rs.next()) {
                System.out.println("👥 " + rs.getString("customer_type") + " Customers: " + rs.getInt("count"));
            }
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM accounts");
            if (rs.next()) System.out.println("💳 Total Accounts: " + rs.getInt("count"));
            rs = stmt.executeQuery("SELECT COUNT(*) as count FROM transactions");
            if (rs.next()) System.out.println("📊 Total Transactions: " + rs.getInt("count"));
            rs = stmt.executeQuery("SELECT SUM(balance) as total_balance FROM accounts");
            if (rs.next()) {
                System.out.println("💰 Total Bank Balance: P " + String.format("%.2f", rs.getDouble("total_balance")));
            }
            System.out.println("==========================\n");
        } catch (SQLException e) {
            System.err.println("❌ Error getting database statistics: " + e.getMessage());
        }
    }

    public static void resetDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS transactions");
            stmt.execute("DROP TABLE IF EXISTS accounts");
            stmt.execute("DROP TABLE IF EXISTS customers");
            stmt.execute("DROP TABLE IF EXISTS bank");
            initialized = false;
            initializeDatabase();
            System.out.println("✅ Database reset successfully");
        } catch (SQLException e) {
            System.err.println("❌ Error resetting database: " + e.getMessage());
        }
    }
}