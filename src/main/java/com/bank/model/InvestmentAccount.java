package com.bank.model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; 
    private static final double MIN_OPENING_BALANCE = 500.0;
    
    public InvestmentAccount(String branch, Customer owner, double initialDeposit) {
        super(branch, owner);
        if (initialDeposit >= MIN_OPENING_BALANCE) {
            deposit(initialDeposit);
        }
    }
    
    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }
    
    @Override
    public boolean canWithdraw() {
        return true;
    }
    
    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
    
    public void applyInterest() {
        double interest = calculateInterest();
        deposit(interest);
    }
    
    public static double getMinOpeningBalance() {
        return MIN_OPENING_BALANCE;
    }
}