package org.rdf4k

import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Value
import org.eclipse.rdf4j.model.impl.SimpleValueFactory
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
fun String.iri(): IRI = valueFactory.createIRI(this)
fun Date.literal() = valueFactory.createLiteral(this)
fun LocalDate.literal() = valueFactory.createLiteral(this)
fun LocalDateTime.literal() = valueFactory.createLiteral(this)
fun Instant.literal() = valueFactory.createLiteral(this.atZone(ZoneOffset.UTC))
