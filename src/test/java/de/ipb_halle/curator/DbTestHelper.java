/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator;

import com.zaxxer.hikari.HikariDataSource;
import java.io.Closeable;
import java.util.UUID;
// import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 *
 * @author fblocal
 */
public class DbTestHelper implements Closeable {

    private final JdbcTemplate jdbcTemplate;
    private HikariDataSource dataSource;

    public DbTestHelper(PostgreSQLContainer pg) {
        jdbcTemplate = configureJdbcTemplate(pg);
    }

    private JdbcTemplate configureJdbcTemplate(PostgreSQLContainer postgreSQL) {
        dataSource = DataSourceBuilder
                .create()
                .url(postgreSQL.getJdbcUrl())
                .username(postgreSQL.getUsername())
                .password(postgreSQL.getPassword())
                .type(HikariDataSource.class)
                .build();
        dataSource.setMaximumPoolSize(1);
        return new JdbcTemplate(dataSource);
    }

    public void deleteElements() {
        jdbcTemplate.execute("DELETE FROM elements");
    }

    public void dbUpdate(String query, Object... params) {
        jdbcTemplate.update(query, params);
    }

    public UUID createElement(int typeId) {
        UUID id = UUID.randomUUID();
        jdbcTemplate.update("INSERT INTO elements (id, type_id) VALUES (?,?)", id, typeId);
        return id;
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
