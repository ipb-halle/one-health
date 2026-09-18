/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.fields.AbstractField;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.fields.integer.IntegerFieldEntity;
import de.ipb_halle.curator.fields.text.TextFieldEntity;
import de.ipb_halle.curator.metadata.ElementType;
import de.ipb_halle.curator.metadata.FieldType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author fblocal
 */
@Service
public class ElementService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ElementConverter converter;

    @Autowired
    private ElementRepository repository;

    @Autowired
    private FieldService fieldService;

    public List<ElementDTO> loadByType(ElementType type) {
       List<Element> elements =  repository.findElementsByType(type.getId());
       List<ElementDTO> dtos = converter.createDTOs(elements);
       dtos.stream().forEach(e -> e.addFields(fieldService.loadFields(e.getId())));
       return dtos;
    }


    public ElementDTO loadByFieldValue(AbstractField queryField) {
        List <AbstractField> fields = fieldService.loadFieldsByValue(queryField, null, 0);
        if (fields.size() == 1) {
            UUID elementId = fields.get(0).getId().getElementId();
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Element> cq = cb.createQuery(Element.class);
            Root<Element> root = cq.from(Element.class);
            cq.where(cb.equal(root.get("id"), elementId));
            cq.select(root);
            List<Element> result = entityManager.createQuery(cq).getResultList();
            if (result.size() == 1) {
                ElementDTO element = converter.createDTO(result.get(0));
                element.addFields(fieldService.loadFields(elementId));
                return element;
            } else {
                throw new IllegalStateException("Obtained multiple Elements for single id");
            }
        }
        return null;
    }

    private Join joinField(Root root, AbstractField field) {
        FieldType fieldType = getFieldType(field);
        return root.join(fieldType.getTableName());
    }

    private Predicate getPredicate(CriteriaBuilder cb, Join join, AbstractField field) {
        return cb.equal(join.get("value"), getFieldValue(field));
    }

    private Object getFieldValue(AbstractField field) {
        FieldType fieldType = getFieldType(field);
        switch(fieldType) {
            case TEXT :
                return ((TextFieldEntity) field.createEntity()).getValue();
            case INTEGER :
                return ((IntegerFieldEntity) field.createEntity()).getValue();
            default :
                return null;
        }
    }

    public void save(ElementDTO dto) {
        repository.save(dto.createEntity());
        for(AbstractField field : dto.getFields()) {
            fieldService.saveField(field);
        }
    }


    private FieldType getFieldType(AbstractField field) {
        return field.getFieldDefinition().getFieldType();
    }
}
