package org.rdf4k

import org.eclipse.rdf4j.repository.RepositoryConnection

fun RepositoryConnection.useBatch(batchSize: Int, use: (StatementsBatch) -> Unit) {
    StatementsBatch(this, batchSize).use(use)
}
