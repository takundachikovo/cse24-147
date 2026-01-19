package bank.model;

import java.io.Serializable;

public class CompanyCustomer extends Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String registrationNumber;
    private String businessType;
    private String contactPerson;
    private String companySize;

    public CompanyCustomer(String customerId, String firstName, String surname, String address,
                           String username, String password, String email,
                           String registrationNumber, String businessType, String contactPerson, String companySize) {
        super(customerId, firstName, surname, address, username, password, email, "COMPANY");
        this.registrationNumber = registrationNumber;
        this.businessType = businessType;
        this.contactPerson = contactPerson;
        this.companySize = companySize;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public String getBusinessType() { return businessType; }
    public String getContactPerson() { return contactPerson; }
    public String getCompanySize() { return companySize; }

    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setCompanySize(String companySize) { this.companySize = companySize; }

    @Override
    public String getDisplayInfo() {
        return "Company - " + businessType + " - " + companySize;
    }
}
