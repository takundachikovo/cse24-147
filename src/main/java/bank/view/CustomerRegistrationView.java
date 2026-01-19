package bank.view;

import bank.controller.LoginController;
import bank.controller.AccountController;
import bank.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomerRegistrationView {
    private Scene scene;
    private LoginController loginController;
    private AccountController accountController;
    private Runnable onRegistrationSuccess;
    private Runnable onShowLogin;

    // Customer type selection
    private ToggleGroup customerTypeGroup;
    private RadioButton individualRadio;
    private RadioButton companyRadio;

    // Common fields
    private TextField firstNameField;
    private TextField surnameField;
    private TextArea addressField;
    private TextField usernameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;

    // Individual customer fields
    private TextField idNumberField;
    private ComboBox<String> occupationCombo;
    private ComboBox<String> employmentCombo;

    // Company customer fields
    private TextField registrationNumberField;
    private ComboBox<String> businessTypeCombo;
    private TextField contactPersonField;
    private ComboBox<String> companySizeCombo;

    // Account creation fields
    private ComboBox<String> accountTypeCombo;
    private ComboBox<String> branchCombo;
    private TextField initialDepositField;

    private Label errorLabel;
    private Label successLabel;
    private VBox individualDetailsContainer;
    private VBox companyDetailsContainer;
    private VBox accountCreationContainer;

    public CustomerRegistrationView(LoginController loginController, AccountController accountController,
                                    Runnable onRegistrationSuccess, Runnable onShowLogin) {
        this.loginController = loginController;
        this.accountController = accountController;
        this.onRegistrationSuccess = onRegistrationSuccess;
        this.onShowLogin = onShowLogin;
        createView();
    }

    private void createView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f7fafc;");

        VBox leftSection = createLeftSection();
        mainLayout.setLeft(leftSection);

        VBox rightSection = createRightSection();
        mainLayout.setCenter(rightSection);

        scene = new Scene(mainLayout, 1200, 700);
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

        Label featuresTitle = new Label("Account Types Available");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.web("#2D3748"));

        VBox featureList = new VBox(10);

        Label feature1 = createFeatureItem("💰", "Savings Account - 0.05% monthly interest");
        Label feature2 = createFeatureItem("📈", "Investment Account - 5% monthly interest");
        Label feature3 = createFeatureItem("💳", "Cheque Account - Salary deposits");

        featureList.getChildren().addAll(feature1, feature2, feature3);
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
        VBox rightSection = new VBox(20);
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setPadding(new Insets(40));
        rightSection.setMaxWidth(600);

        Label titleLabel = new Label("Register New Account");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2D3748"));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(500);

        VBox formContainer = new VBox(15);
        formContainer.setPadding(new Insets(20));
        formContainer.setMaxWidth(550);

        // Customer Type Selection
        VBox customerTypeSection = new VBox(10);
        Label customerTypeLabel = new Label("Customer Type *");
        customerTypeLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        customerTypeGroup = new ToggleGroup();
        individualRadio = new RadioButton("Individual Customer");
        companyRadio = new RadioButton("Company Customer");
        individualRadio.setToggleGroup(customerTypeGroup);
        companyRadio.setToggleGroup(customerTypeGroup);
        individualRadio.setSelected(true);

        HBox radioBox = new HBox(20);
        radioBox.getChildren().addAll(individualRadio, companyRadio);

        customerTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            toggleCustomerTypeFields();
        });

        customerTypeSection.getChildren().addAll(customerTypeLabel, radioBox);

        // Common Personal Information
        VBox personalSection = new VBox(10);
        Label personalLabel = new Label("Personal Information");
        personalLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        HBox nameBox = new HBox(15);
        VBox firstNameBox = new VBox(5);
        Label firstNameLabel = new Label("First Name *");
        firstNameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        firstNameField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        firstNameBox.getChildren().addAll(firstNameLabel, firstNameField);

        VBox surnameBox = new VBox(5);
        Label surnameLabel = new Label("Surname *");
        surnameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        surnameField = new TextField();
        surnameField.setPromptText("Enter surname");
        surnameField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        surnameBox.getChildren().addAll(surnameLabel, surnameField);

        nameBox.getChildren().addAll(firstNameBox, surnameBox);

        VBox addressBox = new VBox(5);
        Label addressLabel = new Label("Address *");
        addressLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        addressField = new TextArea();
        addressField.setPromptText("Enter your full address");
        addressField.setPrefRowCount(3);
        addressField.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-padding: 8px 12px;");
        addressBox.getChildren().addAll(addressLabel, addressField);

        personalSection.getChildren().addAll(personalLabel, nameBox, addressBox);

        // Individual Customer Details
        individualDetailsContainer = new VBox(10);
        Label individualLabel = new Label("Individual Details");
        individualLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        VBox idNumberBox = new VBox(5);
        Label idNumberLabel = new Label("ID Number *");
        idNumberLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        idNumberField = new TextField();
        idNumberField.setPromptText("Enter national ID number");
        idNumberField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        idNumberBox.getChildren().addAll(idNumberLabel, idNumberField);

        VBox occupationBox = new VBox(5);
        Label occupationLabel = new Label("Occupation");
        occupationLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        occupationCombo = new ComboBox<>();
        occupationCombo.getItems().addAll("Select Occupation", "Student", "Employed", "Self-Employed", "Retired", "Other");
        occupationCombo.setValue("Select Occupation");
        occupationCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        occupationBox.getChildren().addAll(occupationLabel, occupationCombo);

        VBox employmentBox = new VBox(5);
        Label employmentLabel = new Label("Employment Status");
        employmentLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        employmentCombo = new ComboBox<>();
        employmentCombo.getItems().addAll("Select Status", "Full-time", "Part-time", "Contract", "Unemployed");
        employmentCombo.setValue("Select Status");
        employmentCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        employmentBox.getChildren().addAll(employmentLabel, employmentCombo);

        individualDetailsContainer.getChildren().addAll(individualLabel, idNumberBox, occupationBox, employmentBox);

        // Company Customer Details
        companyDetailsContainer = new VBox(10);
        companyDetailsContainer.setVisible(false);
        Label companyLabel = new Label("Company Details");
        companyLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        VBox regNumberBox = new VBox(5);
        Label regNumberLabel = new Label("Registration Number *");
        regNumberLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        registrationNumberField = new TextField();
        registrationNumberField.setPromptText("Enter company registration number");
        registrationNumberField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        regNumberBox.getChildren().addAll(regNumberLabel, registrationNumberField);

        VBox businessTypeBox = new VBox(5);
        Label businessTypeLabel = new Label("Business Type");
        businessTypeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        businessTypeCombo = new ComboBox<>();
        businessTypeCombo.getItems().addAll("Select Business Type", "Retail", "Manufacturing", "Services", "Technology", "Finance", "Other");
        businessTypeCombo.setValue("Select Business Type");
        businessTypeCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        businessTypeBox.getChildren().addAll(businessTypeLabel, businessTypeCombo);

        VBox contactPersonBox = new VBox(5);
        Label contactPersonLabel = new Label("Contact Person");
        contactPersonLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        contactPersonField = new TextField();
        contactPersonField.setPromptText("Enter contact person name");
        contactPersonField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        contactPersonBox.getChildren().addAll(contactPersonLabel, contactPersonField);

        VBox companySizeBox = new VBox(5);
        Label companySizeLabel = new Label("Company Size");
        companySizeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        companySizeCombo = new ComboBox<>();
        companySizeCombo.getItems().addAll("Select Size", "Small (1-50)", "Medium (51-200)", "Large (201-1000)", "Enterprise (1000+)");
        companySizeCombo.setValue("Select Size");
        companySizeCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        companySizeBox.getChildren().addAll(companySizeLabel, companySizeCombo);

        companyDetailsContainer.getChildren().addAll(companyLabel, regNumberBox, businessTypeBox, contactPersonBox, companySizeBox);

        // Account Creation Section
        accountCreationContainer = new VBox(10);
        Label accountLabel = new Label("Create Your First Account *");
        accountLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox accountTypeBox = new VBox(5);
        Label accountTypeLabel = new Label("Account Type *");
        accountTypeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        accountTypeCombo.setValue("Savings Account");
        accountTypeCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        accountTypeBox.getChildren().addAll(accountTypeLabel, accountTypeCombo);

        VBox branchBox = new VBox(5);
        Label branchLabel = new Label("Branch *");
        branchLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        branchCombo = new ComboBox<>();
        branchCombo.getItems().addAll("Main Branch - Gaborone", "Francistown Branch", "Maun Branch", "Palapye Branch", "Serowe Branch");
        branchCombo.setValue("Main Branch - Gaborone");
        branchCombo.setStyle("-fx-pref-height: 40px; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 8px;");
        branchBox.getChildren().addAll(branchLabel, branchCombo);

        VBox depositBox = new VBox(5);
        Label depositLabel = new Label("Initial Deposit *");
        depositLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        initialDepositField = new TextField();
        initialDepositField.setPromptText("Enter initial deposit amount");
        initialDepositField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        depositBox.getChildren().addAll(depositLabel, initialDepositField);

        accountCreationContainer.getChildren().addAll(accountLabel, accountTypeBox, branchBox, depositBox);

        // Login Information
        VBox loginSection = new VBox(10);
        Label loginLabel = new Label("Login Information");
        loginLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username *");
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        usernameField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email Address");
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        emailBox.getChildren().addAll(emailLabel, emailField);

        HBox passwordBox = new HBox(15);
        VBox passwordFieldBox = new VBox(5);
        Label passwordLabel = new Label("Password *");
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        passwordFieldBox.getChildren().addAll(passwordLabel, passwordField);

        VBox confirmPasswordBox = new VBox(5);
        Label confirmPasswordLabel = new Label("Confirm Password *");
        confirmPasswordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");
        confirmPasswordField.setStyle("-fx-pref-height: 40px; -fx-padding: 0 12px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #e2e8f0; -fx-border-radius: 8px; -fx-font-size: 14px;");
        confirmPasswordBox.getChildren().addAll(confirmPasswordLabel, confirmPasswordField);

        passwordBox.getChildren().addAll(passwordFieldBox, confirmPasswordBox);
        loginSection.getChildren().addAll(loginLabel, usernameBox, emailBox, passwordBox);

        // Error and success labels
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);

        successLabel = new Label();
        successLabel.setTextFill(Color.GREEN);
        successLabel.setVisible(false);
        successLabel.setWrapText(true);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button registerButton = new Button("Complete Registration");
        registerButton.setStyle("-fx-pref-height: 50px; -fx-min-width: 200px; -fx-background-color: #667eea; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");
        registerButton.setOnAction(e -> handleRegistration());

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-pref-height: 45px; -fx-min-width: 120px; -fx-background-color: white; -fx-background-radius: 8px; -fx-border-color: #667eea; -fx-border-radius: 8px; -fx-text-fill: #667eea; -fx-font-weight: bold; -fx-font-size: 14px; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> onShowLogin.run());

        buttonBox.getChildren().addAll(registerButton, cancelButton);

        // Login link
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label loginText = new Label("Already have an account?");
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setStyle("-fx-text-fill: #667eea; -fx-border-color: transparent; -fx-underline: false;");
        loginLink.setOnMouseEntered(e -> loginLink.setStyle("-fx-text-fill: #5a6fd8; -fx-underline: true;"));
        loginLink.setOnMouseExited(e -> loginLink.setStyle("-fx-text-fill: #667eea; -fx-underline: false;"));
        loginLink.setOnAction(e -> onShowLogin.run());
        loginBox.getChildren().addAll(loginText, loginLink);

        formContainer.getChildren().addAll(
                customerTypeSection, personalSection,
                individualDetailsContainer, companyDetailsContainer,
                accountCreationContainer, loginSection,
                errorLabel, successLabel, buttonBox, loginBox
        );

        scrollPane.setContent(formContainer);
        rightSection.getChildren().addAll(titleLabel, scrollPane);

        return rightSection;
    }

    private void toggleCustomerTypeFields() {
        boolean isIndividual = individualRadio.isSelected();
        individualDetailsContainer.setVisible(isIndividual);
        companyDetailsContainer.setVisible(!isIndividual);
    }

    private void handleRegistration() {
        // Validation
        if (firstNameField.getText().isEmpty() || surnameField.getText().isEmpty() ||
                addressField.getText().isEmpty() || usernameField.getText().isEmpty() ||
                passwordField.getText().isEmpty() || initialDepositField.getText().isEmpty()) {
            showError("Please fill in all required fields");
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showError("Passwords do not match");
            return;
        }

        if (usernameField.getText().length() < 3) {
            showError("Username must be at least 3 characters long");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(initialDepositField.getText());
            if (initialDeposit <= 0) {
                showError("Initial deposit must be greater than 0");
                return;
            }

            String branch = branchCombo.getValue().split(" - ")[0];
            String accountType = accountTypeCombo.getValue();

            // Validate account type restrictions
            if ("Investment Account".equals(accountType) && initialDeposit < 500) {
                showError("Investment account requires minimum P 500.00 opening deposit");
                return;
            }

            Customer customer;
            boolean isIndividual = individualRadio.isSelected();

            if (isIndividual) {
                // Individual customer validation
                if (idNumberField.getText().isEmpty()) {
                    showError("ID Number is required for individual customers");
                    return;
                }

                customer = loginController.registerIndividual(
                        firstNameField.getText(),
                        surnameField.getText(),
                        addressField.getText(),
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        idNumberField.getText(),
                        occupationCombo.getValue(),
                        employmentCombo.getValue()
                );
            } else {
                // Company customer validation
                if (registrationNumberField.getText().isEmpty()) {
                    showError("Registration Number is required for company customers");
                    return;
                }

                customer = loginController.registerCompany(
                        firstNameField.getText(),
                        surnameField.getText(),
                        addressField.getText(),
                        usernameField.getText(),
                        passwordField.getText(),
                        emailField.getText(),
                        registrationNumberField.getText(),
                        businessTypeCombo.getValue(),
                        contactPersonField.getText(),
                        companySizeCombo.getValue()
                );
            }

            if (customer != null) {
                // FIXED: Force account creation - customer cannot be registered without an account
                boolean accountCreated = accountController.createAccountDuringRegistration(customer, accountType, initialDeposit, branch);
                if (accountCreated) {
                    showSuccess("✅ Registration successful! Account created: " + accountType + " with initial deposit: P " + initialDeposit);
                    System.out.println("✅ Registration and account creation successful! Navigating to dashboard...");

                    // Set the current customer in login controller
                    loginController.setCurrentCustomer(customer);

                    // Delay navigation to show success message
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000); // Show success message for 2 seconds
                            javafx.application.Platform.runLater(() -> {
                                onRegistrationSuccess.run();
                            });
                        } catch (InterruptedException e) {
                            javafx.application.Platform.runLater(() -> {
                                onRegistrationSuccess.run();
                            });
                        }
                    }).start();
                } else {
                    showError("❌ Registration failed: Could not create account. Please try again.");
                    System.err.println("❌ ACCOUNT CREATION FAILED for customer: " + customer.getUsername());
                }
            } else {
                showError("Registration failed - please try again");
            }

        } catch (NumberFormatException e) {
            showError("Please enter a valid numeric amount for initial deposit");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Registration failed: " + e.getMessage());
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
        // Clear all fields
        firstNameField.clear();
        surnameField.clear();
        addressField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        idNumberField.clear();
        registrationNumberField.clear();
        contactPersonField.clear();
        initialDepositField.clear();

        // Reset combos
        occupationCombo.setValue("Select Occupation");
        employmentCombo.setValue("Select Status");
        businessTypeCombo.setValue("Select Business Type");
        companySizeCombo.setValue("Select Size");
        accountTypeCombo.setValue("Savings Account");
        branchCombo.setValue("Main Branch - Gaborone");

        // Reset radio buttons
        individualRadio.setSelected(true);
        toggleCustomerTypeFields();

        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }
}