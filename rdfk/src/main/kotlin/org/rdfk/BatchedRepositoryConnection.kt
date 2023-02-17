package org.rdfk

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.repository.RepositoryConnection

class ConnectionStatementBatch(
    val delegate: RepositoryConnection,
    val batchSize: Int
) : AutoCloseable {

    private val statements = mutableListOf<Statement>()

    fun add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
        add(statement(s, p, o, graph))
    }

    fun add(statement: Statement) {
        statements.add(statement)
        if (statements.size == batchSize) {
            delegate.add(statements)
            statements.clear()
        }
    }

    override fun close() {
        if (statements.isNotEmpty()) {
            delegate.add(statements)
        }
    }
}