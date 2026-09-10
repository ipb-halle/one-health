/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.metadata.FieldDefinitionDTO;
import java.util.Iterator;

/**
 *
 * @author fblocal
 */
public abstract class FieldDTO {
    private final IFieldId id;
    private final FieldDefinitionDTO fieldDefinition;
    private FieldDTO linkedFieldDTO;

    public FieldDTO(IFieldId id, FieldDefinitionDTO fieldDefinition) {
        this.id = id;
        this.fieldDefinition = fieldDefinition;
        this.linkedFieldDTO = null;
    }

    public IFieldId getId() {
        return this.id;
    }

    public String getFieldName() {
        return this.fieldDefinition.getName();
    }

    public FieldDefinitionDTO getFieldDefinition() {
        return this.fieldDefinition;
    }

    public abstract Object toCSVcell();

    public FieldDTO getLinkedFieldDTO() {
        return linkedFieldDTO;
    }

    public FieldDTO linkFieldDTO(FieldDTO fieldDTO) {
        linkedFieldDTO = fieldDTO;
        return this;
    }

    public Iterator<FieldDTO> iterator() {
        return new FieldDtoIterator(this);
    }
}
