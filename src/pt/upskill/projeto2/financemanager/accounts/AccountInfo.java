package pt.upskill.projeto2.financemanager.accounts;

public class AccountInfo {
    private long id;
    private String currency;
    private String name;
    private String type;
    private String additionalInfo;

    public AccountInfo(long id, String currency, String name, String type, String additionalInfo) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.type = type;
        this.additionalInfo = additionalInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
