package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.UserCreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCreditRepository extends JpaRepository<UserCreditEntity, Long> {
}