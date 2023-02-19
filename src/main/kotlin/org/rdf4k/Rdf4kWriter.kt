package org.rdf4k

import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.Rio
import java.io.BufferedOutputStream
import java.io.OutputStream

class Rdf4kWriter(
    val format: RDFFormat,
    val outputStream: OutputStream,
    val namespaces: List<Namespace> = emptyList(),
    val bufferSize: Int = 8192
) : AutoCloseable {

    val rdfWriter = Rio.createWriter(format, BufferedOutputStream(outputStream, bufferSize))

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