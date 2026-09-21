/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.metadata.ElementType;
import java.util.Collection;
import java.util.UUID;
import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.MultiValueFieldDTO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fblocal
 */
public class ElementDTO {

    private final UUID id;

    private final ElementType type;

    private final Map<String, AbstractField> fields;

    public ElementDTO(UUID id, ElementType type) {
        this.id = id;
        this.type = type;
        this.fields = new HashMap<> ();
    }

    public ElementDTO(ElementType type) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.fields = new HashMap<> ();
    }

    public static ElementDTO createDTO(ElementEntity element, ElementType type) {
        return new ElementDTO(element.getId(), type);
    }

    public ElementEntity createEntity() {
        return new ElementEntity(id, type.getId());
    }

    public void addField(AbstractField field) {
        String key = field.getFieldName();
        boolean multivalued = field.getFieldDefinition().isMultivalued();
        if(fields.containsKey(key)) {
            if (multivalued) {
                ((MultiValueFieldDTO) fields.get(key)).addValue(field);
                return;
            }
            throw new UnsupportedOperationException("Multiple values for singleton field");
        } else {
            if (multivalued) {
                fields.put(key, new MultiValueFieldDTO(field));
            } else {
                fields.put(key, field);
            }
        }
    }

    public void addFields(Collection<AbstractField> fields) {
        fields.stream().forEach(f -> addField(f));
    }

    public UUID getId() {
        return id;
    }

    public AbstractField getField(String fieldName) {
        return fields.get(fieldName);
    }

    public Collection<AbstractField> getFields() {
        return fields.values();
    }

    public ElementType getType() {
        return type;
    }
}
