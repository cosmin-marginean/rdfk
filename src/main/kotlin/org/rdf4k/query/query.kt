package org.rdf4k.query

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.query.BindingSet
import org.eclipse.rdf4j.query.Query
import org.eclipse.rdf4j.query.TupleQuery
import org.eclipse.rdf4j.repository.RepositoryConnection
import org.rdf4k.resourceAsString

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
