package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

public class PropertyValueDTO extends DTO<String> {
    private String id;
    private Object value;
    private Integer sources;
}
