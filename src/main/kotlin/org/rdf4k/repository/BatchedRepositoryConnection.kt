package org.rdf4k.repository

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.rdf4k.statement

class ConnectionStatementBatch(
    private val delegate: RepositoryConnection,
    private val batchSize: Int
) : AutoCloseable {

    private val statements = mutableListOf<Statement>()

    fun add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
        add(statement(s, p, o, graph))
    }

    fun add(statements: Collection<Statement>) {
        statements.forEach { statement: Statement ->
            add(statement)
        }
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
