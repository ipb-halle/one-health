/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import de.ipb_halle.curator.metadata.MetadataRegistry;
import jakarta.persistence.EntityManager;
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
    private EntityManager em;

    @Autowired
    private MetadataRegistry registry;


    public List<ElementDTO> loadByCriteria() {
    /*
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SampleEntity> cq = cb.createQuery(SampleEntity.class);
        Root<SampleEntity> root = cq.from(SampleEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (namePattern != null && !namePattern.isBlank()) {
            predicates.add(cb.like(root.get("name"), "%" + namePattern + "%"));
        }
        if (minValue != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("value"), minValue));
        }
        if (maxValue != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("value"), maxValue));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("name")));

        return converter.toDTOList(entityManager.createQuery(cq).getResultList());

    */
        return new ArrayList<> ();
    }
}
