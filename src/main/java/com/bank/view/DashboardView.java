package com.bank.view;

import com.bank.controller.AccountController;
import com.bank.controller.LoginController;
import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class DashboardView extends BaseView {
    private LoginController loginController;
    private AccountController accountController;
    private Stage primaryStage;
    private Customer currentCustomer;

    public DashboardView(Stage primaryStage, LoginController loginController) {
        super();
        this.primaryStage = primaryStage;
        this.loginController = loginController;
        this.accountController = new AccountController();
        this.currentCustomer = loginController.getCurrentCustomer();
        initializeView();
    }

    private void initializeView() {
        view.setPadding(new Insets(20));
        view.setStyle("-fx-background-color: #f7fafc;");

        currentCustomer.setAccounts(accountController.getCustomerAccounts(currentCustomer.getCustomerId()));

        HBox headerBox = createHeader();
        view.getChildren().add(headerBox);

        VBox welcomeSection = createWelcomeSection();
        view.getChildren().add(welcomeSection);

        VBox accountsSection = createAccountsSection();
        view.getChildren().add(accountsSection);

        VBox quickActionsSection = createQuickActionsSection();
        view.getChildren().add(quickActionsSection);

        VBox recentActivitySection = createRecentActivitySection();
        view.getChildren().add(recentActivitySection);

        HBox footerBox = createFooter();
        view.getChildren().add(footerBox);
    }

    private HBox createHeader() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        Label bankLabel = new Label("Bank");
        bankLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox navBox = new HBox(20);
        navBox.setAlignment(Pos.CENTER_RIGHT);

        Hyperlink dashboardLink = new Hyperlink("Dashboard");
        Hyperlink profileLink = new Hyperlink("Profile");

        dashboardLink.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 14px;");
        profileLink.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 14px;");

        Label welcomeLabel = new Label("Welcome, " + currentCustomer.getFirstName() + "!");
        welcomeLabel.setStyle("-fx-text-fill: #1a365d; -fx-font-weight: bold;");

        Hyperlink logoutLink = new Hyperlink("Logout");
        logoutLink.setStyle("-fx-text-fill: #e53e3e;");

        navBox.getChildren().addAll(dashboardLink, profileLink, welcomeLabel, logoutLink);
        headerBox.getChildren().addAll(bankLabel, spacer, navBox);

        logoutLink.setOnAction(e -> {
            loginController.logout();
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.getScene().setRoot(loginView.getView());
        });

        return headerBox;
    }

    private VBox createWelcomeSection() {
        VBox welcomeBox = new VBox(10);
        welcomeBox.setPadding(new Insets(20));
        welcomeBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label("Your Financial Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        Label subtitleLabel = new Label("Manage your accounts and track your financial growth");
        subtitleLabel.setStyle("-fx-text-fill: #4a5568;");

        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        double totalBalance = accountController.getTotalBalance(currentCustomer.getAccounts());
        int totalAccounts = currentCustomer.getAccounts().size();

        VBox balanceBox = createStatBox("Total Balance", String.format("BWP %.2f", totalBalance));
        VBox accountsBox = createStatBox("Total Accounts", String.valueOf(totalAccounts));
        VBox interestBox = createStatBox("This Month's Interest", "BWP 0.00");

        statsBox.getChildren().addAll(balanceBox, accountsBox, interestBox);
        welcomeBox.getChildren().addAll(titleLabel, subtitleLabel, statsBox);

        return welcomeBox;
    }

    private VBox createStatBox(String title, String value) {
        VBox statBox = new VBox(5);

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #3182ce;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 12px;");

        statBox.getChildren().addAll(valueLabel, titleLabel);
        return statBox;
    }

    private VBox createAccountsSection() {
        VBox accountsBox = new VBox(15);
        accountsBox.setPadding(new Insets(20, 0, 0, 0));

        Label sectionLabel = new Label("Your Accounts");
        sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox accountsGrid = new HBox(20);
        accountsGrid.setAlignment(Pos.TOP_LEFT);

        for (Account account : currentCustomer.getAccounts()) {
            VBox accountCard = createAccountCard(account);
            accountsGrid.getChildren().add(accountCard);
        }

        if (currentCustomer.getAccounts().size() < 3) {
            VBox newAccountCard = createNewAccountCard();
            accountsGrid.getChildren().add(newAccountCard);
        }

        accountsBox.getChildren().addAll(sectionLabel, accountsGrid);
        return accountsBox;
    }

    private VBox createAccountCard(Account account) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        String badgeColor = "";
        if ("SAVINGS".equals(account.getAccountType())) {
            badgeColor = "#38a169";
        } else if ("INVESTMENT".equals(account.getAccountType())) {
            badgeColor = "#d69e2e";
        } else if ("CHEQUE".equals(account.getAccountType())) {
            badgeColor = "#3182ce";
        } else {
            badgeColor = "#4a5568";
        }

        Label typeLabel = new Label(account.getAccountType());
        typeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-background-color:" + badgeColor + "; -fx-background-radius: 15px;");

        Label balanceLabel = new Label(String.format("BWP %.2f", account.getBalance()));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        String interestRate = "";
        if ("SAVINGS".equals(account.getAccountType())) {
            interestRate = "0.05% monthly";
        } else if ("INVESTMENT".equals(account.getAccountType())) {
            interestRate = "5% monthly";
        } else if ("CHEQUE".equals(account.getAccountType())) {
            interestRate = "No interest";
        } else {
            interestRate = "N/A";
        }

        Label interestLabel = new Label("Interest: " + interestRate);
        interestLabel.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 12px;");

        HBox actionsBox = new HBox(10);

        javafx.scene.control.Button depositBtn = new javafx.scene.control.Button("Deposit");
        depositBtn.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px;");

        if (account.canWithdraw()) {
            javafx.scene.control.Button withdrawBtn = new javafx.scene.control.Button("Withdraw");
            withdrawBtn.setStyle("-fx-font-size: 12px; -fx-pref-height: 25px;");
            actionsBox.getChildren().add(withdrawBtn);

            withdrawBtn.setOnAction(e -> {
                WithdrawalView withdrawalView = new WithdrawalView(primaryStage, loginController, accountController, account);
                primaryStage.getScene().setRoot(withdrawalView.getView());
            });
        }

        actionsBox.getChildren().add(depositBtn);

        String status = "";
        if ("SAVINGS".equals(account.getAccountType())) {
            status = "No withdrawals";
        } else if ("INVESTMENT".equals(account.getAccountType())) {
            status = "Min. BWP 500";
        } else if ("CHEQUE".equals(account.getAccountType())) {
            status = "Salary account";
        }

        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 10px; -fx-font-style: italic;");

        card.getChildren().addAll(typeLabel, balanceLabel, interestLabel, actionsBox, statusLabel);

        depositBtn.setOnAction(e -> {
            DepositView depositView = new DepositView(primaryStage, loginController, accountController, account);
            primaryStage.getScene().setRoot(depositView.getView());
        });

        return card;
    }

    private VBox createNewAccountCard() {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: #cbd5e0; -fx-border-style: dashed; -fx-border-width: 2px;");

        Label plusLabel = new Label("+");
        plusLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #3182ce;");

        Label titleLabel = new Label("Open New Account");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #3182ce;");

        Label subtitleLabel = new Label("Add another account to your portfolio");
        subtitleLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px; -fx-text-alignment: center;");

        card.getChildren().addAll(plusLabel, titleLabel, subtitleLabel);

        card.setOnMouseClicked(e -> {
            OpenAccountView openAccountView = new OpenAccountView(primaryStage, loginController, accountController);
            primaryStage.getScene().setRoot(openAccountView.getView());
        });

        return card;
    }

    private VBox createQuickActionsSection() {
        VBox actionsBox = new VBox(15);
        actionsBox.setPadding(new Insets(20, 0, 0, 0));

        Label sectionLabel = new Label("Quick Banking");
        sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox buttonsBox = new HBox(15);

        javafx.scene.control.Button openAccountBtn = createActionButton("Open New Account", "#3182ce");
        javafx.scene.control.Button depositBtn = createActionButton("Make Deposit", "#38a169");
        javafx.scene.control.Button withdrawBtn = createActionButton("Make Withdrawal", "#d69e2e");
        javafx.scene.control.Button historyBtn = createActionButton("View Transactions", "#4a5568");

        buttonsBox.getChildren().addAll(openAccountBtn, depositBtn, withdrawBtn, historyBtn);
        actionsBox.getChildren().addAll(sectionLabel, buttonsBox);

        openAccountBtn.setOnAction(e -> {
            OpenAccountView openAccountView = new OpenAccountView(primaryStage, loginController, accountController);
            primaryStage.getScene().setRoot(openAccountView.getView());
        });

        depositBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Make Deposit", "Please select an account from your dashboard to make a deposit.");
        });

        withdrawBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Make Withdrawal", "Please select an account from your dashboard to make a withdrawal.");
        });

        historyBtn.setOnAction(e -> {
            TransactionHistoryView transactionView = new TransactionHistoryView(primaryStage, loginController, accountController);
            primaryStage.getScene().setRoot(transactionView.getView());
        });

        return actionsBox;
    }

    private javafx.scene.control.Button createActionButton(String text, String color) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setStyle("-fx-pref-height: 40px; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 5px;");
        return button;
    }

    private VBox createRecentActivitySection() {
        VBox activityBox = new VBox(15);
        activityBox.setPadding(new Insets(20, 0, 0, 0));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label sectionLabel = new Label("Recent Transactions");
        sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(sectionLabel, spacer);

        VBox transactionsBox = new VBox(10);

        List<Transaction> recentTransactions = getRecentTransactions();
        if (recentTransactions.isEmpty()) {
            VBox placeholder = new VBox();
            placeholder.setPrefHeight(100);
            placeholder.setStyle("-fx-background-color: white; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-alignment: center;");

            Label placeholderLabel = new Label("No recent transactions");
            placeholderLabel.setStyle("-fx-text-fill: #a0aec0;");
            placeholder.getChildren().add(placeholderLabel);
            transactionsBox.getChildren().add(placeholder);
        } else {
            for (Transaction transaction : recentTransactions) {
                HBox transactionRow = createTransactionRow(transaction);
                transactionsBox.getChildren().add(transactionRow);
            }
        }

        activityBox.getChildren().addAll(headerBox, transactionsBox);

        return activityBox;
    }

    private List<Transaction> getRecentTransactions() {
        List<Transaction> allTransactions = new java.util.ArrayList<>();
        for (Account account : currentCustomer.getAccounts()) {
            List<Transaction> accountTransactions = accountController.getAccountTransactions(account.getAccountNumber());
            allTransactions.addAll(accountTransactions);
        }
        allTransactions.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));
        return allTransactions.subList(0, Math.min(5, allTransactions.size()));
    }

    private HBox createTransactionRow(Transaction transaction) {
        HBox row = new HBox(20);
        row.setPadding(new Insets(15));
        row.setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-radius: 5px;");

        Label dateLabel = new Label(new java.text.SimpleDateFormat("MMM dd, yyyy").format(transaction.getDate()));
        dateLabel.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 12px;");

        Label typeLabel = new Label(transaction.getType());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        Label amountLabel = new Label(String.format("BWP %.2f", transaction.getAmount()));
        if (transaction.getAmount() > 0) {
            amountLabel.setStyle("-fx-text-fill: #38a169; -fx-font-weight: bold;");
        } else {
            amountLabel.setStyle("-fx-text-fill: #e53e3e; -fx-font-weight: bold;");
        }

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        row.getChildren().addAll(dateLabel, typeLabel, spacer, amountLabel);
        return row;
    }

    private HBox createFooter() {
        HBox footerBox = new HBox();
        footerBox.setPadding(new Insets(20, 0, 0, 0));
        footerBox.setAlignment(Pos.CENTER);

        Label footerLabel = new Label("🔒 Protected by Advanced Encryption • Fully Regulated Banking Environment");
        footerLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");

        footerBox.getChildren().add(footerLabel);
        return footerBox;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
