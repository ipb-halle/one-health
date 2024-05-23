package ipbhalle.de.ontologymanagerserver.n4j.mapping;

import ipbhalle.de.ontologymanagerserver.data.dtos.*;
import ipbhalle.de.ontologymanagerserver.n4j.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface N4JMapper {
    N4JMapper MAPPER = Mappers.getMapper(N4JMapper.class);

    // DTO -> Model

    // Entity type
    default N4JEntityType map(EntityTypeDTO dto){
        if (dto == null)
            return null;

        N4JEntityType entity = new N4JEntityType();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPluralName(dto.getPluralName());
        entity.setDescription(dto.getDescription());
        entity.setParent(map(dto.getParent()));
        entity.setColor(dto.getColor());

        if (dto.getKeywords() != null){
            var keywords = dto.getKeywords().stream().map(this::map).collect(Collectors.toSet());
            entity.setKeywords(keywords);
        }


        if (dto.getProperties() != null){
            var properties = dto.getProperties().stream().filter(p -> !p.getInherited()).map(p -> new N4JPropertyInfo(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getKey(),
                    p.getDataType(),
                    p.getPosition()
            )).collect(Collectors.toSet());
            entity.setProperties(properties);

            var inheritedProperties = dto.getProperties().stream().filter(p -> p.getInherited()).map(p -> new N4JPropertyInfo(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    p.getKey(),
                    p.getDataType(),
                    p.getPosition()
            )).collect(Collectors.toSet());
            entity.setInheritedProperties(inheritedProperties);
        }



        return entity;
    }

    default EntityTypeDTO map(N4JEntityType entity){
        if (entity == null)
            return null;

        EntityTypeDTO dto = new EntityTypeDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPluralName(entity.getPluralName());
        dto.setDescription(entity.getDescription());
        dto.setParent(map(entity.getParent()));
        dto.setColor(entity.getColor());
        dto.setLabel(map(entity.getLabel()));


        var keywords = entity.getKeywords().stream().map(this::map).collect(Collectors.toSet());
        dto.setKeywords(keywords);


        var properties = Stream.concat(
                entity.getProperties().stream().map(p -> new PropertyInfoDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getKey(),
                        p.getDataType(),
                        false,
                        p.getPosition()
                )),

                entity.getInheritedProperties().stream().map(p -> new PropertyInfoDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getKey(),
                        p.getDataType(),
                        true,
                        p.getPosition()
                ))).collect(Collectors.toSet());

        dto.setProperties(properties);

        return dto;
    }

    // Link Type
    default N4JLinkType map(LinkTypeDTO dto) {
        if (dto == null)
            return null;

        N4JLinkType entity = new N4JLinkType();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDirection(dto.getDirection());
        entity.setLeftEntityType(new N4JEntityType(dto.getLeftEntityTypeId()));
        entity.setLeftCardinality(dto.getLeftCardinality());
        entity.setRightEntityType(new N4JEntityType(dto.getRightEntityTypeId()));
        entity.setRightCardinality(dto.getRightCardinality());
        entity.setDescription(dto.getDescription());

        if (dto.getKeywords() != null){
            var keywords = dto.getKeywords().stream().map(this::map).collect(Collectors.toSet());
            entity.setKeywords(keywords);
        }

        if (dto.getProperties() != null) {
            var properties = dto.getProperties().stream().map(p -> new N4JPropertyInfo(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    false,
                    p.getDataType(),
                    p.getPosition()
            )).collect(Collectors.toSet());
            entity.setProperties(properties);
        }

        return entity;
    }

    default LinkTypeDTO map(N4JLinkType entity) {
        if (entity == null)
            return null;

        LinkTypeDTO dto = new LinkTypeDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDirection(entity.getDirection());
        dto.setLeftEntityTypeId(entity.getLeftEntityType().getId());
        dto.setLeftCardinality(entity.getLeftCardinality());
        dto.setRightEntityTypeId(entity.getRightEntityType().getId());
        dto.setRightCardinality(entity.getRightCardinality());
        dto.setDescription(entity.getDescription());

        if (entity.getKeywords() != null){
            var keywords = entity.getKeywords().stream().map(this::map).collect(Collectors.toSet());
            dto.setKeywords(keywords);
        }

        if (entity.getProperties() != null) {
            var properties = entity.getProperties().stream().map(p -> new PropertyInfoDTO(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    false,
                    p.getDataType(),
                    false
            )).collect(Collectors.toSet());
            dto.setProperties(properties);
        }

        return dto;
    }

    // Metadata Element

    default MetadataElementDTO EntityTypeToElement(N4JEntityType entity){
        if (entity == null)
            return null;

        MetadataElementDTO dto = new MetadataElementDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCount(0);
        dto.setType("entity");

        var properties = Stream.concat(
                entity.getProperties().stream().map(p -> new PropertyInfoDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getKey(),
                        p.getDataType(),
                        false
                )),

                entity.getInheritedProperties().stream().map(p -> new PropertyInfoDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getKey(),
                        p.getDataType(),
                        true
                ))).collect(Collectors.toList());

        dto.setProperties(properties);
        return dto;
    }

    default MetadataElementDTO LinkTypeToElement(N4JLinkType entity){
        if (entity == null)
            return null;

        MetadataElementDTO dto = new MetadataElementDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCount(0);
        dto.setType("link");

        if (entity.getProperties() != null) {
            var properties = entity.getProperties().stream().map(p -> new PropertyInfoDTO(
                    p.getId(),
                    p.getName(),
                    p.getDescription(),
                    false,
                    p.getDataType(),
                    false
            )).collect(Collectors.toList());
            dto.setProperties(properties);
        }


        return dto;
    }

    // Data Source


    N4JKeyword map(KeywordDTO dto);

    N4JDataSource map(DataSourceDTO dto);

    N4JPropertyInfo map(PropertyInfoDTO dto);

    // Model -> DTO


    KeywordDTO map(N4JKeyword object);
    DataSourceDTO map(N4JDataSource object);
    PropertyInfoDTO map(N4JPropertyInfo object);

}
