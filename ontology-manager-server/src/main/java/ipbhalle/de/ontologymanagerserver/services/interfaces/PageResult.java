package ipbhalle.de.ontologymanagerserver.services.interfaces;

public class PageResult<T> {
    private int total;
    private Iterable<T> items;

    public PageResult(int total, Iterable<T> items) {
        this.total = total;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Iterable<T> getItems() {
        return items;
    }

    public void setItems(Iterable<T> items) {
        this.items = items;
    }
}
