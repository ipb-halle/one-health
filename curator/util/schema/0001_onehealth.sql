\set CURATOR_DATABASE curator
\set CURATOR_SCHEMA curator
\set CURATOR_USER curator

\connect :CURATOR_DATABASE
\i /opt/bingo/bingo_install.sql
GRANT USAGE ON SCHEMA bingo TO :CURATOR_USER;
GRANT SELECT ON bingo.bingo_config TO :CURATOR_USER;
GRANT SELECT ON bingo.bingo_tau_config TO :CURATOR_USER;

\set search_path :CURATOR_SCHEMA
\connect - :CURATOR_USER

CREATE TYPE element_class AS ENUM ('NODE', 'EDGE');
CREATE TABLE element_types (
    id          SERIAL NOT NULL PRIMARY KEY,
    element_class       element_class NOT NULL,
    label       VARCHAR,
    name        VARCHAR,
    description VARCHAR,
    ui_color    INTEGER
);

CREATE TABLE relation_types (
    left_type_id        INTEGER NOT NULL REFERENCES element_types (id)  ON UPDATE CASCADE ON DELETE CASCADE,
    relation_type_id    INTEGER NOT NULL REFERENCES element_types (id)  ON UPDATE CASCADE ON DELETE CASCADE,
    right_type_id       INTEGER NOT NULL REFERENCES element_types (id)  ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (left_type_id, relation_type_id, right_type_id)
);

CREATE TYPE field_type_enum AS ENUM ('TEXT', 'ENUM', 'INTEGER', 'UUID', 'REAL', 'STRUCTURE');

CREATE TABLE data_sources (
    id          SERIAL NOT NULL PRIMARY KEY,
    name        VARCHAR,
    description VARCHAR,
    handler     VARCHAR,
    source_url  VARCHAR
);

CREATE TABLE element_mappings (
    id                  SERIAL NOT NULL PRIMARY KEY,
    data_source_id      INTEGER NOT NULL REFERENCES data_sources (id) ON UPDATE CASCADE ON DELETE CASCADE,
    element_type_id     INTEGER NOT NULL REFERENCES element_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    mapping             VARCHAR NOT NULL
);

CREATE TABLE field_mappings (
    id                  SERIAL NOT NULL PRIMARY KEY,
    data_source_id      INTEGER NOT NULL REFERENCES data_sources (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_name          VARCHAR NOT NULL,
    mapping             VARCHAR NOT NULL
);

CREATE TABLE field_definitions (
    id          SERIAL NOT NULL PRIMARY KEY,
    field_type        field_type_enum NOT NULL,
    element_type_id   INTEGER NOT NULL REFERENCES element_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    graph_export_order  INTEGER,
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

CREATE TABLE real_fields (
    element_id  UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_order INTEGER NOT NULL DEFAULT 0,
    value       DOUBLE PRECISION,
    PRIMARY KEY (element_id, field_id, field_order)
);
CREATE INDEX float_fields_field_index ON integer_fields (field_id, value);

/* we use the InChI as the value field to allow quick searches for equality */
CREATE TABLE compound_fields (
    element_id  UUID NOT NULL REFERENCES elements (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    value       TEXT,
    compound    TEXT,
    PRIMARY KEY (element_id, field_id)
);
CREATE INDEX compound_fields_inchi_idx ON compound_fields (value);
CREATE INDEX compound_fields_compound_idx ON compound_fields USING bingo_idx (compound bingo.molecule);
