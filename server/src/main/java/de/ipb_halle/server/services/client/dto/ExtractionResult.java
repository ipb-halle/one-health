package de.ipb_halle.server.services.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents the result of extraction from the downstream service.
 * Contains a list of chemical compounds identified in the processed document.
 */
public class ExtractionResult {

    @JsonProperty("compounds")
    private List<Compound> compounds;

    public ExtractionResult() {
    }

    public ExtractionResult(List<Compound> compounds) {
        this.compounds = compounds;
    }

    // Getters and Setters

    public List<Compound> getCompounds() {
        return compounds;
    }

    public void setCompounds(List<Compound> compounds) {
        this.compounds = compounds;
    }
}
