/*
 * One-Health data set configuration
 * This data is to be moved to the One Health Data repository
 */
\set CURATOR_DATABASE curator
\set CURATOR_SCHEMA curator
\set CURATOR_USER curator

\connect :CURATOR_DATABASE :CURATOR_USER
\set search_path :CURATOR_SCHEMA

/*
 * data model
 */
INSERT INTO element_types (element_class, id, name, description, ui_color) VALUES
    ('NODE', 'ORGANISM', 'Organism', 'Living cellular organism', 0x297e00),
    ('NODE', 'COMPOUND', 'Compound', 'Chemical compound, ideally produced by a living organism and thus a natural product', 0x343ea0),
    ('EDGE', 'PRODUCES', 'produces', 'Living organism is capable to produce a natural product', 0x0);

INSERT INTO relation_types (left_type_id, relation_type_id, right_type_id) VALUES
    ('ORGANISM','PRODUCES','COMPOUND');

INSERT INTO field_definitions (field_type, element_type_id, graph_export_order, name, description, mandatory, multivalued) VALUES
    ('TEXT', 'COMPOUND', null, 'synonyms', 'synonym names for compound', false, false),
    ('COMPOUND', 'COMPOUND', null, 'structure', 'chemical constitution and configuration', true, false),
    ('TEXT', 'COMPOUND', null, 'InChI-Key', 'InChi-Key as computed from structure', false, false),
    ('TEXT', 'COMPOUND', null, 'CoconutId', 'Record identifier used by COCONUT DB (https://coconut.naturalproducts.net)', false, false),
    ('TEXT', 'ORGANISM', null, 'synonyms',  'synonym names for organisms', false, true);

/*
 * data sources
 */
INSERT INTO data_sources (id, description, handler, source_url) VALUES
    ('COCONUT', 'COlleCtion of Open NatUral producTs (https://coconut.naturalproducts.net', 'de.ipb_halle.curator.source.coconut.CoconutImportHandler', 'file://coconut.csv');

INSERT INTO element_mappings (data_source_id, element_type_id, source_field_name, identity_mapping, multivalued) VALUES
    ('COCONUT', 'ORGANISM', 'organisms', 'ORGANISM:synonym', true),
    ('COCONUT', 'COMPOUND', 'standard_inchi', 'COMPOUND:InChI-Key', false);

INSERT INTO field_mappings (data_source_id, source_field_name, mapping, multivalued) VALUES
    ('COCONUT', 'identifier', 'COMPOUND:CoconutId', false),
    ('COCONUT', 'synonyms', 'COMPOUND:synonyms', true);

