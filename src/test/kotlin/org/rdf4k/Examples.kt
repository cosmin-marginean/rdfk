package org.rdf4k

import org.eclipse.rdf4j.model.impl.SimpleValueFactory
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio
import java.io.File

class Examples : RdfContainerTest() {

    fun `writer - old`() {
        val outputFile = File("...")

        val RES = "http://test.com/res/"
        val DEFS = "http://test.com/defs/"
        val valueFactory = SimpleValueFactory.getInstance()
        outputFile.outputStream().use { outputStream ->
            val rdfWriter = Rio.createWriter(RDFFormat.TURTLE, outputStream)
            rdfWriter.startRDF()
            rdfWriter.handleNamespace("res", RES)
            rdfWriter.handleNamespace("defs", DEFS)
            rdfWriter.handleStatement(
                valueFactory.createStatement(
                    valueFactory.createIRI(RES, "one"),
                    valueFactory.createIRI(DEFS, "name"),
                    valueFactory.createLiteral("John Smith")
                )
            )
            rdfWriter.handleStatement(
                valueFactory.createStatement(
                    valueFactory.createIRI(RES, "two"),
                    valueFactory.createIRI(DEFS, "name"),
                    valueFactory.createLiteral("Angela White")
                )
            )
            rdfWriter.endRDF()
        }
    }

    fun `writer - new`() {
        val outputFile = File("...")

        val RES = "http://test.com/res/".namespace("res")
        val DEFS = "http://test.com/defs/".namespace("defs")
        outputFile.useRdfWriter(RDFFormat.TURTLE, listOf(T1, T2)) { rdfWriter ->
            rdfWriter.write(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            rdfWriter.write(T1.iri("two"), T2.iri("name"), "Angela Smith".literal())
        }
    }

}