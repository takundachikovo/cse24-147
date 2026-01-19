package bank.model;

import java.io.Serializable;

public class InvestmentAccount extends Account implements InterestBearing, Serializable {
    private static final long serialVersionUID = 1L;
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MIN_OPENING_BALANCE = 500.0;

    public InvestmentAccount(String accountNumber, String branch, Customer owner, double balance) {
        super(accountNumber, branch, owner, balance, "INVESTMENT");
    }

    // NEW: Constructor with Bank
    public InvestmentAccount(String accountNumber, String branch, Customer owner, double balance, Bank bank) {
        super(accountNumber, branch, owner, balance, "INVESTMENT", bank);
    }

    public static boolean isValidOpeningBalance(double amount) {
        return amount >= MIN_OPENING_BALANCE;
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public String getAccountDetails() {
        return "Investment Account - " + getFormattedBalance() + " - 5% monthly interest - Withdrawals allowed - " + getBank().getBankName();
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }

    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            deposit(interest);
        }
    }
}