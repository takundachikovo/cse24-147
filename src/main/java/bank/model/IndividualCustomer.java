package bank.model;

import java.io.Serializable;

public class IndividualCustomer extends Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idNumber;
    private String occupation;
    private String employmentStatus;

    public IndividualCustomer(String customerId, String firstName, String surname, String address,
                              String username, String password, String email,
                              String idNumber, String occupation, String employmentStatus) {
        super(customerId, firstName, surname, address, username, password, email, "INDIVIDUAL");
        this.idNumber = idNumber;
        this.occupation = occupation;
        this.employmentStatus = employmentStatus;
    }

    public String getIdNumber() { return idNumber; }
    public String getOccupation() { return occupation; }
    public String getEmploymentStatus() { return employmentStatus; }

    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    @Override
    public String getDisplayInfo() {
        return "Individual - " + getOccupation() + " - " + getEmploymentStatus();
    }
}
