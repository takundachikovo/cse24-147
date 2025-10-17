
package com.bank.view;

import com.bank.controller.AccountController;
import com.bank.controller.LoginController;
import com.bank.model.Account;
import com.bank.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DepositView extends BaseView {
    private LoginController loginController;
    private AccountController accountController;
    private Stage primaryStage;
    private Customer currentCustomer;
    private Account selectedAccount;

    public DepositView(Stage primaryStage, LoginController loginController, AccountController accountController, Account account) {
        super();
        this.primaryStage = primaryStage;
        this.loginController = loginController;
        this.accountController = accountController;
        this.currentCustomer = loginController.getCurrentCustomer();
        this.selectedAccount = account;
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

        Label breadcrumbLabel = new Label("Dashboard > Make Deposit");
        breadcrumbLabel.setStyle("-fx-text-fill: #4a5568;");

        headerBox.getChildren().addAll(bankLabel, spacer, breadcrumbLabel);
        view.getChildren().add(headerBox);

        
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label("Deposit Funds");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        Label subtitleLabel = new Label("Add money to your account");
        subtitleLabel.setStyle("-fx-text-fill: #4a5568;");

        
        VBox formBox = new VBox(15);

        
        Label accountLabel = new Label("Select Account");
        accountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        ComboBox<String> accountComboBox = new ComboBox<>();
        accountComboBox.setPrefWidth(300);
        accountComboBox.setStyle("-fx-pref-height: 40px;");

        for (Account account : currentCustomer.getAccounts()) {
            accountComboBox.getItems().add(account.getAccountNumber() + " - " + account.getAccountType() + " - BWP " + account.getBalance());
        }

        
        if (selectedAccount != null) {
            String selectedValue = selectedAccount.getAccountNumber() + " - " + selectedAccount.getAccountType() + " - BWP " + selectedAccount.getBalance();
            accountComboBox.setValue(selectedValue);
        }

        
        Label balanceLabel = new Label();
        updateBalanceDisplay(balanceLabel, selectedAccount);

        
        Label amountLabel = new Label("Deposit Amount");
        amountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        javafx.scene.control.TextField amountField = createTextField("Enter amount");

        
        VBox summaryBox = new VBox(10);
        summaryBox.setPadding(new Insets(15));
        summaryBox.setStyle("-fx-background-color: #f0fff4;" +
                "-fx-background-radius: 5px;" +
                "-fx-border-radius: 5px;" +
                "-fx-border-color: #9ae6b4;");

        Label summaryTitle = new Label("Transaction Summary");
        summaryTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #276749;");

        Label summaryDetails = new Label("Select an account and enter amount to see summary");
        summaryDetails.setStyle("-fx-text-fill: #276749; -fx-font-size: 12px;");

        summaryBox.getChildren().addAll(summaryTitle, summaryDetails);
        summaryBox.setVisible(false);

        
        HBox buttonsBox = new HBox(15);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.control.Button depositBtn = createPrimaryButton("Confirm Deposit");
        javafx.scene.control.Button cancelBtn = createSecondaryButton("Cancel");

        buttonsBox.getChildren().addAll(depositBtn, cancelBtn);

        formBox.getChildren().addAll(
                accountLabel, accountComboBox, balanceLabel,
                amountLabel, amountField, summaryBox, buttonsBox
        );

        contentBox.getChildren().addAll(titleLabel, subtitleLabel, formBox);
        view.getChildren().add(contentBox);

        
        accountComboBox.setOnAction(e -> {
            int selectedIndex = accountComboBox.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                selectedAccount = currentCustomer.getAccounts().get(selectedIndex);
                updateBalanceDisplay(balanceLabel, selectedAccount);
                updateSummary(summaryBox, selectedAccount, amountField.getText());
            }
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSummary(summaryBox, selectedAccount, newValue);
        });

        depositBtn.setOnAction(e -> handleDeposit(amountField.getText()));

        cancelBtn.setOnAction(e -> {
            DashboardView dashboardView = new DashboardView(primaryStage, loginController);
            primaryStage.getScene().setRoot(dashboardView.getView());
        });
    }

    private void updateBalanceDisplay(Label balanceLabel, Account account) {
        if (account != null) {
            balanceLabel.setText("Current Balance: BWP " + String.format("%.2f", account.getBalance()));
            balanceLabel.setStyle("-fx-text-fill: #3182ce; -fx-font-weight: bold;");
        } else {
            balanceLabel.setText("Please select an account");
            balanceLabel.setStyle("-fx-text-fill: #a0aec0;");
        }
    }

    private void updateSummary(VBox summaryBox, Account account, String amountStr) {
        if (account == null || amountStr.isEmpty()) {
            summaryBox.setVisible(false);
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            double newBalance = account.getBalance() + amount;

            Label summaryTitle = (Label) summaryBox.getChildren().get(0);
            Label summaryDetails = (Label) summaryBox.getChildren().get(1);

            summaryDetails.setText(String.format(
                    "Account: %s\nDeposit Amount: BWP %.2f\nNew Balance: BWP %.2f",
                    account.getAccountType(), amount, newBalance
            ));

            summaryBox.setVisible(true);
        } catch (NumberFormatException e) {
            summaryBox.setVisible(false);
        }
    }

    private void handleDeposit(String amountStr) {
        if (selectedAccount == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an account.");
            return;
        }

        if (amountStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter deposit amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a positive amount.");
                return;
            }

            boolean success = accountController.deposit(selectedAccount, amount);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        String.format("Deposit of BWP %.2f completed successfully!", amount));
                DashboardView dashboardView = new DashboardView(primaryStage, loginController);
                primaryStage.getScene().setRoot(dashboardView.getView());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Deposit failed. Please try again.");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid amount.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}