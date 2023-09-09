package org.rdf4k

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.rdf4k.TestContainer.Companion.createRepository
import org.rdf4k.TestContainer.Companion.getRepository
import org.rdf4k.TestContainer.Companion.repository
import org.rdf4k.rio.resourceAsRdfModel
import java.util.*

class RepositoryTest : StringSpec({

    "withStatementsBatch - add TTL to batch" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(resourceAsRdfModel("test-input.ttl"))
        }
        val subject = "http://bods.openownership.org/resource/openownership-register-5450813549318202701".toIri()

        val rows = repository.sparqlSelectClasspath("query-select.sparql", "s" to subject)
        rows.size shouldBe 11
        rows.find { it.iri("p") == "http://bods.openownership.org/vocabulary/statementId".toIri() }!!
            .str("o") shouldBe "openownership-register-5450813549318202701"
    }

    "withStatementsBatch - add statements to batch" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.sparqlSelectClasspath("query-select.sparql", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.iri("p") == T2.iri("name") }!!.str("o") shouldBe "John Smith"
        rows.find { it.iri("p") == T2.iri("age") }!!.int("o") shouldBe 23
    }

    "add statements" {
        repository.add(
            listOf(
                statement(T1.iri("one"), T2.iri("name"), "John Smith".literal()),
                statement(T1.iri("one"), T2.iri("age"), "23".literal())
            )
        )

        val rows = repository.sparqlSelectClasspath("query-select.sparql", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.iri("p") == T2.iri("name") }!!.str("o") shouldBe "John Smith"
        rows.find { it.iri("p") == T2.iri("age") }!!.int("o") shouldBe 23
    }

    "create repository" {
        val repositoryId = "test-${UUID.randomUUID()}"
        createRepository(repositoryId) shouldBe true
        getRepository(repositoryId) shouldNotBe null
        createRepository(repositoryId) shouldBe false
    }
})