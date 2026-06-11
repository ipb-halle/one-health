package de.ipb_halle.server.data.dtos;

public class CoOcurrenceQuery {
    private TypeQuery leftTypeQuery;
    private TypeQuery rightTypeQuery;

    public CoOcurrenceQuery() {
    }

    public CoOcurrenceQuery(TypeQuery leftTypeQuery, TypeQuery rightTypeQuery) {
        this.leftTypeQuery = leftTypeQuery;
        this.rightTypeQuery = rightTypeQuery;
    }

    public TypeQuery getLeftTypeQuery() {
        return leftTypeQuery;
    }

    public void setLeftTypeQuery(TypeQuery leftTypeQuery) {
        this.leftTypeQuery = leftTypeQuery;
    }

    public TypeQuery getRightTypeQuery() {
        return rightTypeQuery;
    }

    public void setRightTypeQuery(TypeQuery rightTypeQuery) {
        this.rightTypeQuery = rightTypeQuery;
    }
}
