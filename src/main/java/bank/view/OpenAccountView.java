package bank.view;

import bank.controller.AccountController;
import bank.model.Account;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OpenAccountView {
    private Scene scene;
    private AccountController accountController;
    private Runnable onBackToDashboard;
    private Runnable onAccountOpened;

    private String selectedAccountType;
    private TextField initialDepositField;
    private ComboBox<String> branchCombo;
    private TextField companyNameField;
    private TextArea companyAddressField;
    private Label errorLabel;
    private Label successLabel;
    private VBox employmentDetailsContainer;
    private Button openAccountButton;

    public OpenAccountView(AccountController accountController,
                           Runnable onBackToDashboard, Runnable onAccountOpened) {
        this.accountController = accountController;
        this.onBackToDashboard = onBackToDashboard;
        this.onAccountOpened = onAccountOpened;
        createView();
    }

    private void createView() {

        ScrollPane mainScrollPane = new ScrollPane();
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setPadding(new Insets(10));

        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Button backButton = new Button("← Back to Dashboard");
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #667eea; -fx-font-size: 14px; -fx-cursor: hand; -fx-border-color: transparent;");
        backButton.setOnAction(e -> onBackToDashboard.run());

        HBox.setHgrow(backButton, Priority.ALWAYS);
        header.getChildren().add(backButton);


        Label titleLabel = new Label("Open New Account");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2D3748"));


        VBox typeSelection = createAccountTypeSelection();


        VBox formContainer = new VBox(15);
        formContainer.setMaxWidth(500);
        formContainer.setAlignment(Pos.TOP_CENTER);


        VBox branchBox = new VBox(5);
        Label branchLabel = new Label("Branch *");
        branchLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        branchCombo = new ComboBox<>();
        branchCombo.getItems().addAll("Main Branch", "Francistown Branch", "Maun Branch", "Palapye Branch", "Serowe Branch");
        branchCombo.setValue("Main Branch");
        branchCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        branchBox.getChildren().addAll(branchLabel, branchCombo);


        VBox depositBox = new VBox(5);
        Label depositLabel = new Label("Initial Deposit Amount *");
        depositLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        initialDepositField = new TextField();
        initialDepositField.setPromptText("Enter amount in Pula");
        initialDepositField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        depositBox.getChildren().addAll(depositLabel, initialDepositField);


        employmentDetailsContainer = new VBox(10);
        employmentDetailsContainer.setVisible(false);

        VBox companyNameBox = new VBox(5);
        Label companyNameLabel = new Label("Company Name *");
        companyNameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        companyNameField = new TextField();
        companyNameField.setPromptText("Enter your company name");
        companyNameField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        companyNameBox.getChildren().addAll(companyNameLabel, companyNameField);

        VBox companyAddressBox = new VBox(5);
        Label companyAddressLabel = new Label("Company Address *");
        companyAddressLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        companyAddressField = new TextArea();
        companyAddressField.setPromptText("Enter company address");
        companyAddressField.setPrefRowCount(2);
        companyAddressField.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-padding: 8px 12px;");
        companyAddressBox.getChildren().addAll(companyAddressLabel, companyAddressField);

        employmentDetailsContainer.getChildren().addAll(companyNameBox, companyAddressBox);


        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);

        successLabel = new Label();
        successLabel.setTextFill(Color.GREEN);
        successLabel.setVisible(false);
        successLabel.setWrapText(true);


        formContainer.getChildren().addAll(
                branchBox, depositBox, employmentDetailsContainer, errorLabel, successLabel
        );


        mainContainer.getChildren().addAll(
                header, titleLabel, typeSelection, formContainer
        );

        mainScrollPane.setContent(mainContainer);


        HBox buttonsContainer = new HBox(15);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setPadding(new Insets(20));
        buttonsContainer.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; -fx-border-width: 1px 0 0 0;");

        openAccountButton = new Button("OPEN ACCOUNT");
        openAccountButton.setStyle("-fx-pref-height: 50px; -fx-min-width: 200px; -fx-background-color: #667eea; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");
        openAccountButton.setDisable(true);
        openAccountButton.setOnAction(e -> handleOpenAccount());

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-pref-height: 45px; -fx-min-width: 120px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #667eea; -fx-border-radius: 8px; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> onBackToDashboard.run());

        buttonsContainer.getChildren().addAll(openAccountButton, cancelButton);


        BorderPane rootLayout = new BorderPane();
        rootLayout.setCenter(mainScrollPane);
        rootLayout.setBottom(buttonsContainer);

        scene = new Scene(rootLayout, 900, 600);
    }

    private VBox createAccountTypeSelection() {
        VBox typeContainer = new VBox(15);
        typeContainer.setMaxWidth(600);

        Label sectionTitle = new Label("Choose Account Type");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        HBox accountCards = new HBox(15);
        accountCards.setAlignment(Pos.CENTER);


        VBox savingsCard = createAccountTypeCard(
                "SAVINGS",
                "Savings Account",
                "0.05% monthly interest\nNo withdrawals allowed\nPerfect for saving",
                "💰"
        );


        VBox investmentCard = createAccountTypeCard(
                "INVESTMENT",
                "Investment Account",
                "5% monthly interest\nMinimum P 500 deposit\nWithdrawals allowed\nHigher returns",
                "📈"
        );


        VBox chequeCard = createAccountTypeCard(
                "CHEQUE",
                "Cheque Account",
                "No interest\nSalary deposits\nWithdrawals allowed\nEmployment required",
                "💳"
        );

        accountCards.getChildren().addAll(savingsCard, investmentCard, chequeCard);
        typeContainer.getChildren().addAll(sectionTitle, accountCards);

        return typeContainer;
    }

    private VBox createAccountTypeCard(String type, String title, String description, String emoji) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e2e8f0; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 2); -fx-cursor: hand;");
        card.setPadding(new Insets(15));
        card.setPrefWidth(170);


        card.setOnMouseClicked(e -> {
            selectAccountType(type, card);
        });

        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(20));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#2D3748"));

        TextArea descArea = new TextArea(description);
        descArea.setEditable(false);
        descArea.setWrapText(true);
        descArea.setPrefRowCount(3);
        descArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #718096; -fx-font-size: 11px;");
        descArea.setPrefHeight(60);

        card.getChildren().addAll(emojiLabel, titleLabel, descArea);
        return card;
    }

    private void selectAccountType(String accountType, VBox selectedCard) {
        this.selectedAccountType = accountType;


        BorderPane root = (BorderPane) scene.getRoot();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        VBox mainContainer = (VBox) scrollPane.getContent();

        for (var node : mainContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox typeContainer = (VBox) node;
                for (var containerNode : typeContainer.getChildren()) {
                    if (containerNode instanceof HBox) {
                        HBox cardsContainer = (HBox) containerNode;
                        for (var cardNode : cardsContainer.getChildren()) {
                            if (cardNode instanceof VBox) {
                                cardNode.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e2e8f0; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 2); -fx-cursor: hand;");
                            }
                        }
                    }
                }
            }
        }


        selectedCard.setStyle("-fx-background-color: #f0f4ff; -fx-background-radius: 12px; -fx-border-color: #667eea; -fx-border-width: 2px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.3), 15, 0, 0, 3); -fx-cursor: hand;");

        onAccountTypeSelected(accountType);
        openAccountButton.setDisable(false);

        System.out.println("✅ Account type selected: " + accountType);
    }

    private void onAccountTypeSelected(String accountType) {
        boolean showEmploymentFields = "CHEQUE".equals(accountType);
        employmentDetailsContainer.setVisible(showEmploymentFields);


        companyNameField.clear();
        companyAddressField.clear();
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    private void handleOpenAccount() {
        if (selectedAccountType == null) {
            showError("Please select an account type");
            return;
        }

        String initialDepositText = initialDepositField.getText();
        String branch = branchCombo.getValue();

        if (initialDepositText.isEmpty()) {
            showError("Please enter initial deposit amount");
            return;
        }

        if (branch == null || branch.isEmpty()) {
            showError("Please select a branch");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(initialDepositText);

            if (initialDeposit <= 0) {
                showError("Initial deposit must be greater than 0");
                return;
            }

            if ("CHEQUE".equals(selectedAccountType)) {
                if (companyNameField.getText().isEmpty() || companyAddressField.getText().isEmpty()) {
                    showError("Please fill in company details for cheque account");
                    return;
                }
            }

            if ("INVESTMENT".equals(selectedAccountType) && initialDeposit < 500) {
                showError("❌ Investment account requires minimum P 500.00 opening deposit");
                return;
            }

            System.out.println("💼 Attempting to create " + selectedAccountType + " account:");
            System.out.println("   💵 Deposit: P " + initialDeposit);
            System.out.println("   🏢 Branch: " + branch);


            Account newAccount = null;
            switch (selectedAccountType) {
                case "SAVINGS" -> {
                    newAccount = accountController.openSavingsAccount(initialDeposit, branch);
                }
                case "INVESTMENT" -> {
                    newAccount = accountController.openInvestmentAccount(initialDeposit, branch);
                }
                case "CHEQUE" -> {
                    newAccount = accountController.openChequeAccount(initialDeposit, branch,
                            companyNameField.getText(), companyAddressField.getText());
                }
            }

            if (newAccount != null) {
                showSuccess("✅ Account opened successfully!\nAccount Number: " + newAccount.getAccountNumber() +
                        "\nBalance: " + newAccount.getFormattedBalance() +
                        "\nType: " + newAccount.getAccountType() +
                        "\nBranch: " + branch);

                System.out.println("✅ SUCCESS: Account created - " + newAccount.getAccountNumber() + " for customer");


                clearForm();


                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // Show success message for 3 seconds
                        javafx.application.Platform.runLater(() -> {
                            onAccountOpened.run();
                        });
                    } catch (InterruptedException e) {
                        javafx.application.Platform.runLater(() -> {
                            onAccountOpened.run();
                        });
                    }
                }).start();

            } else {
                showError("❌ Failed to open account. Please try again.");
                System.out.println("❌ FAILED: Account creation returned null");
            }

        } catch (NumberFormatException e) {
            showError("Please enter a valid numeric amount");
        } catch (Exception e) {
            showError("Failed to open account: " + e.getMessage());
            e.printStackTrace();
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

    public Scene getScene() {
        return scene;
    }

    public void clearForm() {
        initialDepositField.clear();
        branchCombo.setValue("Main Branch");
        companyNameField.clear();
        companyAddressField.clear();
        selectedAccountType = null;
        employmentDetailsContainer.setVisible(false);
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
        openAccountButton.setDisable(true);


        BorderPane root = (BorderPane) scene.getRoot();
        ScrollPane scrollPane = (ScrollPane) root.getCenter();
        VBox mainContainer = (VBox) scrollPane.getContent();

        for (var node : mainContainer.getChildren()) {
            if (node instanceof VBox) {
                VBox typeContainer = (VBox) node;
                for (var containerNode : typeContainer.getChildren()) {
                    if (containerNode instanceof HBox) {
                        HBox cardsContainer = (HBox) containerNode;
                        for (var cardNode : cardsContainer.getChildren()) {
                            if (cardNode instanceof VBox) {
                                cardNode.setStyle("-fx-background-color: white; -fx-background-radius: 12px; -fx-border-color: #e2e8f0; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.1), 10, 0, 0, 2); -fx-cursor: hand;");
                            }
                        }
                    }
                }
            }
        }
    }
}