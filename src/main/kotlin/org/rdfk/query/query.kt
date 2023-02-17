package org.rdfk.query

import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.query.Query
import org.eclipse.rdf4j.query.TupleQuery
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.rdfk.resourceAsString

inline fun <reified T : Query> T.bindings(vararg bindings: Pair<String, Value>): T {
    bindings.forEach { (name, value) ->
        this.setBinding(name, value)
    }
    return this
}

inline fun <reified T : Query> T.bindings(bindings: Map<String, Value>): T {
    bindings.forEach { (name, value) ->
        this.setBinding(name, value)
    }
    return this
}

fun RepositoryConnection.prepareTupleQueryClasspath(classpathLocation: String): TupleQuery {
    return this.prepareTupleQuery(resourceAsString(classpathLocation))
}
