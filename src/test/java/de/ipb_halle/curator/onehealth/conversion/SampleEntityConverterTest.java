package de.ipb_halle.curator.onehealth.conversion;

import de.ipb_halle.curator.onehealth.SampleEntity;
import de.ipb_halle.curator.onehealth.dto.SampleEntityDTO;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SampleEntityConverterTest {

    private final SampleEntityConverter converter = new SampleEntityConverter();

    @Test
    void toDTO() {
        UUID testId = UUID.randomUUID();
        SampleEntity entity = new SampleEntity("Test", 10);
        // Simulate persistence setting the id
        entity.setId(testId);

        SampleEntityDTO dto = converter.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(testId);
        assertThat(dto.getName()).isEqualTo("Test");
        assertThat(dto.getValue()).isEqualTo(10);
    }

    @Test
    void toDTO_nullInput() {
        SampleEntityDTO dto = converter.toDTO(null);
        assertThat(dto).isNull();
    }

    @Test
    void toEntity() {
        SampleEntityDTO dto = new SampleEntityDTO(UUID.randomUUID(), "Test", 10);

        SampleEntity entity = converter.toEntity(dto);

        assertThat(entity.getName()).isEqualTo("Test");
        assertThat(entity.getValue()).isEqualTo(10);
    }

    @Test
    void toEntity_nullInput() {
        SampleEntity entity = converter.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    void toDTOList() {
        SampleEntity e1 = new SampleEntity("A", 1);
        SampleEntity e2 = new SampleEntity("B", 2);
        e1.setId(UUID.randomUUID());
        e2.setId(UUID.randomUUID());

        List<SampleEntityDTO> dtos = converter.toDTOList(List.of(e1, e2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getName()).isEqualTo("A");
        assertThat(dtos.get(1).getName()).isEqualTo("B");
    }

    @Test
    void toDTOList_nullInput() {
        List<SampleEntityDTO> dtos = converter.toDTOList(null);
        assertThat(dtos).isEmpty();
    }
}
