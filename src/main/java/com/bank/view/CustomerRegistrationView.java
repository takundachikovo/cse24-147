package com.bank.view;

import com.bank.controller.LoginController;
import com.bank.model.Customer;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerRegistrationView extends BaseView {
    private LoginController loginController;
    private Stage primaryStage;

    public CustomerRegistrationView(Stage primaryStage, LoginController loginController) {
        super();
        this.primaryStage = primaryStage;
        this.loginController = loginController;
        initializeView();
    }

    private void initializeView() {
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f7fafc; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-padding: 20; -fx-background-color: #f7fafc;");

        
        mainContainer.getChildren().add(createHeader("Bank", "Secure Your Financial Future"));

        
        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-alignment: center; -fx-padding: 10;");

        Label registerLabel = new Label("Create Your Account");
        registerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d; -fx-padding: 10 0 5 0;");

        javafx.scene.control.TextField firstNameField = createTextField("First Name");
        javafx.scene.control.TextField surnameField = createTextField("Surname");
        javafx.scene.control.TextField addressField = createTextField("Residential Address");
        addressField.setStyle("-fx-pref-width: 300px; -fx-pref-height: 60px; -fx-font-size: 14px;");

        
        Label credentialsLabel = new Label("Account Credentials");
        credentialsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d; -fx-padding: 10 0 5 0;");

        javafx.scene.control.TextField usernameField = createTextField("Username");
        javafx.scene.control.TextField emailField = createTextField("Email Address");
        javafx.scene.control.PasswordField passwordField = createPasswordField("Password");
        javafx.scene.control.PasswordField confirmPasswordField = createPasswordField("Confirm Password");

        
        Label accountTypeLabel = new Label("Account Type");
        accountTypeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d; -fx-padding: 10 0 5 0;");

        VBox radioBox = new VBox(8);
        radioBox.setStyle("-fx-alignment: center-left; -fx-padding: 0 0 10 0;");

        RadioButton personalRadio = new RadioButton("Personal Banking");
        personalRadio.setSelected(true);
        personalRadio.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 14px;");

        RadioButton businessRadio = new RadioButton("Business Banking (Coming Soon)");
        businessRadio.setDisable(true);
        businessRadio.setStyle("-fx-text-fill: #a0aec0; -fx-font-size: 14px;");

        ToggleGroup accountTypeGroup = new ToggleGroup();
        personalRadio.setToggleGroup(accountTypeGroup);
        businessRadio.setToggleGroup(accountTypeGroup);

        radioBox.getChildren().addAll(personalRadio, businessRadio);

        
        Label termsLabel = new Label("Terms & Conditions");
        termsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1a365d; -fx-padding: 10 0 5 0;");

        CheckBox termsCheckbox = new CheckBox("I agree to the Terms of Use and Privacy Policy");
        termsCheckbox.setStyle("-fx-text-fill: #4a5568; -fx-font-size: 14px; -fx-wrap-text: true;");
        termsCheckbox.setMaxWidth(300);

        HBox termsBox = new HBox(5);
        termsBox.setStyle("-fx-alignment: center; -fx-padding: 5 0 10 0;");

        Hyperlink termsLink = new Hyperlink("Terms of Use");
        Hyperlink privacyLink = new Hyperlink("Privacy Policy");

        termsLink.setStyle("-fx-text-fill: #3182ce; -fx-font-size: 12px;");
        privacyLink.setStyle("-fx-text-fill: #3182ce; -fx-font-size: 12px;");

        termsBox.getChildren().addAll(termsLink, new Label("and"), privacyLink);

        
        VBox buttonsBox = new VBox(10);
        buttonsBox.setStyle("-fx-alignment: center; -fx-padding: 20 0 10 0;");

        javafx.scene.control.Button registerButton = createPrimaryButton("Create Account");
        javafx.scene.control.Button backButton = createSecondaryButton("Back to Login");

        buttonsBox.getChildren().addAll(registerButton, backButton);

        
        Label securityLabel = new Label("🔒 Fully Regulated & Secure");
        securityLabel.setStyle("-fx-alignment: center; -fx-text-fill: #38a169; -fx-font-size: 12px; -fx-padding: 10 0 0 0;");

        
        formBox.getChildren().addAll(
                registerLabel,

                personalInfoLabel,
                firstNameField, surnameField, addressField,

                credentialsLabel,
                usernameField, emailField, passwordField, confirmPasswordField,

                accountTypeLabel,
                radioBox,

                termsLabel,
                termsCheckbox,
                termsBox,

                buttonsBox,
                securityLabel
        );

        mainContainer.getChildren().add(formBox);

        
        VBox.setVgrow(formBox, Priority.ALWAYS);

        
        scrollPane.setContent(mainContainer);

        
        view.getChildren().clear();
        view.getChildren().add(scrollPane);
        view.setStyle("-fx-padding: 0; -fx-background-color: #f7fafc;");

        
        registerButton.setOnAction(e -> handleRegistration(
                firstNameField.getText(), surnameField.getText(), addressField.getText(),
                usernameField.getText(), emailField.getText(),
                passwordField.getText(), confirmPasswordField.getText(),
                termsCheckbox.isSelected()
        ));

        backButton.setOnAction(e -> {
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.getScene().setRoot(loginView.getView());
        });

        termsLink.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Terms of Use", "Bank terms and conditions..."));
        privacyLink.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Privacy Policy", "Bank privacy policy..."));
    }

    private void handleRegistration(String firstName, String surname, String address,
                                    String username, String email, String password,
                                    String confirmPassword, boolean acceptedTerms) {
        
        if (firstName.isEmpty() || surname.isEmpty() || address.isEmpty() ||
                username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        if (!acceptedTerms) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please accept the terms and conditions.");
            return;
        }

        
        Customer customer = new Customer(firstName, surname, address, username, password);

        
        if (loginController.register(customer)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully! Please login.");
            LoginView loginView = new LoginView(primaryStage);
            primaryStage.getScene().setRoot(loginView.getView());
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different username.");
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
