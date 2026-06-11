package de.ipb_halle.server.data.dtos;

import java.util.List;
import java.util.Map;

import de.ipb_halle.server.services.interfaces.Filter;

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
