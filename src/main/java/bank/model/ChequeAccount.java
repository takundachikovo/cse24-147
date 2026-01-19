package bank.model;

import java.io.Serializable;

public class ChequeAccount extends Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private String companyName;
    private String companyAddress;

    public ChequeAccount(String accountNumber, String branch, Customer owner, double balance,
                         String companyName, String companyAddress) {
        super(accountNumber, branch, owner, balance, "CHEQUE");
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }


    public ChequeAccount(String accountNumber, String branch, Customer owner, double balance, Bank bank,
                         String companyName, String companyAddress) {
        super(accountNumber, branch, owner, balance, "CHEQUE", bank);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public String getAccountDetails() {
        return "Cheque Account - " + getFormattedBalance() + " - Salary account - No interest - " + getBank().getBankName();
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
}