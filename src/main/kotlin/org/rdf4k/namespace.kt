package org.rdf4k

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.model.impl.SimpleNamespace

fun String.namespace(prefix: String): Namespace {
    return SimpleNamespace(prefix, this)
}

fun Namespace.iri(resource: String): IRI {
    return valueFactory.createIRI(this.name, resource)
}