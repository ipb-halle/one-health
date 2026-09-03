/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.onehealth;

import java.util.UUID;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 *
 * @author fblocal
 */
public class ElementHelper {

    private JdbcTemplate jdbcTemplate;

    public ElementHelper(PostgreSQLContainer pg) {
        configureJdbcTemplate(pg);
    }

    private void configureJdbcTemplate(PostgreSQLContainer postgreSQL) {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder
                .create()
                .url(postgreSQL.getJdbcUrl())
                .username(postgreSQL.getUsername())
                .password(postgreSQL.getPassword())
                .build());
    }

    public void deleteElements() {
        jdbcTemplate.execute("DELETE FROM elements");
    }

    public UUID createElement(int typeId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO elements (id, type_id) VALUES (?,?)", id, typeId);
        return id;
    }
}
