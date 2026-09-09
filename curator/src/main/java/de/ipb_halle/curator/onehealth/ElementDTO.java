/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.metadata.ElementType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import de.ipb_halle.curator.fields.FieldDTO;

/**
 *
 * @author fblocal
 */
public class ElementDTO {

    private final UUID id;

    private final ElementType type;

    private final List<FieldDTO> fields;

    public ElementDTO(UUID id, ElementType type) {
        this.id = id;
        this.type = type;
        this.fields = new ArrayList<> ();
    }

    public ElementDTO(ElementType type) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.fields = new ArrayList<> ();
    }

    public static ElementDTO createDTO(Element element, ElementType type) {
        return new ElementDTO(element.getId(), type);
    }

    public Element createEntity() {
        return new Element(id, type.getId());
    }

    public void addField(FieldDTO field) {
        this.fields.add(field);
    }

    public void addFields(Collection<FieldDTO> fields) {
        this.fields.addAll(fields);
    }

    public UUID getId() {
        return id;
    }

    public List<FieldDTO> getFields() {
        return fields;
    }

    public ElementType getType() {
        return type;
    }
}
