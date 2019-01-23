package de.uni_stuttgart.informatik.sopra.sopraapp.requests;

public class OidElement {

    private String oidString;
    private String description;

    OidElement(String oidString, String description) {
        this.oidString = oidString;
        this.description = description;
    }

    public String getOidString() {
        return oidString;
    }

    public void setOidString(String oidString) {
        this.oidString = oidString;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
