package org.rdf4k

import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler
import org.rdf4k.TestContainer.Companion.repository
import org.rdf4k.rio.*
import java.io.File
import java.io.InputStream

class ExamplesDetailed {

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
        val MY_NAMESPACE = "http://test.com/".namespace("res")

        // Write statements to an RDF repository
        repository.add(resourceAsRdfModel("input.ttl"))

        // Write statements to an RDF repository in batches
        repository.withStatementsBatch(10_000) { batch ->
            batch.add(resourceAsRdfModel("input.ttl"))
        }

        // Querying
        repository.sparqlSelect("SELECT ?s ?p ?o WHERE { ?s ?p ?o . }", "s" to MY_NAMESPACE.iri("one"))
                .forEach { row ->
                    println(row.iri("p"))
                    println(row.str("o"))
                }

        // Querying using a .sparql from classpath
        repository.sparqlSelectClasspath("queries/tuple-query.sparql",
                "s" to MY_NAMESPACE.iri("one")
        )
        repository.sparqlGraphClasspath("queries/graph-query.sparql",
                "s" to MY_NAMESPACE.iri("one")
        )
    }
}
