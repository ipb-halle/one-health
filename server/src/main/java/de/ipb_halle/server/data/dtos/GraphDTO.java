package de.ipb_halle.server.data.dtos;

import java.util.List;

public class GraphDTO {
    private List<GraphNodeDTO> nodes;
    private List<GraphLinkDTO> links;

    public GraphDTO(List<GraphNodeDTO> nodes, List<GraphLinkDTO> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public List<GraphNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<GraphLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<GraphLinkDTO> links) {
        this.links = links;
    }
}
