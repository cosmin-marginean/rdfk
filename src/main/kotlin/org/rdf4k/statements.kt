package org.rdf4k

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Resource
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.model.impl.SimpleValueFactory
import org.eclipse.rdf4j.model.util.Statements.statement
import org.eclipse.rdf4j.model.vocabulary.XSD
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

internal val valueFactory = SimpleValueFactory.getInstance()

fun String.literal(): Value = valueFactory.createLiteral(this)
fun Int.literal(): Value = valueFactory.createLiteral(this)
fun Double.literal(): Value = valueFactory.createLiteral(this)
fun Double.literalDecimal(): Value = valueFactory.createLiteral(this.toString(), XSD.DECIMAL)
fun Boolean.literal(): Value = valueFactory.createLiteral(this)
fun String.toIri(): IRI = valueFactory.createIRI(this)
fun Date.literal() = valueFactory.createLiteral(this)
fun LocalDate.literal() = valueFactory.createLiteral(this)
fun LocalDateTime.literal() = valueFactory.createLiteral(this)
fun Instant.literal() = valueFactory.createLiteral(this.atZone(ZoneOffset.UTC))

fun statement(s: Resource, p: IRI, o: Value, graph: IRI? = null): Statement {
    return valueFactory.createStatement(s, p, o, graph)
}

fun MutableCollection<Statement>.add(s: Resource, p: IRI, o: Value, graph: IRI? = null) {
    this.add(statement(s, p, o, graph))
}
