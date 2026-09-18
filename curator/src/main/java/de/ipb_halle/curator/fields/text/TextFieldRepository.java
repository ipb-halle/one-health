/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.FieldEntity;
import de.ipb_halle.curator.fields.OrderedFieldId;
import de.ipb_halle.curator.fields.integer.IntegerField;
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
public interface TextFieldRepository extends JpaRepository<TextFieldEntity, OrderedFieldId>, JpaSpecificationExecutor<TextFieldEntity> {

    /**
     * Fetch a single TextFieldEntity by its compound key (element_id, field_definition_id, order) using JPQL.
     */
    @Query("SELECT t FROM TextFieldEntity t WHERE t.id.elementId = :elementId "
            + "AND t.id.fieldId = :fieldId AND t.id.order = :order")
    Optional<TextFieldEntity> findTextField(UUID elementId, int fieldId, int order);

    /**
     * Fetch a list of TextFields for a given Entity.
     */
    @Query("SELECT t FROM TextFieldEntity t WHERE t.id.elementId = :elementId "
            + "ORDER BY t.id.fieldId, t.id.order ASC")
    List<FieldEntity> findTextFields(UUID elementId);

    /**
     * Fetch a list of TextFields for a given Entity.
     */
    @Query("SELECT t FROM TextFieldEntity t WHERE t.id.elementId = :elementId AND "
            + "t.id.fieldId = :fieldId ORDER BY t.id.order ASC")
    List<TextFieldEntity> findTextFields(UUID elementId, int fieldId);

    /**
     * Fetch all TextFields (supports dynamic criteria via {@link JpaSpecificationExecutor}).
     */
    List<TextFieldEntity> findAll();
}
