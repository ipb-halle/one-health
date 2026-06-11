package de.ipb_halle.server.services.interfaces;

public class Filter {
    private String property;
    private String value;
    private String matchMode;
    private String dataType;

    public Filter() {
    }

    public Filter(String property, String value, String matchMode, String dataType) {
        this.property = property;
        this.value = value;
        this.matchMode = matchMode;
        this.dataType = dataType;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
