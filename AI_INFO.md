The purpose of this project is to merge data from different databases and to 
create a curated and consolidated knowledge graph from this data to the main 
project. The main project will have some data records - entities and relations 
- of its own. This data may change over time, as will the records from foreign 
data sources. Therefore we want to track the data records in a source code 
repository and to provide versioned releases of the knowledge graph. The data 
will be centered around the knowledge graph triangle ORGANISM produces 
NATURAL COMPOUND, ORGANISM treats DISEASE and NATURAL PRODUCT treats DISEASE. 
Over time we want to include many more entities and relations, e.g. geo-location, 
involved enzymes etc.
To facilitate the merge and curation process, persistent identifiers need to be 
identified for the records of the original databases. For chemical structures, 
this could be the InChI or InChI-Key, for records taken from ontologies, this 
could be the IRI and in other cases we must keep track of the source 
identifiers.
The project is realized as a Spring Boot application to be run as a single 
plain commandline command. It shall have a configuration file (either JSON or 
Java Properties), which provide information about database connectivity, source 
URLs and other configuration data. The primary compilation of the data shall be 
done in a PostgreSQL database. The contents of that database is later to be 
transformed in a graph database (Neo4J). We need to make sure, the identifiers 
for entities and relations are persistent within our project. Otherwise it 
would be impossible to compare different releases or to cite the results of the 
main application. The merging or compilation step also need to make sure to 
treat records correctly, which have been marked obsolete or incorrect in their 
original sources or which have been deleted.
Some records of the database will include chemical structure information. While 
the main application requires substructure searches in its data this is not 
necessary for the curation database.

The application should basically use Hibernate as an ORM framework for database 
access. As we have some non-standard column types in the database, we do not 
want Spring to automatically adjust the database structure. Instead we want to 
provide a set of SQL scripts for database setup. Currently the schema is stored
in the src/test/resources/schema directory. This may change later. We 
also want to structure the project along the different source databases. Java 
models, DTOs, interfaces and services for database access should be stored in 
their business case package, in contrast to storing all the models in a model 
package, the interfaces in an interface package etc.  The whole application 
must be well covered by unit tests. The tests should use TestContainers for 
testing where appropriate. To keep things manageable, the project should use a 
good compromize in using as few layers of abstraction as possible, while 
keeping a clean code an observing the SOLID principles: single responsibility, 
open/closed principle, Liskov substituion principle, Interface segregation 
principle, dependency inversion principle.
Furthermore the coding style must be explicit. In general that means 
configuration is preferred over convention.  The goal is to make things 
understandable and traceable at the expense of a few more lines of 
code even for people unaware of the conventions. As an example, automatic 
generation of queries from method names is forbidden.
