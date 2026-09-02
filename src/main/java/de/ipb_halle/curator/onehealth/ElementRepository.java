/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.onehealth;

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
public interface ElementRepository extends JpaRepository<Element, UUID>, JpaSpecificationExecutor<Element> {

    /**
     * Fetch a single TextField by its id using JPQL.
     */
    @Query("SELECT e FROM Element e WHERE e.id = :id")
    Optional<Element> findElement(UUID id);


    /**
     * Fetch all Elements (supports dynamic criteria via {@link JpaSpecificationExecutor}).
     */
    List<Element> findAll();
}
