/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.ontology;

import java.io.InputStream;
import java.io.UncheckedIOException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 *
 * @author fblocal
 */
public class DatasetBuilder {

    /**
     * Creates a {@link Dataset} from the OWL file with the given name. The
     * file is looked up on the classpath relative to this class (i.e. in the
     * same package).
     *
     * @param owlFile the name of the OWL resource to load
     * @return a {@link Dataset} whose default model contains the statements
     * read from {@code owlFile}
     */
    public Dataset createDataset(String owlFile) {
        try (InputStream input = getClass().getResourceAsStream(owlFile)) {
            if (input == null) {
                throw new IllegalArgumentException("OWL resource not found: " + owlFile);
            }
            Model model = ModelFactory.createDefaultModel();
            RDFDataMgr.read(model, input, Lang.RDFXML);
            return DatasetFactory.wrap(model);
        } catch (java.io.IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    /*
   import java.io.IOException;
   import java.io.InputStream;
   import java.io.UncheckedIOException;
   import java.nio.file.Files;
   import java.nio.file.Path;
   import java.util.Comparator;
   import org.apache.jena.query.Dataset;
   import org.apache.jena.query.ReadWrite;
   import org.apache.jena.riot.Lang;
   import org.apache.jena.riot.RDFDataMgr;
   import org.apache.jena.tdb2.TDB2Factory;

   public class TdbDatasetBuilder implements AutoCloseable {

       private final Path location;
       private final Dataset dataset;

       private TdbDatasetBuilder(Path location, Dataset dataset) {
           this.location = location;
           this.dataset = dataset;

       private TdbDatasetBuilder(Path location, Dataset dataset) {
           this.location = location;
           this.dataset = dataset;
       }

       // Creates a fresh, private TDB2 store in a temp directory and loads owlFile into it.
       public static TdbDatasetBuilder create(String owlFile) {
           try {
               Path location = Files.createTempDirectory("curator-tdb2-");
               Dataset dataset = TDB2Factory.connectDataset(location.toString());
               try (InputStream input = TdbDatasetBuilder.class.getResourceAsStream(owlFile)) {
                   if (input == null) {
                       throw new IllegalArgumentException("OWL resource not found: " + owlFile);
                   }
                   dataset.begin(ReadWrite.WRITE);
                   try {
                       RDFDataMgr.read(dataset.getDefaultModel(), input, Lang.RDFXML);
                       dataset.commit();
                   } finally {
                       dataset.end();
                   }
                }
               return new TdbDatasetBuilder(location, dataset);
           } catch (IOException e) {
               throw new UncheckedIOException(e);
           }
       }

       public Dataset getDataset() {
           return dataset;
       }

       @Override
       public void close() {
           dataset.close();
           try (var walk = Files.walk(location)) {
               walk.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
           } catch (IOException ignored) {
               // best-effort cleanup
           }
       }
   }


Key points:

   1. One temp directory per ontology.  Files.createTempDirectory  gives each  Dataset  its own exclusive TDB2 location — TDB2 takes a file lock per location, so concurrently open datasets must never share a directory. Keep
      a  Map<String, TdbDatasetBuilder>  keyed by ontology id for your "multiple ontologies in parallel" case.
   2. Transactions are mandatory for TDB2 (unlike the in-memory  Dataset , which silently no-ops them). Every read/write — including in  QueryExecutor.queryDataset — must be wrapped:  dataset.begin(ReadWrite.READ); try {
      ... } finally { dataset.end(); }  (or the  Txn.executeRead(dataset, …)  helper in  org.apache.jena.system.Txn ). Wrapping consistently means the same  QueryExecutor  code works unchanged against both in-memory and
      TDB2 datasets.
   3. Concurrency model: TDB2 allows many concurrent readers but only one writer at a time per location — fine since each ontology has its own location/store.
   4. Cleanup: implement  AutoCloseable  so  try (var b = TdbDatasetBuilder.create(...))  reliably closes the dataset and deletes the temp directory; also consider a JVM shutdown hook as a safety net if the process dies
      uncleanly.
   5. Disk location:  Files.createTempDirectory  honors  java.io.tmpdir  — point that at fast/large storage via  -Djava.io.tmpdir=...  if the default partition is too small for a full taxonomy dump.

   This is sketched, not compiled (no JDK here) — please run  mvn test  with a quick  TdbDatasetBuilderTest  and send me the output.


    */

}
