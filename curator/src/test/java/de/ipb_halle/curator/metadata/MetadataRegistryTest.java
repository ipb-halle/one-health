/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.TestcontainersConfiguration;
import de.ipb_halle.curator.metadata.ElementType.ElementClass;
import de.ipb_halle.curator.metadata.FieldType.FieldTypeEnum;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Import(TestcontainersConfiguration.class)
class MetadataRegistryTest {

    @Autowired
    private MetadataRegistry registry;

    @Test
    void isInitialized_afterStartup() {
        assertThat(registry.isInitialized()).isTrue();
    }

    @Test
    void getElementTypeById_returnsOrganismRecord() {
        ElementType organism = registry.getElementType(1);

        assertThat(organism).isNotNull();
        assertThat(organism.getLabel()).isEqualTo("ORGANISM");
        assertThat(organism.getUiColor().longValue()).isEqualTo(0x297e00);
        assertThat(organism.getFieldDefinitions().size()).isEqualTo(4);
    }

    @Test
    void testFieldDefinitionDTO() {
        FieldDefinitionDTO dto = registry.getFieldDefinition("ORGANISM:primary name");
        assertThat(dto.getName()).isEqualTo("primary name");
        assertThat(dto.getDescription()).isEqualTo("primary node name");
        assertThat(dto.getKey()).isEqualTo("ORGANISM:primary name");
        assertThat(dto.isMandatory()).isFalse();
        assertThat(dto.isMultivalued()).isFalse();
        ElementType et = dto.getElementType();
        assertThat(et.getLabel()).isEqualTo("ORGANISM");
        assertThat(et.getElementClass()).isEqualTo(ElementClass.NODE);
        assertThat(et.getName()).isEqualTo("Organism");
        assertThat(et.getDescription()).isEqualTo("Living cellular organism");
        FieldType ft =dto.getFieldType();
        assertThat(ft.getType()).isEqualTo(FieldTypeEnum.TEXT);
        assertThat(ft.getDescription()).isEqualTo("general text types");
        assertThat(ft.getTableName()).isEqualTo("text_fields");
    }

    @Test
    void testMultiInitializationFails() {
        Assertions.assertThatThrownBy(() -> registry.initializeDynEnums(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> registry.initializeElementTypes(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> registry.initializeFieldTypes(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> registry.initializeFieldDefinitions(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDynEnum() {
        DynEnum dynEnum = registry.getDynEnum(8, "HERB");
        assertThat(dynEnum.getId()).isEqualTo(1);
        assertThat(dynEnum.getFieldDefinitionId()).isEqualTo(8);
        assertThat(dynEnum.getLabel()).isEqualTo("HERB");
        assertThat(dynEnum.getDescription()).isEqualTo("plant growing as a herb");
        Map<String, DynEnum> map = registry.getDynEnumsByLabel(8);
        assertThat(map.size()).isEqualTo(3);

        Assertions.assertThatThrownBy(() -> registry.getDynEnumsByLabel(7))
                .isInstanceOf(IllegalArgumentException.class);

        // attempts to lookup illegal values result in IllegalArgumentException
        Assertions.assertThatThrownBy(() -> registry.getDynEnum(1, "PLANET"))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> registry.getDynEnum(1, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
