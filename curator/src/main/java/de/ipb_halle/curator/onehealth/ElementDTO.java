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
import de.ipb_halle.curator.fields.FieldDTO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fblocal
 */
public class ElementDTO {

    private final UUID id;

    private final ElementType type;

    // private final List<FieldDTO> fields;
    private final Map<String, FieldDTO> fields;

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

    public static ElementDTO createDTO(Element element, ElementType type) {
        return new ElementDTO(element.getId(), type);
    }

    public Element createEntity() {
        return new Element(id, type.getId());
    }

    public void addField(FieldDTO field) {
        String key = field.getFieldName();
        if(fields.containsKey(key)) {
            field.linkFieldDTO(fields.get(key));

        }
        fields.put(key, field);
    }

    public void addFields(Collection<FieldDTO> fields) {
        fields.stream().forEach(f -> addField(f));
    }

    public UUID getId() {
        return id;
    }

    public FieldDTO getField(String fieldName) {
        return fields.get(fieldName);
    }

    public Collection<FieldDTO> getFields() {
        return fields.values();
    }

    public ElementType getType() {
        return type;
    }
}
