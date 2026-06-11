package de.ipb_halle.server.data.query;

public class NaturalProductStructureQuery {
    private StructureComparisonMethods method;
    private int limit;
    private String inChI;
    private String smiles;

    public NaturalProductStructureQuery(){}

    public NaturalProductStructureQuery(StructureComparisonMethods method, int limit, String inChI, String smiles){
        this.method = method;
        this.limit = limit;
        this.inChI = inChI;
        this.smiles = smiles;
    }


    public StructureComparisonMethods getMethod() {
        return method;
    }

    public void setMethod(StructureComparisonMethods method) {
        this.method = method;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getInChI() {
        return inChI;
    }

    public void setInChI(String inChI) {
        this.inChI = inChI;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }
}
