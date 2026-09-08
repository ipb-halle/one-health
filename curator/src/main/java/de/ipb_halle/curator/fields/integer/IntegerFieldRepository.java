/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.integer;

import de.ipb_halle.curator.fields.OrderedFieldId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author fblocal
 */
@Repository
public interface IntegerFieldRepository extends JpaRepository<IntegerField, OrderedFieldId>, JpaSpecificationExecutor<IntegerField> {

    /**
     * Fetch a single IntegerField by its compound key (element_id, field_definition_id, order) using JPQL.
     */
    @Query("SELECT t FROM IntegerField t WHERE t.id.elementId = :elementId "
            + "AND t.id.fieldId = :fieldId AND t.id.order = :order")
    Optional<IntegerField> findIntegerField(UUID elementId, int fieldId, int order);

    /**
     * Fetch a list of IntegerFields for a given Entity.
     */
    @Query("SELECT t FROM IntegerField t WHERE t.id.elementId = :elementId AND "
            + "t.id.fieldId = :fieldId ORDER BY t.id.order ASC")
    List<IntegerField> findIntegerFields(UUID elementId, int fieldId);

    /**
     * Fetch all IntegerFields (supports dynamic criteria via {@link JpaSpecificationExecutor}).
     */
    List<IntegerField> findAll();
}
