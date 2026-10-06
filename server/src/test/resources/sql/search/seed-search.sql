CREATE TABLE IF NOT EXISTS entity_string_index (
    entityid VARCHAR(255) NOT NULL,
    key VARCHAR(255) NOT NULL
);

DELETE FROM entity_string_index;

INSERT INTO entity_string_index (entityid, key) VALUES ('entity-aspirin-1', 'aspirin');
