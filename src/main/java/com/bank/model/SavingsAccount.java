package com.bank.model;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; 
    
    public SavingsAccount(String branch, Customer owner) {
        super(branch, owner);
    }
    
    @Override
    public String getAccountType() {
        return "SAVINGS";
    }
    
    @Override
    public boolean canWithdraw() {
        return false; // No withdrawals allowed for savings
    }
    
    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
    
    public void applyInterest() {
        double interest = calculateInterest();
        deposit(interest);
    }
}