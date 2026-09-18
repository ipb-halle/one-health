/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.fields.compound.CompoundField;
import de.ipb_halle.curator.fields.compound.CompoundFieldRepository;
import de.ipb_halle.curator.fields.integer.IntegerField;
import de.ipb_halle.curator.fields.integer.IntegerFieldRepository;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.fields.text.TextFieldRepository;
import de.ipb_halle.curator.metadata.FieldDefinition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fblocal
 */
@Service
@Transactional(readOnly = true)
public class FieldService {
    private final static String QUERY_BY_VALUE
            = "SELECT element_id, field_id,  field_order, value %s FROM %s "
            + "WHERE value = ? %s LIMIT 100 OFFSET ?";

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FieldConverter converter;

    @Autowired
    private CompoundFieldRepository compoundRepository;

    @Autowired
    private IntegerFieldRepository integerRepository;

    @Autowired
    private TextFieldRepository textRepository;

    public List<FieldDTO> loadFields(UUID elementId) {
        List<FieldDTO> results = new ArrayList<> ();
        results.addAll(converter.createDTOs(compoundRepository.findCompoundFields(elementId)));
        results.addAll(converter.createDTOs(textRepository.findTextFields(elementId)));
        results.addAll(converter.createDTOs(integerRepository.findIntegerFields(elementId)));
        return results;
    }

    public List<FieldDTO> loadFieldsByValue(FieldDTO value, FieldDefinition fieldDef, int offset) {
        String extraFields = "";
        String tableName = value.getTableName();
        String fieldIdCondition = (fieldDef != null) ? " AND field_id = ? " : "";
        String sql = QUERY_BY_VALUE.formatted(extraFields, tableName, fieldIdCondition);
        Query query = entityManager.createNativeQuery(sql, value.getFieldType().getBaseEntity());
        int paramIndex = 1;
        query.setParameter(paramIndex++, value.createEntity().getValue());
        if (fieldDef != null) {
            query.setParameter(paramIndex++, fieldDef.getId());
        }
        query.setParameter(paramIndex++, offset);
        return converter.createDTOs(query.getResultList());
    }

    @Transactional
    public void saveField(FieldDTO field) {
        if (field.isMultivalued()) {
            saveFields((MultiValueFieldDTO) field);
        } else {
            saveSingleField(field);
        }
    }

    private void saveSingleField(FieldDTO field) {
        switch(field.getFieldDefinition().getFieldType()) {
            case TEXT:
                textRepository.save((TextField) field.createEntity());
                break;
            case INTEGER:
            case ENUM:
                integerRepository.save((IntegerField) field.createEntity());
                break;
            case COMPOUND:
                compoundRepository.save((CompoundField) field.createEntity());
                break;
            default:
                throw new UnsupportedOperationException("save not supported for field type");
        }
    }

    private void saveFields(MultiValueFieldDTO field) {
        field.getValues()
                .stream()
                .forEach(f -> saveField((FieldDTO) f));
    }
}
