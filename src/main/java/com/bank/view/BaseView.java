package com.bank.view;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public abstract class BaseView {
    protected VBox view;
    protected ScrollPane scrollPane;

    public BaseView() {
        // Create scroll pane
        this.scrollPane = new ScrollPane();
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setFitToHeight(true);
        this.scrollPane.setStyle("-fx-background-color: #f7fafc; -fx-border-color: transparent;");
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setPrefSize(1200, 800);

        // Create main view container
        this.view = new VBox(20);
        this.view.setStyle("-fx-padding: 20; -fx-background-color: #f7fafc;");
        VBox.setVgrow(this.view, Priority.ALWAYS);

        // scroll pane
        this.scrollPane.setContent(this.view);
    }

    protected Node createHeader(String title, String subtitle) {
        VBox header = new VBox(10);
        header.setStyle("-fx-alignment: center;");

        javafx.scene.control.Label titleLabel = new javafx.scene.control.Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1a365d;");

        javafx.scene.control.Label subtitleLabel = new javafx.scene.control.Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4a5568;");

        header.getChildren().addAll(titleLabel, subtitleLabel);
        return header;
    }

    protected javafx.scene.control.TextField createTextField(String prompt) {
        javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
        textField.setPromptText(prompt);
        textField.setStyle("-fx-pref-width: 300px; -fx-pref-height: 40px; -fx-font-size: 14px;");
        return textField;
    }

    protected javafx.scene.control.PasswordField createPasswordField(String prompt) {
        javafx.scene.control.PasswordField passwordField = new javafx.scene.control.PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setStyle("-fx-pref-width: 300px; -fx-pref-height: 40px; -fx-font-size: 14px;");
        return passwordField;
    }

    protected javafx.scene.control.Button createPrimaryButton(String text) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setStyle("-fx-pref-width: 300px; " +
                "-fx-pref-height: 40px; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;" +
                "-fx-background-color: #3182ce;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 5px;");
        return button;
    }

    protected javafx.scene.control.Button createSecondaryButton(String text) {
        javafx.scene.control.Button button = new javafx.scene.control.Button(text);
        button.setStyle("-fx-pref-width: 300px; " +
                "-fx-pref-height: 40px; " +
                "-fx-font-size: 14px;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #3182ce;" +
                "-fx-border-color: #3182ce;" +
                "-fx-border-width: 1px;" +
                "-fx-background-radius: 5px;" +
                "-fx-border-radius: 5px;");
        return button;
    }

    public ScrollPane getView() {
        return scrollPane;
    }
}