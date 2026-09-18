\connect curator
\connect - curator

/*
 * test data model
 */

INSERT INTO element_types (element_class, id, name, description, ui_color) VALUES
    ('NODE', 'ORGANISM', 'Organism', 'Living cellular organism', 0x297e00),
    ('NODE', 'COMPOUND', 'Compound', 'Chemical compound, ideally produced by a living organism and thus a natural product', 0x343ea0),
    ('NODE', 'DISEASE', 'Disease', 'A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.', 0xb1002a),
    ('EDGE', 'TREATS', 'treats', 'Agent beneficially influences condition', 0x0),
    ('EDGE', 'PRODUCES', 'produces', 'Living organism is capable to produce a natural product', 0x0);

INSERT INTO relation_types (left_type_id, relation_type_id, right_type_id) VALUES
    ('ORGANISM','TREATS','DISEASE'),
    ('ORGANISM','PRODUCES','COMPOUND');

INSERT INTO field_definitions (field_type, element_type_id, graph_export_order, name, description, mandatory, multivalued) VALUES
/* 1 */
    ('TEXT',    'ORGANISM', 1,    'primary name', 'primary node name', false, false),
    ('TEXT',    'COMPOUND', 1,    'primary name', 'primary node name', false, false),
    ('TEXT',    'DISEASE',  1,    'primary name', 'primary node name', false, false),
    ('TEXT',    'ORGANISM', null, 'synonym', 'alternative node names', false, true),
    ('TEXT',    'COMPOUND', null, 'synonym', 'alternative node names', false, true),
/* 6 */
    ('TEXT',    'DISEASE',  null, 'synonym', 'alternative node names', false, true),
    ('INTEGER', 'ORGANISM', null, 'NCBItaxonId', 'link to the NCBI taxonomy', false, false),
    ('ENUM',    'ORGANISM', null, 'Growth form', 'whether the plant grows as herb, shrub or tree', false, false);

INSERT INTO dyn_enums (field_id, label, description) VALUES
    (8, 'HERB', 'plant growing as a herb'),
    (8, 'SHRUB', 'plant growing as a shrub'),
    (8, 'TREE', 'plant growing as a tree');

/*
 * test data source
 */
INSERT INTO data_sources (id, description, handler, source_url) VALUES
    ('TEST_ORGANISMS', 'Source of test data', 'de.ipb_halle.curator.source.testHandler.TestOrganismImportHandler', 'file:testOrganisms.csv');

INSERT INTO element_mappings (data_source_id, element_type_id, source_field_name, identity_mapping, multivalued) VALUES
    ('TEST_ORGANISMS', 'ORGANISM', 'ncbitaxon', 'ORGANISM:NCBItaxonId', false);

INSERT INTO field_mappings (data_source_id, source_field_name, mapping, multivalued) VALUES
    ('TEST_ORGANISMS', 'primary name', 'ORGANISM:primary name', false),
    ('TEST_ORGANISMS', 'growth form', 'ORGANISM:Growth form', false),
    ('TEST_ORGANISMS', 'synonym', 'ORGANISM:synonym', false);

