package de.ipb_halle.server.data.dtos;

public class NaturalProductDTO {
    private String id;
    private String inChI;
    private String inChIKey;
    private String smiles;
    private String molecularFormula;
    private double molecularWeight;
    private String casRegistryNumber;
    private String iupacName;
    private String name;

    public NaturalProductDTO(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInChI() {
        return inChI;
    }

    public void setInChI(String inChI) {
        this.inChI = inChI;
    }

    public String getInChIKey() {
        return inChIKey;
    }

    public void setInChIKey(String inChIKey) {
        this.inChIKey = inChIKey;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public double getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getCasRegistryNumber() {
        return casRegistryNumber;
    }

    public void setCasRegistryNumber(String casRegistryNumber) {
        this.casRegistryNumber = casRegistryNumber;
    }

    public String getIupacName() {
        return iupacName;
    }

    public void setIupacName(String iupacName) {
        this.iupacName = iupacName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
