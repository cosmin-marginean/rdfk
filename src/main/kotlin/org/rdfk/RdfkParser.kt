package org.rdfk

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFHandler
import org.eclipse.rdf4j.rio.Rio
import java.io.InputStream

class RdfkParser(
    val inputstream: InputStream,
    val format: RDFFormat,
    val rdfHandler: RDFHandler
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