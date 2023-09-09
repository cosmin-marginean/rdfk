package org.rdf4k

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.eclipse.rdf4j.repository.RepositoryException
import org.rdf4k.TestContainer.Companion.repository
import org.rdf4k.internals.resourceAsString
import org.rdf4k.rio.resourceAsRdfModel
import java.io.File

class ConnectionTest : StringSpec({

    "useBatch" {
        repository.connection.use { connection ->
            connection.useBatch(10) { batch ->
                batch.add(resourceAsRdfModel("test-input.ttl"))
            }
        }
        val subject = "http://bods.openownership.org/resource/openownership-register-5450813549318202701".toIri()
        val rows = repository.sparqlSelectClasspath(
            "query-select.sparql",
            "s" to subject,
        )
        rows.size shouldBe 11
        rows.find { it.iri("p") == "http://bods.openownership.org/vocabulary/statementId".toIri() }!!
            .str("o") shouldBe "openownership-register-5450813549318202701"
    }


    "add statements" {
        repository.connection.use { connection ->
            connection.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            connection.add(T1.iri("one"), T2.iri("age"), "23".literal())
            connection.add(T1.iri("one"), T2.iri("enabled"), false.literal())
        }
        val rows = repository.sparqlSelectClasspath("query-select.sparql", "s" to T1.iri("one"))
        rows.size shouldBe 4
        rows.find { it.iri("p") == T2.iri("name") }!!.str("o") shouldBe "John Smith"
        rows.find { it.iri("p") == T2.iri("age") }!!.long("o") shouldBe 23
        rows.find { it.iri("p") == T2.iri("enabled") }!!.boolean("o") shouldBe false
    }

    "try add" {
        shouldThrow<RepositoryException> {
            repository.connection.use { connection ->
                connection.tryAdd(
                    listOf(
                        statement(T1.iri("one"), T2.iri("^&*"), "John Smith".literal()),
                        statement(T1.iri("one"), T2.iri("age"), "23".literal())
                    )
                )
            }
        }
        val dumpFile = File(System.getProperty("user.dir")).listFiles()
            .find { it.name.startsWith("rdf-failed-write") }!!
        dumpFile.readText() shouldBe resourceAsString("failed-add-file-contents.txt")
        dumpFile.delete()
    }
})