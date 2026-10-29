package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.UserAuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import de.ipb_halle.server.postgre.models.AuthenticationProvider;

import java.util.List;
import java.util.Optional;

public interface UserAuthenticationRepository
        extends JpaRepository<UserAuthenticationEntity, Long> {

    Optional<UserAuthenticationEntity> findByProviderAndProviderSubjectId(
        AuthenticationProvider provider, 
        String providerSubjectId);

    List<UserAuthenticationEntity> findByUserId(Long userId);
}
