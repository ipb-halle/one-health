# AI_INFO — Project Guidelines for Copilot / AI Coding Assistants

---

## 1. Purpose & Domain

This project merges data from multiple databases and curates a consolidated knowledge graph.
The main project maintains its own entities and relations alongside imported records from foreign sources.
Data records may change over time; both the main project's records and external source records are tracked in this source code repository for versioned releases of the knowledge graph.

**Core domain triangles:**
- ORGANISM produces NATURAL COMPOUND
- ORGANISM treats DISEASE
- NATURAL PRODUCT treats DISEASE

Over time, additional entity and relation types will be added (e.g., geo-location, enzymes).

### Identifier Strategy
Persistent identifiers must be resolved for every imported record:
- Chemical structures → InChI / InChI-Key
- Ontology records → IRI
- Other sources → original source identifier tracked alongside

This ensures releases can be compared and cited reliably. Records marked obsolete or deleted in their original sources must be handled correctly during merging (see §4).

---

## 2. Architecture

### Runtime
The application is a Spring Boot executable JAR run as a single commandline process:
```
java -jar curator.jar
```

### Persistence
- **Primary database:** PostgreSQL — all compiled/curated data lives here.
- **Graph export target:** Neo4J — the PostgreSQL contents are later transformed into graph data.
- **ORM:** Hibernate (via Spring Data JPA) is used exclusively for database access.

### Database Schema Management
- The SQL schema scripts are stored in `util/schema`
- This schema is used for development, testing and production. Some scripts, which require superuser privileges are irrelevant for testing and development. Do not touch them.
- Migrations for the production database are always applied manually - no tools (Flyway or Liquibase) should be adopted

### Application Startup & Component Discovery
The entry point is `CuratorApplication.java`, annotated with `@SpringBootApplication`. This annotation combines three behaviors:

1. **`@Configuration`** — marks the class as a Spring configuration source
2. **`@EnableAutoConfiguration`** — activates Spring Boot's auto-configuration (automatically configures beans based on classpath dependencies and properties)
3. **`@ComponentScan`** — scans the current package (`de.ipb_halle.curator`) and all sub-packages for stereotype-annotated classes

For this project, **implicit component scanning is the preferred approach**. The codebase is small enough that bean definitions are discoverable without explicit `@Bean` wiring. Scanning covers:
- `@Service` → business logic services (singleton scope by default)
- `@Repository` → Spring Data repository interfaces (singleton scope)
- `@Component` → utility components such as converters (singleton scope)

All discovered beans use **singleton scope** by default (one shared instance per Spring context). This is equivalent to Jakarta EE's CDI `@ApplicationScoped`. To use a different scope, annotate the bean explicitly (`@Scope("prototype")`, etc.), but this should be rare and well-justified.

### Configuration & Environments
The application uses **Spring Boot profiles** to switch between environments. Profiles are activated via `--spring.profiles.active=<profile>` or the `SPRING_PROFILES_ACTIVE` environment variable.

#### How It Works
Spring Boot loads configuration files in this priority order (highest first):
1. `application-{profile}.properties` for each active profile
2. `application.properties` (base configuration, always loaded)

Property values in profile-specific files **override** the base file.

#### Profile Files to Create
- `src/main/resources/application.properties` — base defaults (shared across all environments)
- `src/main/resources/application-dev.properties` — local development settings
- `src/main/resources/application-prod.properties` — production settings

#### Switching Environments

```bash
# Development (local PostgreSQL on localhost, open-in-view=true for debugging)
java -jar curator.jar --spring.profiles.active=dev

# Production (production database, open-in-view=false, secure connection pooling)
java -jar curator.jar --spring.profiles.active=prod

# Testing (profile is handled by @SpringBootTest / @Testcontainers; no active profile needed)
mvn test
```

#### Environment Variable Override
Sensitive values can be injected via environment variables at runtime:
```bash
SPRING_DATASOURCE_PASSWORD=mysecret123 java -jar curator.jar --spring.profiles.active=prod
```
This is the **recommended** approach for secrets — never hardcode them in properties files.

#### Typical Profile Property Overrides
| Property | `dev` | `prod` |
|---|---|---|
| `spring.datasource.url` | `jdbc:postgresql://localhost:5432/curator` | Production DB URL (remote) |
| `spring.jpa.open-in-view` | `true` | `false` |
| `logging.level.root` | `DEBUG` | `INFO` |

---

## 3. Code Structure & Package Layout

### Domain-Based Packaging
Package structure follows **business domains** (external data sources), NOT technical layers.
Each external data source gets its own sub-package under `de.ipb_halle.curator/{sourceName}/`:

```
de.ipb_halle.curator/
├── onehealth/                  ← business domain: OneHealth data source
│   ├── SampleEntity.java       ← JPA entity
│   ├── SampleEntityDTO.java    ← DTO (transfer object)
│   ├── repository/             ← Spring Data repositories (interfaces)
│   ├── service/                ← business logic services (@Service)
│   └── conversion/             ← converter classes (@Component)
├── another_source/             ← future: separate business domain
```

All models, DTOs, repository interfaces, and services for a given source live within its package. Avoid technical-layer packages (`model/`, `dao/`, `dto/` at the root level).

### Component Responsibilities
- Controllers should be called directly - additional faceade layers are not wanted
- Conversion components can be called outside of service contexts
- All public APIs should use the DTOs, database entities should not be exposed publicly

---

## 4. Coding Conventions

### Dependency Injection
Field injection with `@Autowired` is the primary method for dependency injection. Constructor injection is to be used sparingly for 
helper classes. These helper classes must do not constitute a service or bean. Their usage scope should be limited, e.g. to a certain 
package.

### Transaction Management
- Use `@Transactional(readOnly = true)` on all methods that only read data. This enables Hibernate read-only optimization.
- Plain `@Transactional` is used for methods that write (save, update, delete).
- Do not annotate fields or classes with transaction semantics — only individual methods.

### SQL & Query Conventions
**Query Declaration (Required)**
All JPQL/HQL queries MUST be declared explicitly using `@Query` annotations. Deriving queries from method names is forbidden:
```java
// ✅ Required
@Query("SELECT s FROM SampleEntity s WHERE s.value = :value")
List<SampleEntity> findByValueJPQL(int value);

// ❌ Forbidden — Spring Data will derive this, but we disallow it
List<SampleEntity> findByValue(int value);
```
This rule extends beyond JPQL: no derived delete methods, no implicit query generation of any kind. All SQL/JPQL must be visible in the source code for traceability.

### Open-in-View (Session Lifecycle)
`open-in-view=true` keeps the Hibernate Session alive until the end of the HTTP request, allowing lazy-loaded proxies to be accessed outside `@Transactional` methods. This is useful in development because it makes lazy-loading bugs visible immediately (missing data throws an exception). In production, however, it hides N+1 query problems and creates invisible transaction boundaries that exhaust DB connections.

**Rule:** Use `open-in-view=true` **only** in the dev profile. Set to `false` (or remove it) in all other profiles. Fetch required data explicitly via JPQL `JOIN FETCH` clauses in production code.

### DTO-to-Entity Mapping Policy
- DTO-to-Entity Mapping and vice versa is done by conversion classes, which are annotated with `@Component`
- partial mapping is allowed - DTOs do not necessarily contain all entity fields
- ModelMapper is strictly forbidden, because does not provide compile time checks and will be evaluated at runtime only
- MapStruct may be acceptable for simple cases (i.e. an entity DTO pair which has many properties but is otherwise simple). A decision will be made on a case by case basis. Agents may suggest changes but need explicit approval.

### Error Handling Strategy
- Custom exception hierarchy for domain-specific errors (e.g., `EntityNotFoundException`, `DuplicateIdentifierException`). All custom exceptions are **unchecked** (`extends RuntimeException`) — callers do not need try/catch blocks.
- Exceptions are handled on a per-service basis, no global exception handler (`@ControllerAdvice` / `@RestControllerAdvice`). Each service decides when to wrap a low-level exception (e.g., Hibernate `DataAccessException`) in a domain-specific one, and when to let it bubble up as-is.
- Validation failures for **missing** data: throw a custom exception or return Optional depending on whether "not found" is a normal outcome of the operation.
- Validation failures for **invalid input**: log at WARN and return early with an empty Optional, rather than throwing.
- Fatal errors (unexpected exceptions) cause program termination — do not catch and swallow them.

---

## 5. Testing Strategy

### Test Scope Rules
| Test Type | Annotation | What's Loaded | Use When |
|---|---|---|---|
| **Repository slice test** | `@DataJpaTest` + `@Testcontainers` | Only JPA entities, repositories, EntityManager | Testing repository queries in isolation |
| **Service integration test** | `@SpringBootTest` + `@Testcontainers` | Full application context (all beans) | Testing the complete service chain including converters and services |
| **Converter unit test** | Plain JUnit 5 (+ Mockito) | Nothing from Spring | Pure Java conversion logic, no Spring dependencies |

### Database Test Setup
All database-dependent tests MUST use Testcontainers:
```java
@SpringBootTest
@Testcontainers
class MyIntegrationTest {
    @Autowired SampleEntityService service; // Full chain available
}

@DataJpaTest
@Testcontainers
class MyRepoTest {
    @Autowired SampleEntityRepository repository; // Repository only
}
```

### SQL Data Insertion in Tests
When inserting test data in `@SpringBootTest` integration tests, use JDBC (`DbTestHelper`) rather than JPQL — uncommitted JDBC inserts are invisible to other transactions during the same test. In `@DataJpaTest` slice tests, you may use JPQL within the same transaction.

### Naming Conventions for Test Classes
Integration tests and acceptance tests will be created manually. The naming scheme will be determined later.

---

## 6. SOLID Principles Reference

All code must observe the five SOLID principles. This is mandatory, not optional:

1. **Single Responsibility** — Each class has one reason to change.
2. **Open/Closed** — Classes are open for extension but closed for modification (prefer composition over inheritance).
3. **Liskov Substitution** — Subtypes must be substitutable for their base types without altering behavior.
4. **Interface Segregation** — Prefer narrow, focused interfaces over fat ones.
5. **Dependency Inversion** — Depend on abstractions (interfaces), not concrete implementations.

---

## 7. Coding Style Summary

| Rule | Enforcement |
|---|---|
| Field injection (constructor injection only for helper classes - not beans or services) | Code review |
| All queries explicit via `@Query` | Compiler + code review |
| `readOnly = true` on read-only methods | Convention |
| Domain-based packages (`curator/{sourceName}/`) | Code review |
| No derived query methods | Code review |
| Testcontainers for all DB tests | CI pipeline |
| Explicit config over convention | Code review |

---

## 8. Logging Policy

The application uses **Logback** (included transitively via Spring Boot) for all logging. No additional dependencies are required.

### Log File Configuration

- All output goes to stdout (console).
- Application packages (`de.ipb_halle.curator.*`) additionally write to `curator.log`.
- Framework logs (Spring, Hibernate, Neo4j) go only to console at WARN level.
- The log file directory is configurable via the standard Spring Boot property `LOG_DIR`:

```bash
# CLI override (highest precedence)
java -jar curator.jar -DLOG_DIR=/var/log/curator

# Environment variable
export LOG_DIR=/var/log/curator
java -jar curator.jar

# Profile file
src/main/resources/application-prod.properties: logging.log.dir=/var/log/curator

---

## 9. Chemical Structure Handling

The database includes chemical structure information. The main application requires substructure searches on this data, but the curation database does not need this capability. Ensure chemical structure columns and indices are preserved during any schema changes. This project uses standard strings for chemical structures.

---

## 10. Obsolete / Deleted Record Handling
Records marked obsolete or deleted in original sources are soft deleted:
- Soft delete flag (`is_obsolete BOOLEAN DEFAULT false`)?

---

*This document is intended for AI coding assistants. It supersedes informal conventions and serves as the canonical reference for project guidelines.*
