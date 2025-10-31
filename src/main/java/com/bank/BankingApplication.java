package com.bank;

import com.bank.dao.TextFileDatabase;
import com.bank.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("🚀 Starting Banking Application...");

        TextFileDatabase.initializeSampleData();
        TextFileDatabase.viewAllData();

        LoginView loginView = new LoginView(primaryStage);
        Scene scene = new Scene(loginView.getView(), 1200, 800);

        primaryStage.setTitle("Bank - Secure Your Financial Future");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

        System.out.println("✅ Application started successfully");
        System.out.println("💡 You can view the data files in: ./bank_data/");
    }

    public static void main(String[] args) {
        launch(args);
    }
}