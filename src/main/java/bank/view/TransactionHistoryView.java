package bank.view;

import bank.controller.AccountController;
import bank.model.Account;
import bank.model.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class TransactionHistoryView {
    private Scene scene;
    private AccountController accountController;
    private Runnable onBackToDashboard;

    private ComboBox<Account> accountCombo;
    private TableView<Transaction> transactionTable;
    private Label noTransactionsLabel;

    public TransactionHistoryView(AccountController accountController, Runnable onBackToDashboard) {
        this.accountController = accountController;
        this.onBackToDashboard = onBackToDashboard;
        createView();
    }

    private void createView() {
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f7fafc;");

       
        VBox leftSection = createLeftSection();
        mainLayout.setLeft(leftSection);

        
        VBox rightSection = createRightSection();
        mainLayout.setCenter(rightSection);

        scene = new Scene(mainLayout, 1200, 800);

        loadAccounts();
    }

    private VBox createLeftSection() {
        VBox leftSection = new VBox(30);
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setPadding(new Insets(40));
        leftSection.setPrefWidth(400);
        leftSection.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 1px 0 0;");

       
        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);

        Label bankLogo = new Label("🏦");
        bankLogo.setFont(Font.font(48));

        Label bankName = new Label("Bank");
        bankName.setFont(Font.font("System", FontWeight.BOLD, 32));
        bankName.setTextFill(Color.web("#2D3748"));

        Label bankTagline = new Label("Secure Banking Solutions");
        bankTagline.setFont(Font.font("System", 16));
        bankTagline.setTextFill(Color.web("#718096"));

        logoContainer.getChildren().addAll(bankLogo, bankName, bankTagline);

       
        VBox featuresContainer = new VBox(15);
        featuresContainer.setAlignment(Pos.CENTER_LEFT);

        Label featuresTitle = new Label("Transaction Features");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.web("#2D3748"));

        VBox featureList = new VBox(10);

        Label feature1 = createFeatureItem("📊", "Complete transaction history");
        Label feature2 = createFeatureItem("🔍", "Detailed transaction information");
        Label feature3 = createFeatureItem("💳", "All account types supported");
        Label feature4 = createFeatureItem("🛡️", "Secure transaction tracking");

        featureList.getChildren().addAll(feature1, feature2, feature3, feature4);
        featuresContainer.getChildren().addAll(featuresTitle, featureList);

        leftSection.getChildren().addAll(logoContainer, new Separator(), featuresContainer);
        return leftSection;
    }

    private Label createFeatureItem(String emoji, String text) {
        HBox featureBox = new HBox(10);
        featureBox.setAlignment(Pos.CENTER_LEFT);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(16));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("System", 14));
        textLabel.setTextFill(Color.web("#4A5568"));
        textLabel.setWrapText(true);

        featureBox.getChildren().addAll(emojiLabel, textLabel);

        Label container = new Label();
        container.setGraphic(featureBox);
        container.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        return container;
    }

    private VBox createRightSection() {
        VBox rightSection = new VBox(30);
        rightSection.setAlignment(Pos.TOP_CENTER);
        rightSection.setPadding(new Insets(40));
        rightSection.setMaxWidth(800);

        
        VBox headerContainer = new VBox(10);
        headerContainer.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Transaction History");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2D3748"));

        Label subtitleLabel = new Label("View your complete transaction history");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#718096"));

        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        
        VBox accountSelectionBox = new VBox(10);
        accountSelectionBox.setAlignment(Pos.CENTER_LEFT);

        Label accountLabel = new Label("Select Account *");
        accountLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        accountLabel.setTextFill(Color.web("#4A5568"));

        accountCombo = new ComboBox<>();
        accountCombo.setPrefWidth(400);
        accountCombo.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        accountCombo.setCellFactory(lv -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                setText(empty ? "" : account.getAccountNumber() + " - " + account.getAccountType() + " (P " + String.format("%.2f", account.getBalance()) + ")");
            }
        });
        accountCombo.setButtonCell(new ListCell<Account>() {
            @Override
            protected void updateItem(Account account, boolean empty) {
                super.updateItem(account, empty);
                setText(empty ? "" : account.getAccountNumber() + " - " + account.getAccountType() + " (P " + String.format("%.2f", account.getBalance()) + ")");
            }
        });
        accountCombo.valueProperty().addListener((obs, oldVal, newVal) -> loadTransactions());

        accountSelectionBox.getChildren().addAll(accountLabel, accountCombo);

        
        VBox tableContainer = new VBox(15);
        tableContainer.setAlignment(Pos.CENTER);

        transactionTable = createTransactionTable();
        transactionTable.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");

        noTransactionsLabel = new Label("📝 No transactions found for selected account");
        noTransactionsLabel.setFont(Font.font("System", 14));
        noTransactionsLabel.setTextFill(Color.web("#718096"));
        noTransactionsLabel.setVisible(false);
        noTransactionsLabel.setAlignment(Pos.CENTER);
        noTransactionsLabel.setMaxWidth(Double.MAX_VALUE);
        noTransactionsLabel.setPadding(new Insets(20));

        tableContainer.getChildren().addAll(transactionTable, noTransactionsLabel);

        
        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle("-fx-pref-height: 45px; -fx-min-width: 200px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #667eea; -fx-border-radius: 8px; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        backButton.setOnAction(e -> onBackToDashboard.run());

        rightSection.getChildren().addAll(headerContainer, accountSelectionBox, tableContainer, backButton);
        return rightSection;
    }

    private TableView<Transaction> createTransactionTable() {
        TableView<Transaction> table = new TableView<>();
        table.setPlaceholder(new Label("Select an account to view transactions"));
        table.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");

        
        TableColumn<Transaction, String> dateColumn = new TableColumn<>("📅 Date");
        dateColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(transaction.getFormattedDate());
        });
        dateColumn.setPrefWidth(150);
        dateColumn.setStyle("-fx-alignment: CENTER_LEFT;");

       
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("🔖 Type");
        typeColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(transaction.getType());
        });
        typeColumn.setPrefWidth(120);
        typeColumn.setStyle("-fx-alignment: CENTER;");

        
        TableColumn<Transaction, String> descColumn = new TableColumn<>("📋 Description");
        descColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(transaction.getDescription());
        });
        descColumn.setPrefWidth(250);
        descColumn.setStyle("-fx-alignment: CENTER_LEFT;");

        
        TableColumn<Transaction, String> amountColumn = new TableColumn<>("💰 Amount");
        amountColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(transaction.getFormattedAmount());
        });
        amountColumn.setPrefWidth(130);
        amountColumn.setStyle("-fx-alignment: CENTER_RIGHT;");

        
        TableColumn<Transaction, String> balanceColumn = new TableColumn<>("💵 Balance After");
        balanceColumn.setCellValueFactory(cellData -> {
            Transaction transaction = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(transaction.getFormattedBalanceAfter());
        });
        balanceColumn.setPrefWidth(130);
        balanceColumn.setStyle("-fx-alignment: CENTER_RIGHT;");

        table.getColumns().addAll(dateColumn, typeColumn, descColumn, amountColumn, balanceColumn);

        
        for (TableColumn<?, ?> column : table.getColumns()) {
            column.setStyle("-fx-font-weight: bold; -fx-text-fill: #4A5568;");
        }

        return table;
    }

    private void loadAccounts() {
        List<Account> accounts = accountController.getCustomerAccounts();
        accountCombo.getItems().clear();
        accountCombo.getItems().addAll(accounts);

        if (!accounts.isEmpty()) {
            accountCombo.setValue(accounts.get(0));
            System.out.println("✅ Loaded " + accounts.size() + " accounts for transaction history");
        } else {
            System.out.println("❌ No accounts found for transaction history");
            noTransactionsLabel.setText("No accounts available. Please create an account first.");
            noTransactionsLabel.setVisible(true);
        }
    }

    private void loadTransactions() {
        Account selectedAccount = accountCombo.getValue();
        if (selectedAccount != null) {
            List<Transaction> transactions = selectedAccount.getTransactions();
            transactionTable.getItems().clear();
            transactionTable.getItems().addAll(transactions);

            noTransactionsLabel.setVisible(transactions.isEmpty());

            if (!transactions.isEmpty()) {
                System.out.println("✅ Loaded " + transactions.size() + " transactions for account: " + selectedAccount.getAccountNumber());
            } else {
                System.out.println("ℹ️ No transactions found for account: " + selectedAccount.getAccountNumber());
            }
        } else {
            transactionTable.getItems().clear();
            noTransactionsLabel.setVisible(false);
        }
    }

    public void refresh() {
        loadAccounts();
        loadTransactions();
    }

    public Scene getScene() {
        return scene;
    }
}