package de.ipb_halle.server.data.dtos;

public class MetadataSummaryDTO {

    private long EntityTypesCount;
    private long EntitiesCount;
    private long LinkTypesCount;
    private long LinkCounts;
    private long DataSourcesCount;
    private long HealthProblemsCount;

    public MetadataSummaryDTO(long entityTypesCount, long entitiesCount, long linkTypesCount, long linkCounts, long dataSourcesCount, long healthProblemsCount) {
        EntityTypesCount = entityTypesCount;
        EntitiesCount = entitiesCount;
        LinkTypesCount = linkTypesCount;
        LinkCounts = linkCounts;
        DataSourcesCount = dataSourcesCount;
        HealthProblemsCount = healthProblemsCount;
    }

    public long getEntityTypesCount() {
        return EntityTypesCount;
    }

    public void setEntityTypesCount(long entityTypesCount) {
        EntityTypesCount = entityTypesCount;
    }

    public long getEntitiesCount() {
        return EntitiesCount;
    }

    public void setEntitiesCount(long entitiesCount) {
        EntitiesCount = entitiesCount;
    }

    public long getLinkTypesCount() {
        return LinkTypesCount;
    }

    public void setLinkTypesCount(long linkTypesCount) {
        LinkTypesCount = linkTypesCount;
    }

    public long getLinkCounts() {
        return LinkCounts;
    }

    public void setLinkCounts(long linkCounts) {
        LinkCounts = linkCounts;
    }

    public long getDataSourcesCount() {
        return DataSourcesCount;
    }

    public void setDataSourcesCount(long dataSourcesCount) {
        DataSourcesCount = dataSourcesCount;
    }

    public long getHealthProblemsCount() {
        return HealthProblemsCount;
    }

    public void setHealthProblemsCount(long healthProblemsCount) {
        HealthProblemsCount = healthProblemsCount;
    }
}
