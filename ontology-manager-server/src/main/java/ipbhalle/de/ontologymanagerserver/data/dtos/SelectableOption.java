package ipbhalle.de.ontologymanagerserver.data.dtos;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;

public class SelectableOption<T> {
    private String label;
    private T value;

    private HashMap<String, String> data;

    public SelectableOption() {
    }

    public SelectableOption(String label, T value) {
        this.label = label;
        this.value = value;
    }

    public SelectableOption(String label, T value, HashMap<String, String> data) {
        this.label = label;
        this.value = value;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
