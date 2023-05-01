package org.rdf4k.repository

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.rdf4k.statement

fun RepositoryConnection.add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
    this.add(statement(s, p, o, graph))
}

fun RepositoryConnection.useBatch(batchSize: Int, use: (StatementsBatch) -> Unit) {
    StatementsBatch(this, batchSize).use(use)
}
