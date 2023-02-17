package org.rdfk

import org.eclipse.rdf4j.model.impl.SimpleValueFactory
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio
import java.io.File

class Examples : RdfContainerTest() {

    fun `writer - old`() {
        val outputFile = File("...")

        val T1 = "http://test1.com/"
        val T2 = "http://test2.com/"
        val valueFactory = SimpleValueFactory.getInstance()
        outputFile.outputStream().use { outputStream ->
            val rdfWriter = Rio.createWriter(RDFFormat.TURTLE, outputStream)
            rdfWriter.startRDF()
            rdfWriter.handleNamespace("t1", T1)
            rdfWriter.handleNamespace("t2", T2)
            rdfWriter.handleStatement(
                valueFactory.createStatement(
                    valueFactory.createIRI(T1, "one"),
                    valueFactory.createIRI(T2, "name"),
                    valueFactory.createLiteral("John Smith")
                )
            )
            rdfWriter.handleStatement(
                valueFactory.createStatement(
                    valueFactory.createIRI(T1, "two"),
                    valueFactory.createIRI(T2, "name"),
                    valueFactory.createLiteral("Angela White")
                )
            )
            rdfWriter.endRDF()
        }
    }

    fun `writer - new`() {
        val outputFile = File("...")

        val N1 = "http://test1.com/".namespace("t1")
        val N2 = "http://test2.com/".namespace("t2")
        outputFile.useRdfWriter(RDFFormat.TURTLE, listOf(N1, N2)) { rdfWriter ->
            rdfWriter.write(N1.iri("one"), N2.iri("name"), "John Smith".literal())
            rdfWriter.write(N1.iri("two"), N2.iri("name"), "Angela Smith".literal())
        }
    }

}