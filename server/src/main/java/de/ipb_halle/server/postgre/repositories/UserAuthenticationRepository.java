package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthenticationRepository
        extends JpaRepository<UserAuthenticationEntity, Long> {

    Optional<UserAuthenticationEntity> findByOrcidId(String orcidId);

    Optional<UserAuthenticationEntity> findByUserId(Long userId);

}
