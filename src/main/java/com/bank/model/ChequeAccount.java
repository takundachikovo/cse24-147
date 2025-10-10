package com.bank.model;

public class ChequeAccount extends Account {
    private String companyName;
    private String companyAddress;
    
    public ChequeAccount(String branch, Customer owner, String companyName, String companyAddress) {
        super(branch, owner);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }
    
    @Override
    public String getAccountType() {
        return "CHEQUE";
    }
    
    @Override
    public boolean canWithdraw() {
        return true;
    }
    
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }
}