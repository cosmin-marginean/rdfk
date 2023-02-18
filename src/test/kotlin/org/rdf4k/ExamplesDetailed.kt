package org.rdf4k

import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler
import org.rdf4k.query.bindings
import org.rdf4k.query.iri
import org.rdf4k.query.prepareTupleQueryClasspath
import org.rdf4k.query.str
import org.rdf4k.repository.useConnectionBatch
import java.io.File
import java.io.InputStream

class ExamplesDetailed : RdfContainerTest() {

    fun `Reading RDF`() {

        val inputStream: InputStream = File("input.ttl").inputStream()

        // Read an RDF file
        File("input.ttl").parseRdfIndexed(RDFFormat.TURTLE) { index, statement ->
            println(statement)
        }

        // Read with custom RDF handler
        val myRdfHandler = object : AbstractRDFHandler() {
            override fun handleStatement(st: Statement) {
                println(st)
            }
        }
        inputStream.parseRdf(RDFFormat.TURTLE, myRdfHandler)
    }

    fun `Writing RDF`() {
        val RES = "http://test.com/res/".namespace("res")
        val DEFS = "http://test.com/defs/".namespace("defs")
        File("input.ttl").useRdfWriter(RDFFormat.TURTLE, listOf(RES, DEFS)) { rdfWriter ->
            val statements = mutableListOf<Statement>()
            statements.add(RES.iri("one"), DEFS.iri("name"), "John Smith".literal())
            statements.add(RES.iri("two"), DEFS.iri("name"), "Angela Smith".literal())
            statements.add(RES.iri("two"), DEFS.iri("age"), 23.literal())
            rdfWriter.write(statements)
        }
    }

    fun `Repository connection`() {
        val NAMESPACE_RES = "http://test.com/".namespace("res")

        // Write statements to an RDF repository connection in batches
        repository.useConnectionBatch(10_000) { batch ->
            batch.add(resourceToRdfModel("input.ttl"))
        }

        // Querying
        repository.connection.use { connection ->
            connection.prepareTupleQuery("SELECT ?s ?p ?o WHERE { ?s ?p ?o . }")
                .bindings("s" to NAMESPACE_RES.iri("one"))
                .evaluate()
                .forEach { row->
                    println(row.iri("p"))
                    println(row.str("o"))
                }

        }

        // Querying using a .sparql from classpath
        repository.connection.use { connection ->
            connection.prepareTupleQueryClasspath("queries/query.sparql")
                .bindings("s" to NAMESPACE_RES.iri("one"))
                .evaluate()
        }
    }
}
