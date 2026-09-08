\connect curator
\connect - curator

INSERT INTO element_types (element_class, label, name, description, ui_color) VALUES
    ('NODE', 'ORGANISM', 'Organism', 'Living cellular organism', 0x297e00),
    ('NODE', 'COMPOUND', 'Compound', 'Chemical compound, ideally produced by a living organism and thus a natural product', 0x343ea0),
    ('NODE', 'DISEASE', 'Disease', 'A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.', 0xb1002a),
    ('EDGE', 'TREATS', 'treats', 'Agent beneficially influences condition', 0x0);

INSERT INTO field_types (type, description, table_name) VALUES
    ('TEXT', 'general text types', 'text_fields'),
    ('INTEGER', 'integral types', 'integer_fields'),
    ('ENUM', 'enumeration types', 'integer_fields'),
    ('UUID', 'universally unique identifiers', 'uuid_fields');


INSERT INTO field_definitions (field_type_id, element_type_id, name, description, mandatory, multivalued) VALUES
    (1, 1, 'primary name', 'primary node name', false, false),
    (1, 2, 'primary name', 'primary node name', false, false),
    (1, 3, 'primary name', 'primary node name', false, false),
    (1, 1, 'synonym', 'alternative node names', false, true),
    (1, 2, 'synonym', 'alternative node names', false, true),
    (1, 3, 'synonym', 'alternative node names', false, true),
    (2, 1, 'NCBItaxonId', 'link to the NCBI taxonomy', false, false),
    (3, 1, 'Growth form', 'whether the plant grows as herb, shrub or tree', false, false);

INSERT INTO dyn_enums (field_id, label, description) VALUES
    (8, 'HERB', 'plant growing as a herb'),
    (8, 'SHRUB', 'plant growing as a shrub'),
    (8, 'TREE', 'plant growing as a tree');

