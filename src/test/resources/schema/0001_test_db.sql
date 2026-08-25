\connect curator
\connect - curator

CREATE TABLE sample_entity (
    id          uuid not null primary key,
    name        varchar,
    value       integer
);
