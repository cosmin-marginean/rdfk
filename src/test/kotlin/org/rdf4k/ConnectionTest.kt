package org.rdf4k

import org.rdf4k.query.*
import org.rdf4k.repository.useBatch
import org.rdf4k.repository.withStatementsBatch
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.annotations.Test

class ConnectionTest : RdfContainerTest() {

    @Test
    fun `repository use batch`() {
        repository.withStatementsBatch(10) { batch ->
            batch.add(resourceToRdfModel("test-input.ttl"))
        }
        val subject = "http://bods.openownership.org/resource/openownership-register-5450813549318202701".iri()
        val rows = repository.connection.use { connection ->
            connection.prepareTupleQueryClasspath("input-test-query-1.sparql")
                .bindings("s" to subject)
                .evaluate()
                .toList()
        }
        assertEquals(rows.size, 11)
        assertEquals(
            rows.find { it.iri("p") == "http://bods.openownership.org/vocabulary/statementId".iri() }!!.str("o"),
            "openownership-register-5450813549318202701"
        )
    }

    @Test
    fun `connection use batch`() {
        repository.connection.use { connection ->
            connection.useBatch(10) { batch ->
                batch.add(resourceToRdfModel("test-input.ttl"))
            }
        }
        val subject = "http://bods.openownership.org/resource/openownership-register-5450813549318202701".iri()
        val rows = repository.connection.use { connection ->
            connection.prepareTupleQueryClasspath("input-test-query-1.sparql")
                .bindings("s" to subject)
                .evaluate()
                .toList()
        }
        assertEquals(rows.size, 11)
        assertEquals(
            rows.find { it.iri("p") == "http://bods.openownership.org/vocabulary/statementId".iri() }!!.str("o"),
            "openownership-register-5450813549318202701"
        )
    }

    @Test
    fun `add statement to batch`() {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.connection.use { connection ->
            connection.prepareTupleQueryClasspath("input-test-query-1.sparql")
                .bindings("s" to T1.iri("one"))
                .evaluate()
                .toList()
        }
        assertEquals(rows.size, 3)
        assertEquals(
            rows.find { it.iri("p") == T2.iri("name") }!!.str("o"),
            "John Smith"
        )
        assertEquals(
            rows.find { it.iri("p") == T2.iri("age") }!!.int("o"),
            23
        )
    }

    @Test
    fun `add statement to connection`() {
        repository.connection.use { connection ->
            connection.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            connection.add(T1.iri("one"), T2.iri("age"), "23".literal())
            connection.add(T1.iri("one"), T2.iri("enabled"), false.literal())
        }
        val rows = repository.connection.use { connection ->
            connection.prepareTupleQueryClasspath("input-test-query-1.sparql")
                .bindings("s" to T1.iri("one"))
                .evaluate()
                .toList()
        }
        assertEquals(rows.size, 4)
        assertEquals(
            rows.find { it.iri("p") == T2.iri("name") }!!.str("o"),
            "John Smith"
        )
        assertEquals(
            rows.find { it.iri("p") == T2.iri("age") }!!.long("o"),
            23
        )
        assertFalse(rows.find { it.iri("p") == T2.iri("enabled") }!!.boolean("o"))
    }
}
