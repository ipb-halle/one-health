\connect curator
\connect - curator

INSERT INTO element_types (element_class, id, name, description, ui_color) VALUES
    ('NODE', 'ORGANISM', 'Organism', 'Living cellular organism', 0x297e00),
    ('NODE', 'COMPOUND', 'Compound', 'Chemical compound, ideally produced by a living organism and thus a natural product', 0x343ea0),
    ('NODE', 'DISEASE', 'Disease', 'A condition that impairs the normal functioning of the body or one of its parts, and it is typically associated with specific symptoms and signs.', 0xb1002a),
    ('EDGE', 'TREATS', 'treats', 'Agent beneficially influences condition', 0x0);

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

