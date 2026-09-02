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
INSERT INTO element_types (element_class, label, name, description, ui_color) VALUES
    ('NODE', 'ORGANISM', 'Organism', 'Living cellular organism', 0x297e00),
    ('NODE', 'COMPOUND', 'Compound', 'Chemical compound, ideally produced by a living organism and thus a natural product', 0x343ea0),
    ('NODE', 'DISEASE', 'Disease', 'A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.', 0xb1002a);

CREATE TYPE field_class AS ENUM ('TEXT');

CREATE TABLE field_types (
    id          SERIAL NOT NULL PRIMARY KEY,
    type        field_class UNIQUE NOT NULL,
    description VARCHAR,
    table_name  VARCHAR
);
INSERT INTO field_types (type, description, table_name) VALUES
    ('TEXT', 'general text types', 'text_fields');

CREATE TABLE field_definitions (
    id          SERIAL NOT NULL PRIMARY KEY,
    field_type_id     INTEGER NOT NULL REFERENCES field_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    element_type_id   INTEGER NOT NULL REFERENCES element_types(id) ON UPDATE CASCADE ON DELETE CASCADE,
    name        VARCHAR NOT NULL,
    description VARCHAR,
    mandatory   BOOLEAN NOT NULL DEFAULT FALSE,
    multivalued BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO field_definitions (field_type_id, element_type_id, name, description, mandatory, multivalued) VALUES
    (1, 1, 'primary name', 'primary node name', false, false),
    (1, 2, 'primary name', 'primary node name', false, false),
    (1, 3, 'primary name', 'primary node name', false, false),
    (1, 1, 'synonym', 'alternative node names', false, true),
    (1, 2, 'synonym', 'alternative node names', false, true),
    (1, 3, 'synonym', 'alternative node names', false, true);


CREATE TABLE elements (
    id          UUID NOT NULL PRIMARY KEY,
    type_id     INTEGER NOT NULL REFERENCES element_types (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE relations (
    left_id     UUID NOT NULL REFERENCES elements (id),
    relation_id UUID NOT NULL REFERENCES elements (id),
    right_id    UUID NOT NULL REFERENCES elements (id),
    PRIMARY KEY (left_id, relation_id, right_id)
);

CREATE TABLE text_fields (
    element_id  UUID NOT NULL REFERENCES elements (id),
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_order INTEGER NOT NULL DEFAULT 0,
    value       VARCHAR,
    PRIMARY KEY (element_id, field_id, field_order)
);
CREATE INDEX text_fields_fulltext_index ON text_fields (value);
CREATE INDEX text_fields_field_index ON text_fields (field_id, value);

CREATE TABLE int_fields (
    element_id  UUID NOT NULL REFERENCES elements (id),
    field_id    INTEGER NOT NULL REFERENCES field_definitions (id) ON UPDATE CASCADE ON DELETE CASCADE,
    field_order INTEGER NOT NULL DEFAULT 0,
    value       BIGINT,
    PRIMARY KEY (element_id, field_id)
);
CREATE INDEX int_fields_field_index ON text_fields (field_id, value);

/*
 * compounds ...
 */

CREATE TABLE sample_entity (
    id          UUID NOT NULL PRIMARY KEY,
    name        VARCHAR,
    value       INTEGER
);
