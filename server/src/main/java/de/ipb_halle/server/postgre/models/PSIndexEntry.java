package de.ipb_halle.server.postgre.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "")
public class PSIndexEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String key;
    private String entityId;
}
