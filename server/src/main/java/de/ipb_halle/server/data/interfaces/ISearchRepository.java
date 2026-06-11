package de.ipb_halle.server.data.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import de.ipb_halle.server.postgre.models.PSIndexEntry;

import java.util.List;

public interface ISearchRepository extends JpaRepository<PSIndexEntry, Long> {
}
