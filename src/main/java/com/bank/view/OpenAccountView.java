package com.bank.view;

import com.bank.controller.AccountController;
import com.bank.controller.LoginController;
import com.bank.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OpenAccountView extends BaseView {
    private LoginController loginController;
    private AccountController accountController;
    private Stage primaryStage;
    private Customer currentCustomer;
    private String selectedAccountType = "SAVINGS";

    public OpenAccountView(Stage primaryStage, LoginController loginController, AccountController accountController) {
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

        Label breadcrumbLabel = new Label("Dashboard > Open New Account");
        breadcrumbLabel.setStyle("-fx-text-fill: #4a5568;");

        headerBox.getChildren().addAll(bankLabel, spacer, breadcrumbLabel);
        view.getChildren().add(headerBox);

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label titleLabel = new Label("Open New Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        Label subtitleLabel = new Label("Choose the account type that fits your needs");
        subtitleLabel.setStyle("-fx-text-fill: #4a5568;");

        VBox accountSelectionBox = new VBox(15);
        Label selectionLabel = new Label("Select Account Type");
        selectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        HBox accountCardsBox = new HBox(20);
        accountCardsBox.setAlignment(Pos.TOP_CENTER);

        VBox savingsCard = createAccountTypeCard("💰", "Savings Account", "0.05% monthly interest", "No withdrawals", "No minimum balance");
        VBox investmentCard = createAccountTypeCard("📈", "Investment Account", "5% monthly interest", "Withdrawals allowed", "BWP 500 minimum");
        VBox chequeCard = createAccountTypeCard("🏦", "Cheque Account", "No interest", "Unlimited transactions", "Employment required");

        accountCardsBox.getChildren().addAll(savingsCard, investmentCard, chequeCard);
        accountSelectionBox.getChildren().addAll(selectionLabel, accountCardsBox);

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20, 0, 0, 0));
        formBox.setStyle("-fx-min-height: 300px;");

        contentBox.getChildren().addAll(titleLabel, subtitleLabel, accountSelectionBox, formBox);
        view.getChildren().add(contentBox);

        selectAccountType(savingsCard, "SAVINGS");
    }

    private VBox createAccountTypeCard(String emoji, String title, String... features) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: #f8f9fa; " +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-border-color: #cbd5e0;" +
                "-fx-border-width: 1px;" +
                "-fx-cursor: hand;");

        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 24px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a365d;");

        VBox featuresBox = new VBox(5);
        for (String feature : features) {
            Label featureLabel = new Label("• " + feature);
            featureLabel.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 12px;");
            featuresBox.getChildren().add(featureLabel);
        }

        Label selectLabel = new Label("Select");
        selectLabel.setStyle("-fx-text-fill: #3182ce; -fx-font-weight: bold; -fx-font-size: 12px;");

        card.getChildren().addAll(emojiLabel, titleLabel, featuresBox, selectLabel);

        card.setOnMouseClicked(e -> selectAccountType(card, getAccountTypeFromTitle(title)));

        return card;
    }

    private String getAccountTypeFromTitle(String title) {
        if ("Savings Account".equals(title)) {
            return "SAVINGS";
        } else if ("Investment Account".equals(title)) {
            return "INVESTMENT";
        } else if ("Cheque Account".equals(title)) {
            return "CHEQUE";
        } else {
            return "SAVINGS";
        }
    }

    private void selectAccountType(VBox selectedCard, String accountType) {
        HBox parent = (HBox) selectedCard.getParent();
        for (javafx.scene.Node node : parent.getChildren()) {
            if (node instanceof VBox) {
                VBox card = (VBox) node;
                card.setStyle("-fx-background-color: #f8f9fa; " +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-border-color: #cbd5e0;" +
                        "-fx-border-width: 1px;");
            }
        }

        selectedCard.setStyle("-fx-background-color: #ebf8ff; " +
                "-fx-background-radius: 10px;" +
                "-fx-border-radius: 10px;" +
                "-fx-border-color: #3182ce;" +
                "-fx-border-width: 2px;");

        this.selectedAccountType = accountType;
        updateAccountForm();
    }

    private void updateAccountForm() {
        VBox contentBox = (VBox) view.getChildren().get(1);
        VBox formBox = (VBox) contentBox.getChildren().get(3);
        formBox.getChildren().clear();

        Label formTitle = new Label("Account Details");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");
        formBox.getChildren().add(formTitle);

        javafx.scene.control.TextField initialDepositField = createTextField("Initial Deposit Amount");

        if ("INVESTMENT".equals(selectedAccountType)) {
            Label investmentWarning = new Label("Minimum opening deposit: BWP 500.00");
            investmentWarning.setStyle("-fx-text-fill: #d69e2e; -fx-font-weight: bold;");
            formBox.getChildren().add(investmentWarning);
        } else if ("CHEQUE".equals(selectedAccountType)) {
            javafx.scene.control.TextField companyNameField = createTextField("Company Name");
            javafx.scene.control.TextField companyAddressField = createTextField("Company Address");
            formBox.getChildren().addAll(companyNameField, companyAddressField);
        }

        javafx.scene.control.CheckBox termsCheckbox = new javafx.scene.control.CheckBox("I agree to the terms and conditions for opening a " +
                selectedAccountType.toLowerCase() + " account");
        termsCheckbox.setStyle("-fx-text-fill: #4a5568;");

        VBox buttonsBox = new VBox(10);
        buttonsBox.setStyle("-fx-alignment: center; -fx-padding: 20 0 0 0;");

        javafx.scene.control.Button openAccountBtn = createPrimaryButton("Open Account");
        javafx.scene.control.Button cancelBtn = createSecondaryButton("Cancel");

        buttonsBox.getChildren().addAll(openAccountBtn, cancelBtn);

        formBox.getChildren().addAll(initialDepositField, termsCheckbox, buttonsBox);

        openAccountBtn.setOnAction(e -> {
            String companyName = "";
            String companyAddress = "";

            if ("CHEQUE".equals(selectedAccountType)) {
                companyName = ((javafx.scene.control.TextField) formBox.getChildren().get(2)).getText();
                companyAddress = ((javafx.scene.control.TextField) formBox.getChildren().get(3)).getText();
            }

            handleOpenAccount(initialDepositField.getText(), companyName, companyAddress, termsCheckbox.isSelected());
        });

        cancelBtn.setOnAction(e -> {
            DashboardView dashboardView = new DashboardView(primaryStage, loginController);
            primaryStage.getScene().setRoot(dashboardView.getView());
        });
    }

    private void handleOpenAccount(String initialDepositStr, String companyName, String companyAddress, boolean acceptedTerms) {
        if (!acceptedTerms) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please accept the terms and conditions.");
            return;
        }

        double initialDeposit = 0.0;
        try {
            if (!initialDepositStr.isEmpty()) {
                initialDeposit = Double.parseDouble(initialDepositStr);
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid deposit amount.");
            return;
        }

        boolean success = false;
        String message = "";

        if ("SAVINGS".equals(selectedAccountType)) {
            success = accountController.openSavingsAccount(currentCustomer);
            message = "Savings account opened successfully!";
        } else if ("INVESTMENT".equals(selectedAccountType)) {
            if (initialDeposit < 500.0) {
                showAlert(Alert.AlertType.ERROR, "Error", "Investment account requires minimum BWP 500.00 opening deposit.");
                return;
            }
            success = accountController.openInvestmentAccount(currentCustomer, initialDeposit);
            message = "Investment account opened successfully!";
        } else if ("CHEQUE".equals(selectedAccountType)) {
            if (companyName.isEmpty() || companyAddress.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please provide company name and address for cheque account.");
                return;
            }
            success = accountController.openChequeAccount(currentCustomer, companyName, companyAddress);
            message = "Cheque account opened successfully!";
        }

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", message);
            DashboardView dashboardView = new DashboardView(primaryStage, loginController);
            primaryStage.getScene().setRoot(dashboardView.getView());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open account. Please try again.");
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
