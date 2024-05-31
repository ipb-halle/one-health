# Security Scheme

### Dependencies Audit

Frontend dependencies are automatically checked by a continuous integration test using a GitHub action to run the command

```bash
npm audit --omit=dev
```

which checks for vulnerabilities in the production build of the app. The `dev` and `main` branches of the repository are protected by only accepting changes coming from pull requests that have passed the automatic tests, ensuring that there are no depency vulnerabilities in the app.

The source code for the integration test can be found in `.github/workflows/ci_test.yaml`

### Database Connection Pool Management

The database connection pool is protected by:

1. Ensuring that every connection is closed after retrieving the data

   - By default most query methods of the `Neo4jTemplate` and `JdbcTemplate` objects (Neo4J driver and PostgreSQL driver respectively) close the connections during a `finally` block statement ensuring the connection is closed.

   - For the special case of the method `JdbcTemplate.queryForStream` which does not close the connection automatically since it has a lazy behaviour it becomes necessary to call this method within a `try` block which disposes the `Stream` object and therefore closes the connection after finishing consuming the data stream or an exception has been raised. An example of how to use this method is provided below.

     ```java
     //ontology-manager-sever/src/main/java/ipbhalle/de/ontologymanagerserver/postgre/repositories/PSQLNaturalProductRepository.java
     
       private NaturalProductDTO QueryForCompound(String query, @Nullable Object... args) {
           try (var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), args)){
               var first = result.findFirst();
               if (first.isEmpty())
                   return null;
               return PSQLMapper.MAPPER.map(first.get());
           } catch (Exception e) {
               e.printStackTrace();
               return null;
           }
       }
     
       private List<NaturalProductDTO> QueryForCompoundList(String query, @Nullable Object... args){
           try (var result =  template.queryForStream(query, new PSQLNaturalProductRowMapper(), args)){
               return  result.map(PSQLMapper.MAPPER::map).toList();
           } catch ( Exception e){
               e.printStackTrace();
               return new ArrayList<>();
           }
       }
     ```

2. Configuring limits for connection lifetime, timeout and leak detection for the Hikari connection pool using the production configuration file.

   ```properties
   #ontology-manager-server/src/main/resources/application-prod.properties
   spring.datasource.hikari.maximum-pool-size=20
   spring.datasource.hikari.minimum-idle=10
   spring.datasource.hikari.idle-timeout=30000
   spring.datasource.hikari.connection-timeout=30000
   spring.datasource.hikari.max-lifetime=1600000
   spring.datasource.hikari.leak-detection-threshold=2000
   ```

### Input Validation

- Query limits: All PostgreSQL queries have hard limits set to the upper bound of the expected results (.i.e. `MIN(input_limit, upper_bound_limit)`), this limits are enforced by the service layer so these limits are implemented independently from the database repositories implementation.
- Strings: All strings received are checked in the service layer searching for common SQL injection patterns.







