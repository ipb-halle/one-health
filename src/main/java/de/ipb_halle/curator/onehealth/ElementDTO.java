/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.fields.Field;
import de.ipb_halle.curator.metadata.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
public class ElementDTO {

    private final UUID id;

    private final ElementType type;

    private final List<Field> fields;

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

    public static ElementDTO createElementDTO(Element element, ElementType type) {
        return new ElementDTO(element.getId(), type);
    }

    public Element createElement() {
        return new Element(id, type.getId());
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public UUID getId() {
        return id;
    }

    public List<Field> getFields() {
        return fields;
    }

    public ElementType getType() {
        return type;
    }
}
