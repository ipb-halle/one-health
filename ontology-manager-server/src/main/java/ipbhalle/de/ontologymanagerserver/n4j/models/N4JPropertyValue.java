package ipbhalle.de.ontologymanagerserver.n4j.models;

import java.util.Map;

public class N4JPropertyValue {
    private String name;
    private Object value;

    public N4JPropertyValue(){}

    public N4JPropertyValue(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public N4JPropertyValue(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.value = map.get("value");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
