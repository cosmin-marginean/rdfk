package org.rdf4k.rio

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFHandler
import org.eclipse.rdf4j.rio.Rio
import java.io.InputStream

class Rdf4kParser(
    val inputstream: InputStream,
    format: RDFFormat,
    rdfHandler: RDFHandler
) : AutoCloseable {

    val rdfParser = Rio.createParser(format)

    init {
        rdfParser.setRDFHandler(rdfHandler)
    }

    internal fun parse() {
        rdfParser.parse(inputstream)
    }

    override fun close() {
        inputstream.close()
    }
}