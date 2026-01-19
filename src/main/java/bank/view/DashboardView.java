package bank.view;

import bank.controller.DashboardController;
import bank.model.Account;
import bank.model.Customer;
import bank.model.IndividualCustomer;
import bank.model.CompanyCustomer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class DashboardView {
    private Scene scene;
    private DashboardController dashboardController;
    private Runnable onOpenAccount;
    private Runnable onDeposit;
    private Runnable onWithdraw;
    private Runnable onViewTransactions;
    private Runnable onLogout;

    private Label welcomeLabel;
    private Label totalBalanceLabel;
    private Label accountCountLabel;
    private VBox accountsContainer;
    private VBox profileContainer;
    private VBox dashboardContent;
    private ScrollPane scrollPane;
    private VBox mainContent;
    private boolean showingProfile = false;

    // Theme colors
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

    public DashboardView(DashboardController dashboardController, Runnable onOpenAccount,
                         Runnable onDeposit, Runnable onWithdraw, Runnable onViewTransactions,
                         Runnable onLogout) {
        this.dashboardController = dashboardController;
        this.onOpenAccount = onOpenAccount;
        this.onDeposit = onDeposit;
        this.onWithdraw = onWithdraw;
        this.onViewTransactions = onViewTransactions;
        this.onLogout = onLogout;
        createView();
    }

    private void createView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");

        VBox leftNav = createLeftNavigation();
        mainLayout.setLeft(leftNav);

        scrollPane = createCenterContent();
        mainLayout.setCenter(scrollPane);

        scene = new Scene(mainLayout, 1200, 800);
    }

    private VBox createLeftNavigation() {
        VBox leftNav = new VBox(20);
        leftNav.setPadding(new Insets(30, 20, 30, 20));
        leftNav.setPrefWidth(250);
        leftNav.setStyle("-fx-background-color: " + BACKGROUND_SECONDARY + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 0 1px 0 0;");

        VBox logoContainer = new VBox(10);
        logoContainer.setAlignment(Pos.CENTER);

        Label bankLogo = new Label("🏦");
        bankLogo.setFont(Font.font(32));
        bankLogo.setTextFill(Color.web(ACCENT_LIGHT));

        Label bankName = new Label("NeoBank");
        bankName.setFont(Font.font("System", FontWeight.BOLD, 20));
        bankName.setTextFill(Color.web(TEXT_PRIMARY));

        logoContainer.getChildren().addAll(bankLogo, bankName);

        welcomeLabel = new Label();
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        welcomeLabel.setTextFill(Color.web(TEXT_SECONDARY));
        welcomeLabel.setWrapText(true);

        VBox navItems = new VBox(10);

        Button dashboardBtn = createNavButton("📊 Dashboard", true);
        dashboardBtn.setOnAction(e -> showDashboard());

        Button profileBtn = createNavButton("👤 My Profile", false);
        profileBtn.setOnAction(e -> showProfile());

        navItems.getChildren().addAll(dashboardBtn, profileBtn);

        Label quickActionsLabel = new Label("Quick Actions");
        quickActionsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        quickActionsLabel.setTextFill(Color.web(TEXT_PRIMARY));

        VBox quickActions = new VBox(8);

        Button openAccountBtn = createQuickActionButton("📝 Open Account");
        openAccountBtn.setOnAction(e -> {
            System.out.println("📝 Open Account button clicked");
            onOpenAccount.run();
        });

        Button depositBtn = createQuickActionButton("💰 Make Deposit");
        depositBtn.setOnAction(e -> {
            System.out.println("💰 Deposit button clicked");
            onDeposit.run();
        });

        Button withdrawBtn = createQuickActionButton("💸 Make Withdrawal");
        withdrawBtn.setOnAction(e -> {
            System.out.println("💸 Withdrawal button clicked");
            onWithdraw.run();
        });

        Button transactionsBtn = createQuickActionButton("📊 View Transactions");
        transactionsBtn.setOnAction(e -> {
            System.out.println("📊 Transactions button clicked");
            onViewTransactions.run();
        });

        quickActions.getChildren().addAll(openAccountBtn, depositBtn, withdrawBtn, transactionsBtn);

        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-text-fill: #ff5252; -fx-border-color: #ff5252; -fx-border-radius: 8px; -fx-padding: 12px 16px; -fx-cursor: hand; -fx-font-weight: bold;");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> onLogout.run());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #ff5252; -fx-text-fill: white; -fx-border-color: #ff5252; -fx-border-radius: 8px; -fx-padding: 12px 16px; -fx-cursor: hand; -fx-font-weight: bold;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-text-fill: #ff5252; -fx-border-color: #ff5252; -fx-border-radius: 8px; -fx-padding: 12px 16px; -fx-cursor: hand; -fx-font-weight: bold;"));

        leftNav.getChildren().addAll(
                logoContainer, welcomeLabel, createSeparator(),
                navItems, createSeparator(),
                quickActionsLabel, quickActions, createSeparator(),
                logoutBtn
        );
        return leftNav;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: " + BORDER_COLOR + ";");
        return separator;
    }

    private Button createNavButton(String text, boolean active) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        if (active) {
            button.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand; -fx-font-weight: bold;");
        } else {
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand;");
        }
        button.setOnMouseEntered(e -> {
            if (!active) {
                button.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand;");
            }
        });
        button.setOnMouseExited(e -> {
            if (!active) {
                button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand;");
            }
        });
        return button;
    }

    private Button createQuickActionButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-text-fill: " + ACCENT_LIGHT + "; -fx-font-size: 13px; -fx-padding: 10px 12px; -fx-border-radius: 6px; -fx-cursor: hand; -fx-border-color: " + BORDER_COLOR + ";");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 10px 12px; -fx-border-radius: 6px; -fx-cursor: hand; -fx-border-color: " + ACCENT_COLOR + ";"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-text-fill: " + ACCENT_LIGHT + "; -fx-font-size: 13px; -fx-padding: 10px 12px; -fx-border-radius: 6px; -fx-cursor: hand; -fx-border-color: " + BORDER_COLOR + ";"));
        return button;
    }

    private ScrollPane createCenterContent() {
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPadding(new Insets(0));

        mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");

        dashboardContent = createDashboardContent();

        profileContainer = createProfileContent();

        mainContent.getChildren().add(dashboardContent);
        scrollPane.setContent(mainContent);

        return scrollPane;
    }

    private VBox createDashboardContent() {
        VBox dashboardContent = new VBox(20);
        dashboardContent.setPadding(new Insets(20));
        dashboardContent.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerLabel = new Label("Dashboard Overview");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        headerLabel.setTextFill(Color.web(TEXT_PRIMARY));
        header.getChildren().add(headerLabel);

        HBox balanceCard = createBalanceCard();

        VBox accountsSection = createAccountsSection();

        dashboardContent.getChildren().addAll(header, balanceCard, accountsSection);
        return dashboardContent;
    }

    private VBox createProfileContent() {
        VBox profileContent = new VBox(20);
        profileContent.setPadding(new Insets(20));
        profileContent.setStyle("-fx-background-color: " + BACKGROUND_PRIMARY + ";");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Label headerLabel = new Label("My Profile");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        headerLabel.setTextFill(Color.web(TEXT_PRIMARY));
        header.getChildren().add(headerLabel);

        VBox profileCard = new VBox(20);
        profileCard.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 12px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 0, 4);");
        profileCard.setPadding(new Insets(25));

        Label loadingLabel = new Label("Loading profile information...");
        loadingLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px;");
        profileCard.getChildren().add(loadingLabel);

        profileContent.getChildren().addAll(header, profileCard);
        return profileContent;
    }

    private VBox createProfileField(String label, String value) {
        VBox field = new VBox(5);
        field.setAlignment(Pos.CENTER_LEFT);

        Label fieldLabel = new Label(label);
        fieldLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        fieldLabel.setTextFill(Color.web(ACCENT_LIGHT));

        String displayValue = (value == null || value.trim().isEmpty()) ? "Not specified" : value;

        Label fieldValue = new Label(displayValue);
        fieldValue.setFont(Font.font("System", 14));
        fieldValue.setTextFill(Color.web(TEXT_SECONDARY));
        fieldValue.setWrapText(true);
        fieldValue.setStyle("-fx-padding: 5px 0;");

        field.getChildren().addAll(fieldLabel, fieldValue);
        return field;
    }

    private HBox createBalanceCard() {
        HBox balanceCard = new HBox(20);
        balanceCard.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 12px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 15, 0, 0, 4);");
        balanceCard.setPadding(new Insets(20));

        VBox balanceInfo = new VBox(5);
        Label balanceTitle = new Label("Total Balance");
        balanceTitle.setFont(Font.font("System", 14));
        balanceTitle.setTextFill(Color.web(TEXT_SECONDARY));

        totalBalanceLabel = new Label("P 0.00");
        totalBalanceLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        totalBalanceLabel.setTextFill(Color.web(ACCENT_LIGHT));

        balanceInfo.getChildren().addAll(balanceTitle, totalBalanceLabel);

        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER_RIGHT);

        VBox accountsStat = new VBox(5);
        accountsStat.setAlignment(Pos.CENTER);
        accountCountLabel = new Label("0");
        accountCountLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        accountCountLabel.setTextFill(Color.web(ACCENT_LIGHT));
        Label accountsLabel = new Label("Accounts");
        accountsLabel.setFont(Font.font("System", 12));
        accountsLabel.setTextFill(Color.web(TEXT_SECONDARY));
        accountsStat.getChildren().addAll(accountCountLabel, accountsLabel);

        stats.getChildren().addAll(accountsStat);

        HBox.setHgrow(balanceInfo, Priority.ALWAYS);
        balanceCard.getChildren().addAll(balanceInfo, stats);

        return balanceCard;
    }

    private VBox createAccountsSection() {
        VBox accountsSection = new VBox(15);

        HBox sectionHeader = new HBox();
        sectionHeader.setAlignment(Pos.CENTER_LEFT);

        Label sectionTitle = new Label("Your Accounts");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        sectionTitle.setTextFill(Color.web(TEXT_PRIMARY));

        HBox.setHgrow(sectionTitle, Priority.ALWAYS);
        sectionHeader.getChildren().add(sectionTitle);

        accountsContainer = new VBox(10);
        updateAccountsDisplay();

        accountsSection.getChildren().addAll(sectionHeader, accountsContainer);
        return accountsSection;
    }

    private void updateAccountsDisplay() {
        accountsContainer.getChildren().clear();

        List<Account> accounts = dashboardController.getCustomerAccounts();

        if (accounts.isEmpty()) {
            VBox noAccountsCard = new VBox(10);
            noAccountsCard.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-padding: 20; -fx-alignment: center;");

            Label noAccountsIcon = new Label("💰");
            noAccountsIcon.setFont(Font.font(32));
            noAccountsIcon.setTextFill(Color.web(ACCENT_LIGHT));

            Label noAccountsLabel = new Label("You don't have any accounts yet.");
            noAccountsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            noAccountsLabel.setTextFill(Color.web(TEXT_PRIMARY));

            Label noAccountsSubtitle = new Label("Use the 'Open Account' button in the left navigation to create your first account!");
            noAccountsSubtitle.setFont(Font.font("System", 14));
            noAccountsSubtitle.setTextFill(Color.web(TEXT_SECONDARY));
            noAccountsSubtitle.setWrapText(true);

            noAccountsCard.getChildren().addAll(noAccountsIcon, noAccountsLabel, noAccountsSubtitle);
            accountsContainer.getChildren().add(noAccountsCard);
        } else {
            // Add account count
            Label accountCount = new Label("Total Accounts: " + accounts.size());
            accountCount.setFont(Font.font("System", FontWeight.BOLD, 14));
            accountCount.setTextFill(Color.web(TEXT_SECONDARY));
            accountsContainer.getChildren().add(accountCount);

            for (Account account : accounts) {
                HBox accountCard = createAccountCard(account);
                accountsContainer.getChildren().add(accountCard);
            }
        }
    }

    private HBox createAccountCard(Account account) {
        HBox accountCard = new HBox(15);
        accountCard.setStyle("-fx-background-color: " + BACKGROUND_CARD + "; -fx-background-radius: 8px; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 2);");
        accountCard.setPadding(new Insets(15));
        accountCard.setAlignment(Pos.CENTER_LEFT);

        Label accountIcon = new Label(getAccountIcon(account.getAccountType()));
        accountIcon.setFont(Font.font(24));
        accountIcon.setTextFill(getAccountColor(account.getAccountType()));

        VBox accountInfo = new VBox(5);
        Label accountType = new Label(account.getAccountType());
        accountType.setFont(Font.font("System", FontWeight.BOLD, 16));
        accountType.setTextFill(Color.web(TEXT_PRIMARY));

        Label accountNumber = new Label(account.getAccountNumber());
        accountNumber.setFont(Font.font("System", 12));
        accountNumber.setTextFill(Color.web(TEXT_SECONDARY));

        Label accountDetails = new Label(account.getAccountDetails());
        accountDetails.setFont(Font.font("System", 12));
        accountDetails.setTextFill(Color.web(ACCENT_LIGHT));

        accountInfo.getChildren().addAll(accountType, accountNumber, accountDetails);

        Label balanceLabel = new Label(account.getFormattedBalance());
        balanceLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        balanceLabel.setTextFill(Color.web(SUCCESS_COLOR));

        HBox.setHgrow(accountInfo, Priority.ALWAYS);
        accountCard.getChildren().addAll(accountIcon, accountInfo, balanceLabel);

        return accountCard;
    }

    private String getAccountIcon(String accountType) {
        return switch (accountType) {
            case "SAVINGS" -> "💰";
            case "INVESTMENT" -> "📈";
            case "CHEQUE" -> "💳";
            default -> "🏦";
        };
    }

    private Color getAccountColor(String accountType) {
        return switch (accountType) {
            case "SAVINGS" -> Color.web(SUCCESS_COLOR); // Green
            case "INVESTMENT" -> Color.web(WARNING_COLOR); // Orange
            case "CHEQUE" -> Color.web(INFO_COLOR); // Blue
            default -> Color.web(ACCENT_LIGHT); // Purple
        };
    }

    private void showDashboard() {
        showingProfile = false;

        mainContent.getChildren().clear();
        mainContent.getChildren().add(dashboardContent);

        updateNavButtons(true, false);

        scrollPane.setVvalue(0);

        refresh();
    }

    private void showProfile() {
        showingProfile = true;

        mainContent.getChildren().clear();
        mainContent.getChildren().add(profileContainer);

        updateNavButtons(false, true);

        scrollPane.setVvalue(0);

        refreshProfile();
    }

    private void updateNavButtons(boolean dashboardActive, boolean profileActive) {
        VBox leftNav = (VBox) ((BorderPane) scene.getRoot()).getLeft();
        VBox navItems = (VBox) leftNav.getChildren().get(3);

        Button dashboardBtn = (Button) navItems.getChildren().get(0);
        Button profileBtn = (Button) navItems.getChildren().get(1);

        dashboardBtn.setStyle(dashboardActive ?
                "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand; -fx-font-weight: bold;" :
                "-fx-background-color: transparent; -fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand;");

        profileBtn.setStyle(profileActive ?
                "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand; -fx-font-weight: bold;" :
                "-fx-background-color: transparent; -fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px; -fx-padding: 12px 16px; -fx-border-radius: 8px; -fx-cursor: hand;");
    }

    private void refreshProfile() {
        Customer customer = dashboardController.getCurrentCustomer();
        if (customer != null) {
            System.out.println("👤 Refreshing profile for: " + customer.getFirstName() + " " + customer.getSurname());
            System.out.println("📧 Email: " + customer.getEmail());
            System.out.println("🏠 Address: " + customer.getAddress());
            System.out.println("🏢 Customer Type: " + customer.getCustomerType());

            VBox profileCard = (VBox) profileContainer.getChildren().get(1);
            profileCard.getChildren().clear();

            // Personal Information Section
            VBox personalInfoSection = new VBox(15);
            Label personalTitle = new Label("👤 Personal Information");
            personalTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
            personalTitle.setTextFill(Color.web(TEXT_PRIMARY));

            VBox personalFields = new VBox(10);
            personalFields.getChildren().addAll(
                    createProfileField("Full Name", customer.getFirstName() + " " + customer.getSurname()),
                    createProfileField("Customer ID", customer.getCustomerId()),
                    createProfileField("Customer Type", customer.getCustomerType()),
                    createProfileField("Username", customer.getUsername()),
                    createProfileField("Email", customer.getEmail()),
                    createProfileField("Address", customer.getAddress())
            );

            personalInfoSection.getChildren().addAll(personalTitle, personalFields);

            // Customer Type Specific Information
            VBox specificInfoSection = new VBox(15);

            if (customer instanceof IndividualCustomer) {
                IndividualCustomer individual = (IndividualCustomer) customer;
                Label specificTitle = new Label("🆔 Individual Details");
                specificTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
                specificTitle.setTextFill(Color.web(TEXT_PRIMARY));

                VBox specificFields = new VBox(10);
                specificFields.getChildren().addAll(
                        createProfileField("ID Number", individual.getIdNumber()),
                        createProfileField("Occupation", individual.getOccupation()),
                        createProfileField("Employment Status", individual.getEmploymentStatus())
                );
                specificInfoSection.getChildren().addAll(specificTitle, specificFields);

            } else if (customer instanceof CompanyCustomer) {
                CompanyCustomer company = (CompanyCustomer) customer;
                Label specificTitle = new Label("🏢 Company Details");
                specificTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
                specificTitle.setTextFill(Color.web(TEXT_PRIMARY));

                VBox specificFields = new VBox(10);
                specificFields.getChildren().addAll(
                        createProfileField("Registration Number", company.getRegistrationNumber()),
                        createProfileField("Business Type", company.getBusinessType()),
                        createProfileField("Contact Person", company.getContactPerson()),
                        createProfileField("Company Size", company.getCompanySize())
                );
                specificInfoSection.getChildren().addAll(specificTitle, specificFields);
            }

            Separator separator = new Separator();
            separator.setStyle("-fx-background-color: " + BORDER_COLOR + ";");

            profileCard.getChildren().addAll(
                    personalInfoSection,
                    separator,
                    specificInfoSection
            );

            System.out.println("✅ Profile content populated successfully with customer type: " + customer.getCustomerType());
        } else {
            System.out.println("❌ No customer data available for profile");
            VBox profileCard = (VBox) profileContainer.getChildren().get(1);
            profileCard.getChildren().clear();

            Label noDataLabel = new Label("No customer data available");
            noDataLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + "; -fx-font-size: 14px; -fx-padding: 20px;");
            profileCard.getChildren().add(noDataLabel);
        }
    }

    public void refresh() {
        var customer = dashboardController.getCurrentCustomer();
        if (customer != null) {
            welcomeLabel.setText("Welcome, " + customer.getFirstName() + "! (" + customer.getCustomerType() + ")");

            // DEBUG: Check what accounts are being retrieved
            System.out.println("🔄 Dashboard refresh - Getting accounts for customer: " + customer.getCustomerId());

            List<Account> accounts = dashboardController.getCustomerAccounts();
            double totalBalance = dashboardController.getTotalBalance();

            System.out.println("📊 Accounts retrieved: " + accounts.size());
            for (Account account : accounts) {
                System.out.println("   ✅ " + account.getAccountNumber() + " | " + account.getAccountType() + " | P " + account.getBalance());
            }

            totalBalanceLabel.setText(String.format("P %.2f", totalBalance));
            accountCountLabel.setText(String.valueOf(accounts.size()));

            if (!showingProfile) {
                updateAccountsDisplay();
            } else {
                refreshProfile();
            }

            System.out.println("=== Dashboard Refreshed ===");
            System.out.println("👤 Customer: " + customer.getUsername() + " (ID: " + customer.getCustomerId() + ", Type: " + customer.getCustomerType() + ")");
            System.out.println("💰 Total balance: P " + totalBalance);
            System.out.println("📊 Number of accounts: " + accounts.size());

        } else {
            System.out.println("❌ Dashboard refresh: No customer found!");
        }
    }

    public Scene getScene() {
        return scene;
    }
}