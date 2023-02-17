package org.rdfk

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.model.util.Statements.statement

fun statement(s: Resource, p: IRI, o: Value, graph: IRI? = null): Statement {
    return valueFactory.createStatement(s, p, o, graph)
}

fun MutableCollection<Statement>.add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
    this.add(statement(s, p, o, graph))
}
