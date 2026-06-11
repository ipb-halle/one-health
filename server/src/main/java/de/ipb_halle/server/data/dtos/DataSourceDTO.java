package de.ipb_halle.server.data.dtos;


import de.ipb_halle.server.data.interfaces.DTO;


public class DataSourceDTO extends DTO<String> {
    private String id;
    private String name;
    private String path;
    private boolean uploaded;
    public DataSourceDTO() {};

    public DataSourceDTO(String id, String name, String path, boolean uploaded) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.uploaded = uploaded;
    }

    public DataSourceDTO(String name, String path, boolean uploaded) {
        this.name = name;
        this.path = path;
        this.uploaded = uploaded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}
