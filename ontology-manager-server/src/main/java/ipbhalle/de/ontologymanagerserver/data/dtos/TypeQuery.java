package ipbhalle.de.ontologymanagerserver.data.dtos;

import ipbhalle.de.ontologymanagerserver.services.interfaces.Filter;

import java.util.List;
import java.util.Map;

public class TypeQuery {
    private String type;
    private List<Filter> filters;
    private String GroupBy;

    public TypeQuery() {
    }

    public TypeQuery(String type, List<Filter> filters, String groupBy) {
        this.type = type;
        this.filters = filters;
        GroupBy = groupBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public String getGroupBy() {
        return GroupBy;
    }

    public void setGroupBy(String groupBy) {
        GroupBy = groupBy;
    }
}
