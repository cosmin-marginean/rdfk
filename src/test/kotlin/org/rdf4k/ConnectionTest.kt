package org.rdf4k

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.rdf4k.TestContainer.Companion.repository
import org.rdf4k.rio.resourceAsRdfModel

class ConnectionTest : StringSpec({

    "useBatch" {
        repository.connection.use { connection ->
            connection.useBatch(10) { batch ->
                batch.add(resourceAsRdfModel("test-input.ttl"))
            }
        }
        val subject = "http://bods.openownership.org/resource/openownership-register-5450813549318202701".iri()
        val rows = repository.sparqlSelectClasspath(
                "query-select.sparql",
                "s" to subject,
        )
        rows.size shouldBe 11
        rows.find { it.iri("p") == "http://bods.openownership.org/vocabulary/statementId".iri() }!!.str("o") shouldBe "openownership-register-5450813549318202701"
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
})