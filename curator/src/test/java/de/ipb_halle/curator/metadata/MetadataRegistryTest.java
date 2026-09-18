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
        ElementType organism = registry.getElementType("ORGANISM");

        assertThat(organism).isNotNull();
        assertThat(organism.getElementClass()).isEqualTo(ElementClass.NODE);
        assertThat(organism.getUiColor().longValue()).isEqualTo(0x297e00);
        assertThat(organism.getFieldDefinitions().size()).isEqualTo(4);
    }

    @Test
    void testFieldDefinition() {
        FieldDefinition fieldDef = registry.getFieldDefinition("ORGANISM:primary name");
        assertThat(fieldDef.getName()).isEqualTo("primary name");
        assertThat(fieldDef.getDescription()).isEqualTo("primary node name");
        assertThat(fieldDef.getKey()).isEqualTo("ORGANISM:primary name");
        assertThat(fieldDef.isMandatory()).isFalse();
        assertThat(fieldDef.isMultivalued()).isFalse();
        ElementType et = fieldDef.getElementType();
        assertThat(et.getId()).isEqualTo("ORGANISM");
        assertThat(et.getElementClass()).isEqualTo(ElementClass.NODE);
        assertThat(et.getName()).isEqualTo("Organism");
        assertThat(et.getDescription()).isEqualTo("Living cellular organism");
        FieldType ft =fieldDef.getFieldType();
        assertThat(ft).isEqualTo(FieldType.TEXT);
        assertThat(ft.getTableName()).isEqualTo("text_fields");
    }

    @Test
    void testMultiInitializationFails() {
        Assertions.assertThatThrownBy(() -> registry.initializeDynEnums(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> registry.initializeElementTypes(null))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> registry.initializeFieldDefinitions(null))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDynEnum() {
        DynEnum dynEnum = registry.getDynEnum(8, "SHRUB");
        assertThat(dynEnum.getId()).isEqualTo(2);
        assertThat(dynEnum.getFieldDefinitionId()).isEqualTo(8);
        assertThat(dynEnum.getLabel()).isEqualTo("SHRUB");
        assertThat(dynEnum.getDescription()).isEqualTo("plant growing as a shrub");
        Map<String, DynEnum> map = registry.getDynEnumsByFieldId(8);
        assertThat(map.size()).isEqualTo(3);
        assertThat(registry.getDynEnum(8, "PLANET")).isNull();
        assertThat(registry.getDynEnum(8, 999999)).isNull();
        dynEnum = new DynEnum(null, 8, "MOSS", "growth form of Bryophytes");
        registry.registerDynEnum(dynEnum);
        assertThat(registry.getDynEnum(8, "MOSS").getId()).isGreaterThan(3);

        // attempts to lookup or register illegal values result in IllegalArgumentExceptions
        Assertions.assertThatThrownBy(() -> registry.getDynEnumsByFieldId(7))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> registry.getDynEnum(1, 1))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> registry.getDynEnum(1, "PLANET"))
                .isInstanceOf(IllegalArgumentException.class);

        final DynEnum temp1 = new DynEnum(null, 7, "CAR", "Motorized vehicle");
        Assertions.assertThatThrownBy(() -> registry.registerDynEnum(temp1))
                .isInstanceOf(IllegalArgumentException.class);

        final DynEnum temp2 = new DynEnum(null, 8, "MOSS", "growth form of Bryophytes");
        Assertions.assertThatThrownBy(() -> registry.registerDynEnum(temp2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
