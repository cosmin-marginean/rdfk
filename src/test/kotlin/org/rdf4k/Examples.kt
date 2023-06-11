package org.rdf4k

import org.eclipse.rdf4j.model.impl.SimpleValueFactory
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio
import org.rdf4k.rio.useRdfWriter
import org.rdf4k.rio.write
import java.io.File

class Examples {

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
                            valueFactory.createLiteral("Angela Smith")
                    )
            )
            rdfWriter.endRDF()
        }
    }

    fun `writer - new`() {
        val outputFile = File("...")

        val RES = "http://test.com/res/".namespace("res")
        val DEFS = "http://test.com/defs/".namespace("defs")
        outputFile.useRdfWriter(RDFFormat.TURTLE, listOf(RES, DEFS)) { rdfWriter ->
            rdfWriter.write(RES.iri("one"), DEFS.iri("name"), "John Smith".literal())
            rdfWriter.write(RES.iri("two"), DEFS.iri("name"), "Angela Smith".literal())
        }
    }
}