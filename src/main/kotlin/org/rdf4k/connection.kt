package org.rdf4k

import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.slf4j.LoggerFactory
import java.io.File

private val log = LoggerFactory.getLogger("org.rdf4k.connection")

fun RepositoryConnection.useBatch(batchSize: Int, use: (StatementsBatch) -> Unit) {
    StatementsBatch(this, batchSize).use(use)
}

fun RepositoryConnection.tryAdd(statements: Collection<Statement>) {
    try {
        add(statements)
    } catch (t: Throwable) {
        log.error("Error adding RDF statements: ${t.message}", t)
        val tempFile = File(System.getProperty("user.dir"), "rdf-failed-write-${System.nanoTime()}.txt")
        tempFile.writeText(statements.joinToString("\n"))
        throw t
    }
}
