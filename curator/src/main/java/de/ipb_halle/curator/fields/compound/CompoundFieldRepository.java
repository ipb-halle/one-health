/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.compound;

import de.ipb_halle.curator.fields.FieldEntity;
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
public interface CompoundFieldRepository extends JpaRepository<CompoundField, OrderedFieldId>, JpaSpecificationExecutor<CompoundField> {

    /**
     * Fetch a single CompoundField by its compound key (element_id, field_definition_id, order) using JPQL.
     */
    @Query("SELECT t FROM CompoundField t WHERE t.id.elementId = :elementId "
            + "AND t.id.fieldId = :fieldId")
    Optional<CompoundField> findCompoundField(UUID elementId, int fieldId);

    /**
     * Fetch a list of CompoundFields for a given Entity.
     */
    @Query("SELECT t FROM CompoundField t WHERE t.id.elementId = :elementId "
            + "ORDER BY t.id.fieldId ASC")
    List<FieldEntity> findCompoundFields(UUID elementId);

    /**
     * Fetch a list of CompoundFields for a given Entity.
     */
    @Query("SELECT t FROM CompoundField t WHERE t.id.elementId = :elementId AND "
            + "t.id.fieldId = :fieldId")
    List<CompoundField> findCompoundFields(UUID elementId, int fieldId);

    @Query("SELECT t FROM CompoundField t WHERE t.value = :value AND "
            + "t.id.fieldId = COALESCE(:fieldId, t.id.fieldId) LIMIT 1000")
    List<CompoundField> findCompoundFieldsByValue(String value, Integer fieldId);

    /**
     * Fetch all CompoundFields (supports dynamic criteria via {@link JpaSpecificationExecutor}).
     */
    List<CompoundField> findAll();
}
