package de.ipb_halle.server.services.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a chemical compound extracted from a document.
 * Mirrors the Compound model from the downstream extraction service.
 */
public class Compound {

    private String id;
    private String name;
    private String smiles;

    @JsonProperty("iupac_name")
    private String iupacName;

    private String inchi;

    @JsonProperty("inchi_key")
    private String inchiKey;

    @JsonProperty("molecular_formula")
    private String molecularFormula;

    @JsonProperty("molecular_weight")
    private Double molecularWeight;

    private List<String> synonyms;

    public Compound() {
    }

    public Compound(String id, String name, String smiles, String iupacName, String inchi,
                    String inchiKey, String molecularFormula, Double molecularWeight,
                    List<String> synonyms) {
        this.id = id;
        this.name = name;
        this.smiles = smiles;
        this.iupacName = iupacName;
        this.inchi = inchi;
        this.inchiKey = inchiKey;
        this.molecularFormula = molecularFormula;
        this.molecularWeight = molecularWeight;
        this.synonyms = synonyms;
    }

    // Getters and Setters

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

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchiKey() {
        return inchiKey;
    }

    public void setInchiKey(String inchiKey) {
        this.inchiKey = inchiKey;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(Double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }
}
