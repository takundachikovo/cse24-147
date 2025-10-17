package com.bank.view;

import com.bank.controller.LoginController;
import com.bank.model.Customer;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView extends BaseView {
    private LoginController loginController;
    private Stage primaryStage;

    public LoginView(Stage primaryStage) {
        super();
        this.primaryStage = primaryStage;
        this.loginController = new LoginController();
        initializeView();
    }

    private void initializeView() {
        
        view.getChildren().add(createHeader("Bank", "Secure Your Financial Future"));

        /
        VBox formBox = new VBox(15);
        formBox.setStyle("-fx-alignment: center;");

        Label loginLabel = new Label("Welcome Back");
        loginLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        javafx.scene.control.TextField usernameField = createTextField("Username");
        javafx.scene.control.PasswordField passwordField = createPasswordField("Password");

        Hyperlink forgotPasswordLink = new Hyperlink("Recovery Password");
        forgotPasswordLink.setStyle("-fx-text-fill: #3182ce;");

        javafx.scene.control.Button loginButton = createPrimaryButton("Login");

        // Registration section
        HBox registerBox = new HBox(5);
        registerBox.setStyle("-fx-alignment: center;");

        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #4a5568;");

        Hyperlink registerLink = new Hyperlink("Register");
        registerLink.setStyle("-fx-text-fill: #3182ce; -fx-font-weight: bold;");

        registerBox.getChildren().addAll(noAccountLabel, registerLink);

        // Security footer
        Label securityLabel = new Label("🔒 Fully Regulated & Secure");
        securityLabel.setStyle("-fx-alignment: center; -fx-text-fill: #38a169; -fx-font-size: 12px;");

        // Add all components to form
        formBox.getChildren().addAll(
                loginLabel, usernameField, passwordField, forgotPasswordLink,
                loginButton, registerBox, securityLabel
        );

        view.getChildren().add(formBox);

        // Event handlers
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));

        registerLink.setOnAction(e -> {
            CustomerRegistrationView registrationView = new CustomerRegistrationView(primaryStage, loginController);
            primaryStage.getScene().setRoot(registrationView.getView());
        });

        forgotPasswordLink.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Password Recovery",
                    "Please contact bank support for password recovery.");
        });
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter both username and password.");
            return;
        }

        Customer customer = loginController.login(username, password);
        if (customer != null) {
            DashboardView dashboardView = new DashboardView(primaryStage, loginController);
            primaryStage.getScene().setRoot(dashboardView.getView());
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
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
