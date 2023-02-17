package org.rdf4k

import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler

class IndexedStatementHandler(private val handler: (Long, Statement) -> Unit) : AbstractRDFHandler() {

    private var indexed = 0L

    override fun handleStatement(st: Statement) {
        handler(indexed, st)
        indexed++
    }

    val statementCount: Long get() = indexed
}