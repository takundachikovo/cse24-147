package bank.view;

import bank.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LoginView {
    private Scene scene;
    private LoginController loginController;
    private Runnable onLoginSuccess;
    private Runnable onShowRegister;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    // Theme colors matching DashboardView
    private final String BACKGROUND_PRIMARY = "#121212";
    private final String BACKGROUND_SECONDARY = "#1e1e1e";
    private final String BACKGROUND_CARD = "#252525";
    private final String TEXT_PRIMARY = "#ffffff";
    private final String TEXT_SECONDARY = "#b0b0b0";
    private final String ACCENT_COLOR = "#9c27b0";
    private final String ACCENT_LIGHT = "#bb86fc";
    private final String BORDER_COLOR = "#333333";
    private final String SUCCESS_COLOR = "#4caf50";
    private final String WARNING_COLOR = "#ff9800";
    private final String INFO_COLOR = "#2196f3";

    public LoginView(LoginController loginController, Runnable onLoginSuccess, Runnable onShowRegister) {
        this.loginController = loginController;
        this.onLoginSuccess = onLoginSuccess;
        this.onShowRegister = onShowRegister;
        createView();
    }

    private void createView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");

        VBox leftSection = createLeftSection();
        mainLayout.setLeft(leftSection);

        VBox rightSection = createRightSection();
        mainLayout.setCenter(rightSection);

        scene = new Scene(mainLayout, 1000, 600);
    }

    private VBox createLeftSection() {
        VBox leftSection = new VBox(30);
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setPadding(new Insets(40));
        leftSection.setPrefWidth(400);
        leftSection.setStyle("-fx-background-color: " + BACKGROUND_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 1px 0 0;");

        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);

        Label bankLogo = new Label("🏦");
        bankLogo.setFont(Font.font(48));
        bankLogo.setTextFill(Color.web(ACCENT_LIGHT));

        Label bankName = new Label("NeoBank");
        bankName.setFont(Font.font("System", FontWeight.BOLD, 32));
        bankName.setTextFill(Color.web(TEXT_PRIMARY));

        Label bankTagline = new Label("Secure Banking Solutions");
        bankTagline.setFont(Font.font("System", 16));
        bankTagline.setTextFill(Color.web(TEXT_SECONDARY));

        logoContainer.getChildren().addAll(bankLogo, bankName, bankTagline);

        VBox featuresContainer = new VBox(15);
        featuresContainer.setAlignment(Pos.CENTER_LEFT);

        Label featuresTitle = new Label("Why Bank With Us?");
        featuresTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        featuresTitle.setTextFill(Color.web(TEXT_PRIMARY));

        VBox featureList = new VBox(10);

        Label feature1 = createFeatureItem("💰", "Savings Accounts with 0.05% interest");
        Label feature2 = createFeatureItem("📈", "Investment Accounts with 5% returns");
        Label feature3 = createFeatureItem("💳", "Cheque Accounts for salary deposits");
        Label feature4 = createFeatureItem("🔒", "Secure & Protected Banking");

        featureList.getChildren().addAll(feature1, feature2, feature3, feature4);
        featuresContainer.getChildren().addAll(featuresTitle, featureList);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + BORDER_COLOR + ";");

        leftSection.getChildren().addAll(logoContainer, separator, featuresContainer);
        return leftSection;
    }

    private Label createFeatureItem(String emoji, String text) {
        HBox featureBox = new HBox(10);
        featureBox.setAlignment(Pos.CENTER_LEFT);

        Label emojiLabel = new Label(emoji);
        emojiLabel.setFont(Font.font(16));
        emojiLabel.setTextFill(Color.web(ACCENT_LIGHT));

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("System", 14));
        textLabel.setTextFill(Color.web(TEXT_SECONDARY));
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
        rightSection.setMaxWidth(400);

        // Welcome section
        VBox welcomeContainer = new VBox(10);
        welcomeContainer.setAlignment(Pos.CENTER);

        Label welcomeTitle = new Label("Welcome Back");
        welcomeTitle.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeTitle.setTextFill(Color.web(TEXT_PRIMARY));

        Label welcomeSubtitle = new Label("Sign in to your account to continue");
        welcomeSubtitle.setFont(Font.font("System", 16));
        welcomeSubtitle.setTextFill(Color.web(TEXT_SECONDARY));

        welcomeContainer.getChildren().addAll(welcomeTitle, welcomeSubtitle);

        // Login form
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);

        // Username field
        VBox usernameBox = new VBox(5);
        usernameBox.setAlignment(Pos.CENTER_LEFT);

        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        usernameLabel.setTextFill(Color.web(TEXT_SECONDARY));

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
        usernameField.setFocusTraversable(true);

        // Add hover effect to username field
        usernameField.setOnMouseEntered(e -> usernameField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + ACCENT_LIGHT + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";"));
        usernameField.setOnMouseExited(e -> usernameField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";"));
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                usernameField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + ACCENT_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
            } else {
                usernameField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
            }
        });

        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password field
        VBox passwordBox = new VBox(5);
        passwordBox.setAlignment(Pos.CENTER_LEFT);

        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        passwordLabel.setTextFill(Color.web(TEXT_SECONDARY));

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
        passwordField.setFocusTraversable(true);

        // Add hover effect to password field
        passwordField.setOnMouseEntered(e -> passwordField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + ACCENT_LIGHT + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";"));
        passwordField.setOnMouseExited(e -> passwordField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";"));
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + ACCENT_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
            } else {
                passwordField.setStyle("-fx-pref-height: 45px; -fx-padding: 0 15px; -fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-text-fill: " + TEXT_PRIMARY + ";");
            }
        });

        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Error label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#ff5252"));
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 8px; -fx-background-color: rgba(255, 82, 82, 0.1); -fx-background-radius: 6px;");

        // Login button
        Button loginButton = new Button("Sign In");
        loginButton.setStyle("-fx-pref-height: 50px; -fx-background-color: " + ACCENT_COLOR + "; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> handleLogin());

        // Add hover effect to login button
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-pref-height: 50px; -fx-background-color: " + ACCENT_LIGHT + "; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(156, 39, 176, 0.3), 10, 0, 0, 2);"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-pref-height: 50px; -fx-background-color: " + ACCENT_COLOR + "; -fx-background-radius: 8px; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-cursor: hand;"));

        // Register link
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);

        Label registerText = new Label("Don't have an account?");
        registerText.setFont(Font.font("System", 14));
        registerText.setTextFill(Color.web(TEXT_SECONDARY));

        Hyperlink registerLink = new Hyperlink("Register Account");
        registerLink.setStyle("-fx-text-fill: " + ACCENT_LIGHT + "; -fx-border-color: transparent; -fx-underline: false; -fx-font-size: 14px; -fx-font-weight: bold;");
        registerLink.setOnMouseEntered(e -> registerLink.setStyle("-fx-text-fill: " + ACCENT_COLOR + "; -fx-underline: true; -fx-font-size: 14px; -fx-font-weight: bold;"));
        registerLink.setOnMouseExited(e -> registerLink.setStyle("-fx-text-fill: " + ACCENT_LIGHT + "; -fx-underline: false; -fx-font-size: 14px; -fx-font-weight: bold;"));
        registerLink.setOnAction(e -> onShowRegister.run());

        registerBox.getChildren().addAll(registerText, registerLink);

        formContainer.getChildren().addAll(
                usernameBox, passwordBox, errorLabel, loginButton, registerBox
        );

        rightSection.getChildren().addAll(welcomeContainer, formContainer);
        return rightSection;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        try {
            if (loginController.login(username, password) != null) {
                System.out.println("✅ Login successful for: " + username);
                onLoginSuccess.run();
            } else {
                showError("Invalid username or password");
            }
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public Scene getScene() {
        return scene;
    }

    public void clearForm() {
        usernameField.clear();
        passwordField.clear();
        errorLabel.setVisible(false);
    }

    public void focusUsernameField() {
        usernameField.requestFocus();
    }
}