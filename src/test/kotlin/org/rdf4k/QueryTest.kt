package org.rdf4k

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.eclipse.rdf4j.model.Literal
import org.rdf4k.TestContainer.Companion.repository

class QueryTest : StringSpec({

    "bindings - pairs" {
        repository.connection.use { connection ->
            val bindings = connection.prepareTupleQuery("SELECT ?p ?o WHERE { ?s ?p ?o }").bindings("s" to T1.iri("one")).bindings
            bindings.size() shouldBe 1
            bindings.iri("s") shouldBe T1.iri("one")
        }
    }

    "bindings - map" {
        repository.connection.use { connection ->
            val bindings = connection.prepareTupleQuery("SELECT ?p ?o WHERE { ?s ?p ?o }").bindings(mapOf("s" to T1.iri("one"))).bindings
            bindings.size() shouldBe 1
            bindings.iri("s") shouldBe T1.iri("one")
        }
    }

    "sparqlSelect" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.sparqlSelect("SELECT ?p ?o WHERE { ?s ?p ?o }", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.iri("p") == T2.iri("name") }!!.str("o") shouldBe "John Smith"
        rows.find { it.iri("p") == T2.iri("age") }!!.int("o") shouldBe 23
    }

    "sparqlSelectClasspath" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.sparqlSelectClasspath("query-select.sparql", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.iri("p") == T2.iri("name") }!!.str("o") shouldBe "John Smith"
        rows.find { it.iri("p") == T2.iri("age") }!!.int("o") shouldBe 23
    }

    "sparqlGraph" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.sparqlGraph("CONSTRUCT { ?s ?p ?o } WHERE { ?s ?p ?o }", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.predicate == T2.iri("name") }!!.`object` shouldBe "John Smith".literal()
        (rows.find { it.predicate == T2.iri("age") }!!.`object` as Literal).intValue() shouldBe 23
    }

    "sparqlGraphClasspath" {
        repository.withStatementsBatch(10) { batch ->
            batch.add(T1.iri("one"), T2.iri("name"), "John Smith".literal())
            batch.add(T1.iri("one"), T2.iri("age"), "23".literal())
        }
        val rows = repository.sparqlGraphClasspath("query-graph.sparql", "s" to T1.iri("one"))
        rows.size shouldBe 3
        rows.find { it.predicate == T2.iri("name") }!!.`object` shouldBe "John Smith".literal()
        (rows.find { it.predicate == T2.iri("age") }!!.`object` as Literal).intValue() shouldBe 23
    }
})