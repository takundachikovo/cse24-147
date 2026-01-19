package bank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bankId;
    private String bankName;
    private String headquarters;
    private String contactNumber;
    private String email;
    private List<String> branches;


    public Bank() {
        this.bankId = "BANK001";
        this.bankName = "Botswana National Bank";
        this.headquarters = "Gaborone, Botswana";
        this.contactNumber = "+267 123 4567";
        this.email = "info@botswanabank.bw";
        this.branches = new ArrayList<>();
        initializeBranches();
    }

    // Parameterized constructor
    public Bank(String bankId, String bankName, String headquarters,
                String contactNumber, String email) {
        this.bankId = bankId;
        this.bankName = bankName;
        this.headquarters = headquarters;
        this.contactNumber = contactNumber;
        this.email = email;
        this.branches = new ArrayList<>();
        initializeBranches();
    }

    private void initializeBranches() {
        branches.add("Main Branch - Gaborone");
        branches.add("Francistown Branch");
        branches.add("Maun Branch");
        branches.add("Palapye Branch");
        branches.add("Serowe Branch");
        branches.add("Molepolole Branch");
        branches.add("Kanye Branch");
    }

    // Getters and Setters
    public String getBankId() { return bankId; }
    public void setBankId(String bankId) { this.bankId = bankId; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getHeadquarters() { return headquarters; }
    public void setHeadquarters(String headquarters) { this.headquarters = headquarters; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getBranches() { return new ArrayList<>(branches); }
    public void setBranches(List<String> branches) { this.branches = new ArrayList<>(branches); }

    // Business methods
    public void addBranch(String branch) {
        if (!branches.contains(branch)) {
            branches.add(branch);
        }
    }

    public boolean removeBranch(String branch) {
        return branches.remove(branch);
    }

    public List<String> getAllBranches() {
        return new ArrayList<>(branches);
    }

    public String getBankInfo() {
        return String.format(
                "🏦 %s\n📍 %s\n📞 %s\n📧 %s\n🏢 %d branches nationwide",
                bankName, headquarters, contactNumber, email, branches.size()
        );
    }

    @Override
    public String toString() {
        return bankName + " - " + headquarters;
    }
}