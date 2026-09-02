/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
@Entity
@Table(name="relations")
public class Relation {

    public final static String[] HEADER = {"left_id", "relation_id", "right_id" };

    @EmbeddedId
    private final RelationId id;

    public Relation(UUID leftId, UUID relationId, UUID rightId) {
        id = new RelationId(leftId, relationId, rightId);
    }

    public RelationId getId() {
        return id;
    }
}
