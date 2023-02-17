package org.rdf4k

import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio
import java.io.OutputStream

class Rdf4kWriter(
    val format: RDFFormat,
    val outputStream: OutputStream,
    val namespaces: List<Namespace> = emptyList()
) : AutoCloseable {

    val rdfWriter = Rio.createWriter(format, outputStream)

    init {
        rdfWriter.startRDF()
        namespaces.forEach { namespace ->
            rdfWriter.handleNamespace(namespace.prefix, namespace.name)
        }
    }

    override fun close() {
        rdfWriter.endRDF()
    }
}