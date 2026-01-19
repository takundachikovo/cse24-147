package bank;

import bank.controller.*;
import bank.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankApp extends Application {
    private Stage primaryStage;


    private LoginController loginController;
    private CustomerController customerController;
    private AccountController accountController;
    private TransactionController transactionController;
    private DashboardController dashboardController;


    private LoginView loginView;
    private CustomerRegistrationView registrationView;
    private DashboardView dashboardView;
    private OpenAccountView openAccountView;
    private DepositView depositView;
    private WithdrawalView withdrawalView;
    private TransactionHistoryView transactionHistoryView;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;



        initializeControllers();
        initializeViews();
        showLoginView();

        primaryStage.setTitle("Banking System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();


        bank.repository.DatabaseConnection.displayDatabaseStats();
        bank.repository.DatabaseConnection.checkDatabaseStatus();
    }

    private void initializeControllers() {
        loginController = new LoginController();
        customerController = new CustomerController(loginController);
        accountController = new AccountController(loginController);
        transactionController = new TransactionController(accountController);
        dashboardController = new DashboardController(loginController, accountController);
    }

    private void initializeViews() {

        loginView = new LoginView(loginController, this::showDashboardView, this::showRegistrationView);
        registrationView = new CustomerRegistrationView(loginController, accountController, this::showDashboardView, this::showLoginView);


        dashboardView = new DashboardView(dashboardController,
                this::showOpenAccountView,
                this::showDepositView,
                this::showWithdrawalView,
                this::showTransactionHistoryView,
                this::logout);


        openAccountView = new OpenAccountView(accountController,
                this::showDashboardView,
                this::showDashboardViewWithRefresh);


        depositView = new DepositView(transactionController, this::showDashboardViewWithRefresh);
        withdrawalView = new WithdrawalView(transactionController, this::showDashboardViewWithRefresh);
        transactionHistoryView = new TransactionHistoryView(accountController, this::showDashboardView);
    }

    private void showLoginView() {
        loginView.clearForm();
        primaryStage.setScene(loginView.getScene());
        primaryStage.setTitle("Bank - Login");
    }

    private void showRegistrationView() {
        registrationView.clearForm();
        primaryStage.setScene(registrationView.getScene());
        primaryStage.setTitle("Bank - Register");
    }

    private void showDashboardView() {
        primaryStage.setScene(dashboardView.getScene());
        primaryStage.setTitle("Bank - Dashboard");


        bank.repository.DatabaseConnection.displayDatabaseStats();

        dashboardView.refresh();
    }

    private void showDashboardViewWithRefresh() {
        dashboardView.refresh();
        showDashboardView();
    }

    private void showOpenAccountView() {
        openAccountView.clearForm();
        primaryStage.setScene(openAccountView.getScene());
        primaryStage.setTitle("Bank - Open Account");
    }

    private void showDepositView() {
        depositView.refresh();
        primaryStage.setScene(depositView.getScene());
        primaryStage.setTitle("Bank - Deposit");
    }

    private void showWithdrawalView() {
        withdrawalView.refresh();
        primaryStage.setScene(withdrawalView.getScene());
        primaryStage.setTitle("Bank - Withdrawal");
    }

    private void showTransactionHistoryView() {
        transactionHistoryView.refresh();
        primaryStage.setScene(transactionHistoryView.getScene());
        primaryStage.setTitle("Bank - Transaction History");
    }

    private void logout() {
        loginController.logout();
        showLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}