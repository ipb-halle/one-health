package ipbhalle.de.ontologymanagerserver.data.dtos;

public class NaturalProductDTO {
    private String inChI;
    private String inChIKey;
    private String smiles;

    public NaturalProductDTO(){}

    public NaturalProductDTO(String inChI, String inChIKey, String smiles){
        this.inChI = inChI;
        this.inChIKey = inChIKey;
        this.smiles = smiles;
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

}
