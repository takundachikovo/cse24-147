package com.bank.view;

import com.bank.controller.AccountController;
import com.bank.controller.LoginController;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionHistoryView extends BaseView {
    private LoginController loginController;
    private AccountController accountController;
    private Stage primaryStage;
    private Customer currentCustomer;
    private TableView<Transaction> transactionTable;

    public TransactionHistoryView(Stage primaryStage, LoginController loginController, AccountController accountController) {
        super();
        this.primaryStage = primaryStage;
        this.loginController = loginController;
        this.accountController = accountController;
        this.currentCustomer = loginController.getCurrentCustomer();
        initializeView();
    }

    private void initializeView() {
        view.setPadding(new Insets(20));

        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Label bankLabel = new Label("Bank");
        bankLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label breadcrumbLabel = new Label("Dashboard > Transaction History");
        breadcrumbLabel.setStyle("-fx-text-fill: #4a5568;");

        headerBox.getChildren().addAll(bankLabel, spacer, breadcrumbLabel);
        view.getChildren().add(headerBox);


        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        Label subtitleLabel = new Label("View your account transactions and activity");
        subtitleLabel.setStyle("-fx-text-fill: #4a5568;");

        
        VBox selectionBox = new VBox(10);
        Label accountLabel = new Label("Select Account");
        accountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.setPrefWidth(300);
        accountComboBox.setStyle("-fx-pref-height: 40px;");

        
        for (Account account : currentCustomer.getAccounts()) {
            accountComboBox.getItems().add(account.getAccountNumber() + " - " + account.getAccountType() + " - BWP " + account.getBalance());
        }

        if (!currentCustomer.getAccounts().isEmpty()) {
            accountComboBox.setValue(accountComboBox.getItems().get(0));
        }

        selectionBox.getChildren().addAll(accountLabel, accountComboBox);

        
        VBox tableBox = new VBox(10);
        Label tableLabel = new Label("Recent Transactions");
        tableLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        transactionTable = createTransactionsTable();
        tableBox.getChildren().addAll(tableLabel, transactionTable);


        HBox buttonsBox = new HBox();
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        buttonsBox.setPadding(new Insets(20, 0, 0, 0));

        javafx.scene.control.Button backButton = createSecondaryButton("Back to Dashboard");
        buttonsBox.getChildren().add(backButton);

        contentBox.getChildren().addAll(titleLabel, subtitleLabel, selectionBox, tableBox, buttonsBox);
        view.getChildren().add(contentBox);

        
        accountComboBox.setOnAction(e -> {
            int selectedIndex = accountComboBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Account selectedAccount = currentCustomer.getAccounts().get(selectedIndex);
                loadTransactions(selectedAccount);
            }
        });

        backButton.setOnAction(e -> {
            DashboardView dashboardView = new DashboardView(primaryStage, loginController);
            primaryStage.getScene().setRoot(dashboardView.getView());
        });

        // Load initial transactions if accounts exist
        if (!currentCustomer.getAccounts().isEmpty()) {
            loadTransactions(currentCustomer.getAccounts().get(0));
        }
    }

    private TableView<Transaction> createTransactionsTable() {
        TableView<Transaction> table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0;");
        table.setPrefHeight(400);


        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(150);
        dateColumn.setStyle("-fx-alignment: CENTER_LEFT;");

        
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setPrefWidth(100);
        typeColumn.setStyle("-fx-alignment: CENTER;");

        
        TableColumn<Transaction, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setPrefWidth(120);
        amountColumn.setStyle("-fx-alignment: CENTER_RIGHT;");

        
        TableColumn<Transaction, String> balanceColumn = new TableColumn<>("Balance After");
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));
        balanceColumn.setPrefWidth(120);
        balanceColumn.setStyle("-fx-alignment: CENTER_RIGHT;");

        table.getColumns().addAll(dateColumn, typeColumn, amountColumn, balanceColumn);

        return table;
    }

    private void loadTransactions(Account account) {
        transactionTable.getItems().clear();


        List<Transaction> transactions = accountController.getAccountTransactions(account.getAccountNumber());


        if (transactions.isEmpty()) {
            
            transactions.add(new Transaction(1000.0, "DEPOSIT", account));
            transactions.add(new Transaction(500.0, "WITHDRAWAL", account));
            transactions.add(new Transaction(50.0, "INTEREST", account));
            transactions.add(new Transaction(200.0, "DEPOSIT", account));
        }

        transactionTable.getItems().addAll(transactions);
    }

}
