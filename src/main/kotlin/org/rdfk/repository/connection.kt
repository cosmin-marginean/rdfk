package org.rdfk.repository

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.rdfk.statement

fun RepositoryConnection.add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
    this.add(statement(s, p, o, graph))
}

fun RepositoryConnection.useBatch(batchSize: Int, use: (ConnectionStatementBatch) -> Unit) {
    ConnectionStatementBatch(this, batchSize).use(use)
}
