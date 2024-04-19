package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

public class KeywordDTO extends DTO<String> {
    private String id;
    private String value;

    public KeywordDTO() {};
    public KeywordDTO(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public KeywordDTO(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
