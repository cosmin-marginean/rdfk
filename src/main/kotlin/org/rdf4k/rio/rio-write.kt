package org.rdf4k.rio

import org.eclipse.rdf4j.model.*
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriter
import org.rdf4k.statement
import java.io.File
import java.io.OutputStream

fun RDFWriter.write(statements: Collection<Statement>) {
    statements.forEach { statement ->
        handleStatement(statement)
    }
}

fun RDFWriter.write(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
    handleStatement(statement(s, p, o, graph))
}

fun Collection<Statement>.writeTo(writer: RDFWriter) {
    writer.write(this)
}

fun File.useRdfWriter(
    format: RDFFormat,
    namespaces: List<Namespace> = emptyList(),
    bufferSize: Int = 8192,
    use: (RDFWriter) -> Unit
) {
    outputStream().useRdfWriter(format, namespaces, bufferSize, use)
}

fun OutputStream.useRdfWriter(
    format: RDFFormat,
    namespaces: List<Namespace> = emptyList(),
    bufferSize: Int = 8192,
    use: (RDFWriter) -> Unit
) {
    this.use { outputStream ->
        Rdf4kWriter(format, outputStream, namespaces, bufferSize).use { rdf4kWriter ->
            use(rdf4kWriter.rdfWriter)
        }
    }
}
