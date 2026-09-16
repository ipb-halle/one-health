/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

/**
 * Represents the available field types. This class must correspond to the
 * database type field_type_enum
 */
public enum FieldType {

        TEXT("text_fields"),
        INTEGER("integer_fields"),
        ENUM("integer_fields"),
        UUID("text_fields"),
        FLOAT("float_fields"),
        STRUCTURE("compound_fields");

        private final String tableName;

        FieldType(String tableName) {
            this.tableName = tableName;
        }

        public String getTableName() {
            return tableName;
        }
}
