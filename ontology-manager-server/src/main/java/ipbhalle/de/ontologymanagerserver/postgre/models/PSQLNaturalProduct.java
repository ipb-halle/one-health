package ipbhalle.de.ontologymanagerserver.postgre.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

public class PSQLNaturalProduct {

    private String __id;
    private String inchi;
    private String inchikey;
    private String smiles;
    private String molformula;
    private double molweight;
    private String cas;
    private String iupac;
    private String name;

    public PSQLNaturalProduct(){};

    public PSQLNaturalProduct(String __id, String inchi, String inchikey, String smiles, String molformula, double molweight, String cas, String iupac, String name){
        this.__id = __id;
        this.inchi = inchi;
        this.inchikey = inchikey;
        this.smiles = smiles;
        this.molformula = molformula;
        this.molweight = molweight;
        this.cas = cas;
        this.iupac = iupac;
        this.name = name;
    }

    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public String getInchi() {
        return inchi;
    }

    public void setInchi(String inchi) {
        this.inchi = inchi;
    }

    public String getInchikey() {
        return inchikey;
    }

    public void setInchikey(String inchikey) {
        this.inchikey = inchikey;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getMolformula() {
        return molformula;
    }

    public void setMolformula(String molformula) {
        this.molformula = molformula;
    }


    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getIupac() {
        return iupac;
    }

    public void setIupac(String iupac) {
        this.iupac = iupac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMolweight() {
        return molweight;
    }

    public void setMolweight(double molweight) {
        this.molweight = molweight;
    }
}
