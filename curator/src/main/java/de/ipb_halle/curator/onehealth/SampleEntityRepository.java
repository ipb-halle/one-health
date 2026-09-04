package de.ipb_halle.curator.onehealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SampleEntityRepository extends JpaRepository<SampleEntity, UUID>, JpaSpecificationExecutor<SampleEntity> {

    /**
     * Fetch a single SampleEntity by its UUID using JPQL.
     */
    @Query("SELECT s FROM SampleEntity s WHERE s.id = :id")
    Optional<SampleEntity> findByIdJPQL(UUID id);

    /**
     * Fetch a list of SampleEntities by their value property using JPQL.
     */
    @Query("SELECT s FROM SampleEntity s WHERE s.value = :value ORDER BY s.name ASC")
    List<SampleEntity> findByValueJPQL(int value);

    /**
     * Fetch all SampleEntities (supports dynamic criteria via {@link JpaSpecificationExecutor}).
     */
    List<SampleEntity> findAll();
}
