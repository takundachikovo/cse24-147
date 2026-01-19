package bank.view;

import bank.controller.TransactionController;
import bank.model.Account;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class DepositView {
    private Scene scene;
    private TransactionController transactionController;
    private Runnable onBackToDashboard;

    private ComboBox<Account> accountCombo;
    private TextField amountField;
    private Label currentBalanceLabel;
    private Label errorLabel;
    private Label successLabel;

    public DepositView(TransactionController transactionController, Runnable onBackToDashboard) {
        this.transactionController = transactionController;
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

        scene = new Scene(mainLayout, 1000, 600);

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

        Label featuresTitle = new Label("Deposit Benefits");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.web("#2D3748"));

        VBox featureList = new VBox(10);

        Label feature1 = createFeatureItem("💰", "Instant deposit processing");
        Label feature2 = createFeatureItem("🔒", "Secure transaction handling");
        Label feature3 = createFeatureItem("📈", "Grow your savings");
        Label feature4 = createFeatureItem("💳", "All account types supported");

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
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setPadding(new Insets(40));
        rightSection.setMaxWidth(500);

        
        VBox headerContainer = new VBox(10);
        headerContainer.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Make Deposit");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2D3748"));

        Label subtitleLabel = new Label("Add funds to your account securely");
        subtitleLabel.setFont(Font.font("System", 16));
        subtitleLabel.setTextFill(Color.web("#718096"));

        headerContainer.getChildren().addAll(titleLabel, subtitleLabel);

        
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);

        
        VBox accountBox = new VBox(5);
        accountBox.setAlignment(Pos.CENTER_LEFT);

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
        accountCombo.valueProperty().addListener((obs, oldVal, newVal) -> updateBalanceDisplay());
        accountBox.getChildren().addAll(accountLabel, accountCombo);

        
        VBox balanceBox = new VBox(5);
        balanceBox.setAlignment(Pos.CENTER_LEFT);

        Label balanceTitle = new Label("Current Balance");
        balanceTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        balanceTitle.setTextFill(Color.web("#4A5568"));

        currentBalanceLabel = new Label("P 0.00");
        currentBalanceLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        currentBalanceLabel.setTextFill(Color.web("#2D3748"));
        balanceBox.getChildren().addAll(balanceTitle, currentBalanceLabel);

        
        VBox amountBox = new VBox(5);
        amountBox.setAlignment(Pos.CENTER_LEFT);

        Label amountLabel = new Label("Deposit Amount *");
        amountLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        amountLabel.setTextFill(Color.web("#4A5568"));

        amountField = new TextField();
        amountField.setPromptText("Enter amount in Pula");
        amountField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        amountBox.getChildren().addAll(amountLabel, amountField);

        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setMaxWidth(Double.MAX_VALUE);

        successLabel = new Label();
        successLabel.setTextFill(Color.GREEN);
        successLabel.setVisible(false);
        successLabel.setWrapText(true);
        successLabel.setAlignment(Pos.CENTER);
        successLabel.setMaxWidth(Double.MAX_VALUE);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button depositButton = new Button("💳 Deposit Funds");
        depositButton.setStyle("-fx-pref-height: 50px; -fx-min-width: 200px; -fx-background-color: #667eea; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");
        depositButton.setOnAction(e -> handleDeposit());

        Button cancelButton = new Button("← Back to Dashboard");
        cancelButton.setStyle("-fx-pref-height: 45px; -fx-min-width: 180px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #667eea; -fx-border-radius: 8px; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> onBackToDashboard.run());

        buttonBox.getChildren().addAll(depositButton, cancelButton);

        formContainer.getChildren().addAll(
                accountBox, balanceBox, amountBox, errorLabel, successLabel, buttonBox
        );

        rightSection.getChildren().addAll(headerContainer, formContainer);
        return rightSection;
    }

    private void loadAccounts() {
        List<Account> accounts = transactionController.getAllCustomerAccounts();
        accountCombo.getItems().clear();
        accountCombo.getItems().addAll(accounts);

        if (!accounts.isEmpty()) {
            accountCombo.setValue(accounts.get(0));
            System.out.println("✅ Loaded " + accounts.size() + " accounts for deposit selection");
        } else {
            System.out.println("❌ No accounts found for deposit selection");
            showError("No accounts available. Please create an account first.");
        }
    }

    private void updateBalanceDisplay() {
        Account selected = accountCombo.getValue();
        if (selected != null) {
            currentBalanceLabel.setText(selected.getFormattedBalance());
        } else {
            currentBalanceLabel.setText("P 0.00");
        }
    }

    private void handleDeposit() {
        Account selectedAccount = accountCombo.getValue();
        String amountText = amountField.getText();

        if (selectedAccount == null) {
            showError("Please select an account");
            return;
        }

        if (amountText.isEmpty()) {
            showError("Please enter deposit amount");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            if (amount <= 0) {
                showError("Deposit amount must be greater than 0");
                return;
            }

            if (transactionController.deposit(selectedAccount.getAccountNumber(), amount)) {
                showSuccess("✅ Deposit successful! New balance: " +
                        String.format("P %.2f", selectedAccount.getBalance()));
                amountField.clear();
                updateBalanceDisplay();

                // Refresh account list to get updated balances
                loadAccounts();
            } else {
                showError("❌ Deposit failed");
            }

        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        errorLabel.setVisible(false);
    }

    public void refresh() {
        loadAccounts();
        clearMessages();
    }

    private void clearMessages() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    public Scene getScene() {
        return scene;
    }
}