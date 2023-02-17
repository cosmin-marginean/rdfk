package org.rdf4k

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriter
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
    use: (RDFWriter) -> Unit
) {
    outputStream().useRdfWriter(format, namespaces, use)
}

fun OutputStream.useRdfWriter(
    format: RDFFormat,
    namespaces: List<Namespace> = emptyList(),
    use: (RDFWriter) -> Unit
) {
    this.use { outputStream ->
        Rdf4kWriter(format, outputStream, namespaces).use { rdf4kWriter ->
            use(rdf4kWriter.rdfWriter)
        }
    }
}
