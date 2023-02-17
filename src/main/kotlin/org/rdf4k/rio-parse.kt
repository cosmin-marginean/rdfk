package org.rdf4k

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.impl.LinkedHashModel
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFHandler
import org.eclipse.rdf4j.rio.helpers.StatementCollector
import java.io.File
import java.io.InputStream

fun InputStream.parseRdf(format: RDFFormat, rdfHandler: RDFHandler) {
    this.use { inputStream ->
        Rdf4kParser(inputStream, format, rdfHandler).use { rdf4kParser ->
            rdf4kParser.parse()
        }
    }
}

fun InputStream.parseRdfIndexed(format: RDFFormat, handler: (index: Long, statement: Statement) -> Unit): Long {
    val rdfHandler = IndexedStatementHandler(handler)
    parseRdf(format, rdfHandler)
    return rdfHandler.statementCount
}

fun InputStream.toRdfStatements(format: RDFFormat): Collection<Statement> {
    val rdfHandler = StatementCollector()
    this.parseRdf(format, rdfHandler)
    return rdfHandler.statements
}

fun InputStream.toRdfModel(format: RDFFormat): Model {
    val model = LinkedHashModel()
    val rdfHandler = StatementCollector(model)
    this.parseRdf(format, rdfHandler)
    return model
}

fun File.parseRdf(format: RDFFormat, rdfHandler: RDFHandler) {
    this.inputStream().use { inputStream ->
        inputStream.parseRdf(format, rdfHandler)
    }
}

fun File.parseRdfIndexed(format: RDFFormat, handler: (index: Long, statement: Statement) -> Unit): Long {
    val rdfHandler = IndexedStatementHandler(handler)
    parseRdf(format, rdfHandler)
    return rdfHandler.statementCount
}

fun File.toRdfStatements(format: RDFFormat = fileRdfFormat(this.name)!!): Collection<Statement> {
    return this.inputStream().use { inputStream ->
        inputStream.toRdfStatements(format)
    }
}

fun File.toRdfModel(format: RDFFormat = fileRdfFormat(this.name)!!): Model {
    return this.inputStream().use { inputStream ->
        inputStream.toRdfModel(format)
    }
}

fun resourceToRdfModel(classpathLocation: String, format: RDFFormat = RDFFormat.TURTLE): Model {
    return resourceAsInput(classpathLocation).toRdfModel(format)
}

