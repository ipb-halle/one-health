/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 *
 * @author fblocal
 */
public class DbTestHelper {

    private JdbcTemplate jdbcTemplate;

    public DbTestHelper(PostgreSQLContainer pg) {
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

    public void dbUpdate(String query, Object... params) {
        jdbcTemplate.update(query, params);
    }
}
