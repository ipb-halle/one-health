package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByOrcid(String orcid);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}