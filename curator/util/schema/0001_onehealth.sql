\connect curator
\connect - curator

CREATE TYPE element_class AS ENUM ('NODE', 'EDGE');
CREATE TABLE element_types (
    id          SERIAL NOT NULL PRIMARY KEY,
    element_class       element_class NOT NULL,
    label       VARCHAR,
    name        VARCHAR,
    description VARCHAR,
    ui_color    INTEGER
);


CREATE TYPE field_class AS ENUM ('TEXT', 'ENUM', 'INTEGER', 'UUID');
CREATE TABLE field_types (
    id          SERIAL NOT NULL PRIMARY KEY,
    type        field_class UNIQUE NOT NULL,
    description VARCHAR,
    table_name  VARCHAR
);


CREATE TABLE field_definitions (
    id          SERIAL NOT NULL PRIMARY KEY,
    field_type_id     INTEGER NOT NULL REFERENCES field_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    element_type_id   INTEGER NOT NULL REFERENCES element_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    name        VARCHAR NOT NULL,
    description VARCHAR,
    mandatory   BOOLEAN NOT NULL DEFAULT FALSE,
    multivalued BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (element_type_id, name)
);


CREATE TABLE dyn_enums (
    id          SERIAL NOT NULL PRIMARY KEY,
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    label       VARCHAR,
    description VARCHAR
);


CREATE TABLE elements (
    id          UUID NOT NULL PRIMARY KEY,
    type_id     INTEGER NOT NULL REFERENCES element_types (id) ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE relations (
    left_id     UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    relation_id UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    right_id    UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (left_id, relation_id, right_id)
);


CREATE TABLE text_fields (
    element_id  UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_order INTEGER NOT NULL DEFAULT 0,
    value       VARCHAR,
    PRIMARY KEY (element_id, field_id, field_order)
);
CREATE INDEX text_fields_fulltext_index ON text_fields (value);
CREATE INDEX text_fields_field_index ON text_fields (field_id, value);


CREATE TABLE integer_fields (
    element_id  UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_order INTEGER NOT NULL DEFAULT 0,
    value       BIGINT,
    PRIMARY KEY (element_id, field_id, field_order)
);
CREATE INDEX integer_fields_field_index ON integer_fields (field_id, value);

/*
 * compounds ...
 */
