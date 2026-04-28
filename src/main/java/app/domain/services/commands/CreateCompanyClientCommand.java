package app.domain.services.commands;

public class CreateCompanyClientCommand {
    private String clientId;
    private String companyName;
    private String taxId;
    private String contactEmail;

    public CreateCompanyClientCommand(String clientId, String companyName,
                                     String taxId, String contactEmail) {
        this.clientId = clientId;
        this.companyName = companyName;
        this.taxId = taxId;
        this.contactEmail = contactEmail;
    }

    public String getClientId() { return clientId; }
    public String getCompanyName() { return companyName; }
    public String getTaxId() { return taxId; }
    public String getContactEmail() { return contactEmail; }
}
