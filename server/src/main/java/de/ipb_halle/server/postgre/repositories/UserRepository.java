package de.ipb_halle.server.postgre.repositories;

import de.ipb_halle.server.postgre.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}