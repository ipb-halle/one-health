# Metadata System

## Overview

The metadata system provides immutable, read-only lookup data loaded from the database at application startup. This covers three database tables that never change during runtime:

| Table | Purpose | Map Key |
|---|---|---|
| `node_types` | Entity kinds (Organism, Compound, Disease) | `String name` |
| `field_types` | Field categories for polymorphic fields | Java enum `FieldTypeEnum` |
| `field_definitions` | Specific field definitions with type reference | `String name` |

## Architecture

```
de.ipb_halle.curator.metadata/
├── enums/
│   └── FieldTypeEnum.java            ← Enum from field_types.name (TEXT)
├── model/
│   ├── NodeTypeInfo.java             ← Immutable POJO for node_types
│   └── FieldDefinitionInfo.java      ← Immutable POJO for field_definitions
├── repository/
│   └── MetadataRepository.java       ← Read-only JdbcTemplate queries
├── service/
│   └── MetadataLoader.java           ← @EventListener loads data at startup
└── MetadataRegistry.java             ← Singleton holder (three maps)
```

## Data Flow

1. **Startup** — `MetadataLoader` receives `ApplicationReadyEvent` from Spring.
2. **Load** — Queries the database via `JdbcTemplate` in `MetadataRepository`.
3. **Populate** — Builds three maps and passes them to `MetadataRegistry.initialize()`.
4. **Consume** — Other components `@Autowired` the registry to access lookup data.

```
Database ──► MetadataRepository (JdbcTemplate) ──► MetadataLoader ──► MetadataRegistry
                                                                    │
                                                            @Autowired by consumers
```

## FieldTypeEnum

The `field_types` table maps to a Java enum. Each row's `name` column must be uppercase and is converted via `FieldTypeEnum.fromName("TEXT")`. This provides compile-time safety for type discrimination.

To add a new field type:
1. Insert a row into `field_types(name, ...)` in the database
2. Add a corresponding enum constant to `FieldTypeEnum`

## MetadataRegistry API

| Method | Returns | Unmodifiable |
|---|---|---|
| `getNodeTypesByName()` | `Map<String, NodeTypeInfo>` | Yes |
| `getFieldDefsByType()` | `Map<FieldTypeEnum, List<FieldDefinitionInfo>>` | Yes |
| `getFieldDefsByName()` | `Map<String, FieldDefinitionInfo>` | Yes |
| `isInitialized()` | `boolean` | — |

## Design Decisions

- **No JPA entities** — These tables are read-only lookup data. JPA overhead (session management, dirty checking, lazy loading) is unnecessary.
- **JdbcTemplate** — Lightweight, explicit SQL, no ORM mapping overhead.
- **Unmodifiable maps** — Populated once at startup via `Collections.unmodifiableMap()`, then never changed. Safe for concurrent reads.
- **Singleton scope** — All components are Spring singletons by default (`@Component`, `@Service`, `@Repository`).
