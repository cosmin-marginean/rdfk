package org.rdf4k

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.query.BindingSet
import org.eclipse.rdf4j.query.Query
import org.eclipse.rdf4j.repository.Repository
import org.rdf4k.internals.resourceAsString

fun <T : Query> T.bindings(vararg bindings: Pair<String, Value>): T {
    bindings.forEach { (name, value) ->
        this.setBinding(name, value)
    }
    return this
}

fun <T : Query> T.bindings(bindings: Map<String, Value>): T {
    bindings.forEach { (name, value) ->
        this.setBinding(name, value)
    }
    return this
}

fun BindingSet.str(name: String): String {
    return this.getValue(name).stringValue()
}

fun BindingSet.int(name: String): Int {
    return this.getValue(name).stringValue().toInt()
}

fun BindingSet.long(name: String): Long {
    return this.getValue(name).stringValue().toLong()
}

fun BindingSet.boolean(name: String): Boolean {
    return this.getValue(name).stringValue().toBoolean()
}

fun BindingSet.iri(name: String): IRI {
    return this.getValue(name) as IRI
}

fun Repository.sparqlSelect(query: String, vararg bindings: Pair<String, Value>): List<BindingSet> {
    return connection.use { c ->
        val q = c.prepareTupleQuery(query)
        q.bindings(*bindings)
        q.evaluate().toList()
    }
}

fun Repository.sparqlSelectClasspath(classpathQuery: String, vararg bindings: Pair<String, Value>): List<BindingSet> {
    return sparqlSelect(resourceAsString(classpathQuery), *bindings)
}

fun Repository.sparqlGraph(query: String, vararg bindings: Pair<String, Value>): List<Statement> {
    return connection.use { c ->
        val q = c.prepareGraphQuery(query)
        q.bindings(*bindings)
        q.evaluate().toList()
    }
}

fun Repository.sparqlGraphClasspath(classpathQuery: String, vararg bindings: Pair<String, Value>): List<Statement> {
    return sparqlGraph(resourceAsString(classpathQuery), *bindings)
}
