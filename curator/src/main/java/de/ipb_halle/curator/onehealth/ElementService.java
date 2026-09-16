/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.fields.FieldDTO;
import de.ipb_halle.curator.fields.FieldService;
import de.ipb_halle.curator.fields.integer.IntegerField;
import de.ipb_halle.curator.fields.text.TextField;
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


    public List<ElementDTO> loadByFieldValue(FieldDTO field) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Element> cq = cb.createQuery(Element.class);
        Root<Element> root = cq.from(Element.class);
        Join join = joinField(root, field);

        cq.where(getPredicate(cb, join, field));
        cq.select(root).distinct(true);
        List<ElementDTO> dtos = converter.createDTOs(entityManager
                .createQuery(cq)
                .getResultList());
        dtos.stream().forEach(e -> e.addFields(fieldService.loadFields(e.getId())));
        return dtos;
    }

    private Join joinField(Root root, FieldDTO field) {
        FieldType fieldType = getFieldType(field);
        return root.join(fieldType.getTableName());
    }

    private Predicate getPredicate(CriteriaBuilder cb, Join join, FieldDTO field) {
        return cb.equal(join.get("value"), getFieldValue(field));
    }

    private Object getFieldValue(FieldDTO field) {
        FieldType fieldType = getFieldType(field);
        switch(fieldType) {
            case TEXT :
                return ((TextField) field.createEntity()).getValue();
            case INTEGER :
                return ((IntegerField) field.createEntity()).getValue();
            default :
                return null;
        }
    }

    private FieldType getFieldType(FieldDTO field) {
        return field.getFieldDefinition().getFieldType();
    }
}
