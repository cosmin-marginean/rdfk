package org.rdf4k.rio

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.eclipse.rdf4j.model.IRI
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriter
import org.rdf4k.*
import org.rdf4k.internals.resourceAsString
import org.testcontainers.shaded.org.apache.commons.io.output.ByteArrayOutputStream
import java.io.File
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class RioWriteTest : StringSpec() {

    init {
        "use file rdf writer" {
            val outputFile = File(UUID.randomUUID().toString())
            outputFile.useRdfWriter(RDFFormat.TURTLE, TEST_NAMESPACES) { rdfWriter ->
                writeStatements(rdfWriter)
            }
            outputFile.readText() shouldBe resourceAsString("test-output.ttl")
            outputFile.delete()
        }

        "use file rdf writer - no namespaces" {
            val outputFile = File(UUID.randomUUID().toString())
            outputFile.useRdfWriter(RDFFormat.TURTLE) { rdfWriter ->
                writeStatements(rdfWriter)
            }
            outputFile.readText() shouldBe resourceAsString("test-output-no-namespaces.ttl")
            outputFile.delete()
        }

        "use outputstream rdf writer" {
            val outputStream = ByteArrayOutputStream()
            outputStream.useRdfWriter(RDFFormat.TURTLE, TEST_NAMESPACES) { rdfWriter ->
                writeStatements(rdfWriter)
            }
            String(outputStream.toByteArray()) shouldBe resourceAsString("test-output.ttl")
        }

        "use outputstream rdf writer - no namespaces" {
            val outputStream = ByteArrayOutputStream()
            outputStream.useRdfWriter(RDFFormat.TURTLE) { rdfWriter ->
                writeStatements(rdfWriter)
            }
            String(outputStream.toByteArray()) shouldBe resourceAsString("test-output-no-namespaces.ttl")
        }

        "use graph" {
            val outputFile = File(UUID.randomUUID().toString())
            outputFile.useRdfWriter(RDFFormat.TRIG, TEST_NAMESPACES) { rdfWriter ->
                writeStatements(rdfWriter, T1.iri("test-graph"))
            }

            outputFile.readLines().toList().filter { it.trim().isNotEmpty() }.joinToString("\n") shouldBe
                    resourceAsString("test-output-with-graph.trig").split("\n").filter { it.trim().isNotEmpty() }.joinToString("\n")
            outputFile.delete()
        }

    }

    private fun writeStatements(rdfWriter: RDFWriter, graph: IRI? = null) {
        val date: Date = Date.from(Instant.parse("2001-12-17T16:51:14.691Z"))
        val localDateTime: LocalDateTime = LocalDateTime.parse("2002-04-02T09:51:14.721")
        val localDate: LocalDate = LocalDate.parse("2021-06-29")
        val instant: Instant = Instant.parse("1983-11-04T12:33:55.002Z")

        rdfWriter.handleStatement(statement(T1.iri("one"), T2.iri("hasName"), "John Smith".literal(), graph))

        val statements = mutableListOf<Statement>()
        statements.add(T1.iri("two"), T2.iri("hasName"), "Jane Smith".literal(), graph)
        statements.add(T1.iri("two"), T2.iri("hasAge"), 23.literal(), graph)
        statements.add(T1.iri("two"), T2.iri("hasAge"), 23.literal(), graph)
        rdfWriter.write(statements)

        rdfWriter.write(T1.iri("three"), T2.iri("hasName"), "Angela Smith".literal())
        rdfWriter.write(T1.iri("three"), T2.iri("hasWeight"), 3.23412.literal())
        rdfWriter.write(T1.iri("three"), T2.iri("hasHeight"), 13.12938015.literalDecimal())
        rdfWriter.write(T1.iri("three"), T2.iri("isEmployed"), true.literal())
        rdfWriter.write(T1.iri("three"), T2.iri("lastChecked"), date.literal())
        rdfWriter.write(T1.iri("three"), T2.iri("recordTimestamp"), localDateTime.literal())
        rdfWriter.write(T1.iri("three"), T2.iri("modifiedDate"), localDate.literal())
        rdfWriter.write(T1.iri("three"), T2.iri("createdDate"), instant.literal())

        listOf(
                statement(T1.iri("x"), T1.iri("name"), "Tom".literal()),
                statement(T1.iri("y"), T1.iri("name"), "Jane".literal())
        ).writeTo(rdfWriter)
    }
}
