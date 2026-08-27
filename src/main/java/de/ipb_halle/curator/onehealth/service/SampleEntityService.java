package de.ipb_halle.curator.onehealth.service;

import de.ipb_halle.curator.onehealth.SampleEntity;
import de.ipb_halle.curator.onehealth.conversion.SampleEntityConverter;
import de.ipb_halle.curator.onehealth.dto.SampleEntityDTO;
import de.ipb_halle.curator.onehealth.repository.SampleEntityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SampleEntityService {

    private static final Logger logger = LoggerFactory.getLogger(SampleEntityService.class);

    private final SampleEntityRepository repository;
    private final SampleEntityConverter converter;
    private final EntityManager entityManager;

    public SampleEntityService(SampleEntityRepository repository,
            SampleEntityConverter converter,
            EntityManager entityManager) {
        this.repository = repository;
        this.converter = converter;
        this.entityManager = entityManager;
    }

    /**
     * Fetch a single SampleEntity by UUID using JPQL.
     */
    @Transactional(readOnly = true)
    public Optional<SampleEntityDTO> findById(UUID id) {
        return repository.findByIdJPQL(id).map(converter::toDTO);
    }

    /**
     * Save or update a SampleEntity. Accepts either a DTO or entity.
     */
    @Transactional
    public SampleEntityDTO save(SampleEntityDTO dto) {
        SampleEntity entity = converter.toEntity(dto);
        SampleEntity saved = repository.save(entity);
        return converter.toDTO(saved);
    }

    /**
     * Save a sample entity directly (entity variant).
     */
    @Transactional
    public SampleEntity save(SampleEntity entity) {
        return repository.save(entity);
    }

    /**
     * Fetch a list of SampleEntities by their value property. Simple query →
     * JPQL via repository.
     */
    @Transactional(readOnly = true)
    public List<SampleEntityDTO> findByValue(int value) {
        logger.info("findByValue called");
        return converter.toDTOList(repository.findByValueJPQL(value));
    }

    /**
     * Fetch a list of SampleEntities using the Criteria API. Demonstrates
     * complex / dynamic query building (e.g. optional filters).
     */
    @Transactional(readOnly = true)
    public List<SampleEntityDTO> findByCriteria(String namePattern, Integer minValue, Integer maxValue) {
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
    }

    /**
     * Fetch a list of SampleEntities via Specification (also Criteria-based
     * under the hood).
     */
    @Transactional(readOnly = true)
    public List<SampleEntityDTO> findBySpecification(Specification<SampleEntity> spec) {
        return converter.toDTOList(repository.findAll(spec));
    }

    /**
     * Fetch all SampleEntities (no filtering).
     */
    @Transactional(readOnly = true)
    public List<SampleEntityDTO> findAll() {
        return converter.toDTOList(repository.findAll());
    }
}
