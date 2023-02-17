package org.rdfk.query

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.query.BindingSet

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
