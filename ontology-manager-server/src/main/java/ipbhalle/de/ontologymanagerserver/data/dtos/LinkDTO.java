package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.data.interfaces.DTO;

import java.util.Set;

public class LinkDTO extends DTO<String> {
    private Long id;
    private EntityDTO leftEntity;
    private EntityDTO rightEntity;
    private Set<PropertyValueDTO> properties;
    private Set<DataSourceDTO> sources;
}
