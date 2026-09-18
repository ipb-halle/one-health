/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.metadata;

import de.ipb_halle.curator.fields.compound.CompoundField;
import de.ipb_halle.curator.fields.integer.IntegerFieldEntity;
import de.ipb_halle.curator.fields.real.RealField;
import de.ipb_halle.curator.fields.text.TextFieldEntity;

/**
 * Represents the available field types. This class must correspond to the
 * database type field_type_enum
 */
public enum FieldType {

        TEXT("text_fields", TextFieldEntity.class, null),
        INTEGER("integer_fields", IntegerFieldEntity.class, null),
        ENUM("integer_fields", IntegerFieldEntity.class, INTEGER),
        UUID("text_fields", TextFieldEntity.class, TEXT),
        REAL("real_fields", RealField.class, null),
        COMPOUND("compound_fields", CompoundField.class, null);

        private final String tableName;
        private final Class  baseEntity;
        private final FieldType baseType;

        FieldType(String tableName, Class baseEntity, FieldType baseType) {
            this.tableName = tableName;
            this.baseEntity = baseEntity;
            this.baseType = (baseType == null) ? this : baseType;
        }

        public String getTableName() {
            return tableName;
        }

        public Class getBaseEntity() {
            return baseEntity;
        }

        public FieldType getBaseType() {
            return baseType;
        }
}
