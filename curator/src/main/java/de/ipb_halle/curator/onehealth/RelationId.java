/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
@Embeddable
public class RelationId {

    @Column(name = "left_id")
    private UUID leftId;

    @Column(name = "relation_id")
    private UUID relationId;

    @Column(name = "right_id")
    private UUID rightId;

    public RelationId() {
    }

    public RelationId(UUID leftId, UUID relationId, UUID rightId) {
        this.leftId = leftId;
        this.relationId = relationId;
        this.rightId = rightId;
    }

    public UUID getLeftId() {
        return leftId;
    }

    public UUID getRelationId() {
        return relationId;
    }

    public UUID getRightId() {
        return rightId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.leftId)
                + Objects.hashCode(this.relationId)
                + Objects.hashCode(this.rightId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelationId other = (RelationId) obj;
        if (!Objects.equals(this.leftId, other.leftId)) {
            return false;
        }
        if (!Objects.equals(this.relationId, other.relationId)) {
            return false;
        }
        return Objects.equals(this.rightId, other.rightId);
    }
}
