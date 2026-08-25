package de.ipb_halle.curator.onehealth.conversion;

import de.ipb_halle.curator.onehealth.SampleEntity;
import de.ipb_halle.curator.onehealth.dto.SampleEntityDTO;
import org.springframework.stereotype.Component;

/**
 * Converts between {@link SampleEntity} and {@link SampleEntityDTO}.
 */
@Component
public class SampleEntityConverter {

    /**
     * Convert entity to DTO.
     */
    public SampleEntityDTO toDTO(SampleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new SampleEntityDTO(
            entity.getId(),
            entity.getName(),
            entity.getValue()
        );
    }

    /**
     * Convert DTO to entity.
     */
    public SampleEntity toEntity(SampleEntityDTO dto) {
        if (dto == null) {
            return null;
        }
        // Id is assigned by JPA (GenerationType.UUID), so we do not set it here.
        // If the DTO carries an existing id, use it:
        SampleEntity entity = new SampleEntity(dto.getName(), dto.getValue());
        entity.setId(dto.getId());
        return entity;
    }

    /**
     * Convert a list of entities to a list of DTOs.
     */
    public java.util.List<SampleEntityDTO> toDTOList(java.util.List<SampleEntity> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }
        return entities.stream()
            .map(this::toDTO)
            .toList();
    }
}
