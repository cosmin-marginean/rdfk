package org.rdf4k

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.rdf4k.TestContainer.Companion.newRepository

class StatementsBatchTest : StringSpec() {

    init {
        "batch sizes" {
            testStatements(1)
            testStatements(5)
            testStatements(13)
            testStatements(49)
            testStatements(100)
            testStatements(1000)
            testStatements(1456)
        }
    }

    private fun testStatements(batchSize: Int) {
        val repository = newRepository()
        repository.withStatementsBatch(batchSize) { batch ->
            repeat(100) {
                batch.add(T1.iri("subject"), T2.iri("name"), "John Smith - $it".literal())
            }
        }
        val rows = repository.sparqlSelect(
            "SELECT ?o WHERE { ?s ?p ?o } LIMIT 100",
            "s" to T1.iri("subject"),
            "p" to T2.iri("name")
        )
        rows.size shouldBe 100
        rows.map { it.str("o") } shouldContainExactly (0 until 100).map {
            "John Smith - $it"
        }
    }
}