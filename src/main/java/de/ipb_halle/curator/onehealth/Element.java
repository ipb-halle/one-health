/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
@Entity
@Table(name="elements")
public class Element {

    public final static String[] HEADER = { "id", "type" };

    @Id
    private UUID id;

    @Column(name="type_id")
    private int typeId;

    public Element() {
    }

    public Element(UUID id, int type) {
        this.id = id;
        this.typeId = type;
    }

    public Element(int typeId) {
        this.id = UUID.randomUUID();
        this.typeId = typeId;
    }

    public UUID getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }
}
