package ipbhalle.de.ontologymanagerserver.services.interfaces;

import java.util.Dictionary;
import java.util.List;

public class QueryCommand {
    private Integer first;
    private Integer rows;
    private Integer page;
    private String sortField;
    private Integer sortOrder;
    private List<Filter> filters;

    public QueryCommand() {
    }

    public QueryCommand(Integer first, Integer rows, Integer page, String sortField, Integer sortOrder, List<Filter> filters) {
        this.first = first;
        this.rows = rows;
        this.page = page;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
        this.filters = filters;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
