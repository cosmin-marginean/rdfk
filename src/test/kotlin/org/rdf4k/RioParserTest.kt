package org.rdf4k

import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.helpers.StatementCollector
import org.testng.annotations.Test
import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RioParserTest {

    @Test
    fun `parse inputstream`() {
        val rdfHandler = StatementCollector()
        resourceAsInput("test-input.ttl").parseRdf(RDFFormat.TURTLE, rdfHandler)
        assertStatementsMatch(rdfHandler.statements)
        assertNamespacesMatch(rdfHandler.namespaces.map { it.value.namespace(it.key) })
    }

    @Test
    fun `parse inputstream indexed`() {
        val indices = mutableListOf<Long>()
        val count = resourceAsInput("test-input.ttl").parseRdfIndexed(RDFFormat.TURTLE) { index, statement ->
            indices.add(index)
        }
        assertEquals(count, 18)
        val expected: List<Long> = (0L..17L).toList()
        assertContentEquals(indices, expected)
    }

    @Test
    fun `parse file indexed`() {
        val indices = mutableListOf<Long>()
        val count = File("src/test/resources/test-input.ttl").parseRdfIndexed(RDFFormat.TURTLE) { index, statement ->
            indices.add(index)
        }
        assertEquals(count, 18)
        val expected: List<Long> = (0L..17L).toList()
        assertContentEquals(indices, expected)
    }

    @Test
    fun `parse file`() {
        val rdfHandler = StatementCollector()
        File("src/test/resources/test-input.ttl").parseRdf(RDFFormat.TURTLE, rdfHandler)
        assertStatementsMatch(rdfHandler.statements)
        assertNamespacesMatch(rdfHandler.namespaces.map { it.value.namespace(it.key) })
    }

    @Test
    fun `inputstream to rdf statements`() {
        assertStatementsMatch(resourceAsInput("test-input.ttl").toRdfStatements(RDFFormat.TURTLE))
    }

    @Test
    fun `file to rdf statements`() {
        assertStatementsMatch(File("src/test/resources/test-input.ttl").toRdfStatements())
        assertStatementsMatch(File("src/test/resources/test-input.ttl").toRdfStatements(RDFFormat.TURTLE))
    }

    @Test
    fun `inputstream to model`() {
        val model = resourceAsInput("test-input.ttl").toRdfModel(RDFFormat.TURTLE)
        assertStatementsMatch(model)
        assertNamespacesMatch(model.namespaces)
    }

    @Test
    fun `file to model`() {
        val model = File("src/test/resources/test-input.ttl").toRdfModel(RDFFormat.TURTLE)
        assertStatementsMatch(model)
        assertNamespacesMatch(model.namespaces)
        val model2 = File("src/test/resources/test-input.ttl").toRdfModel()
        assertStatementsMatch(model2)
        assertNamespacesMatch(model2.namespaces)
    }

    @Test
    fun `resource as model`() {
        val model = resourceToRdfModel("test-input.ttl")
        assertStatementsMatch(model)
        assertNamespacesMatch(model.namespaces)
    }

    private fun assertStatementsMatch(statements: Collection<Statement>) {
        assertEquals(statements.size, 18)
        assertTrue(
            statements.contains(
                statement(
                    "http://bods.openownership.org/resource/openownership-register-9473160899263237344".iri(),
                    "http://www.w3.org/1999/02/22-rdf-syntax-ns#type".iri(),
                    "http://bods.openownership.org/vocabulary/RegisteredEntity".iri()
                )
            )
        )
        assertTrue(
            statements.contains(
                statement(
                    "http://bods.openownership.org/resource/openownership-register-5450813549318202701".iri(),
                    "http://bods.openownership.org/vocabulary/statesInterest".iri(),
                    "http://bods.openownership.org/resource/openownership-register-5450813549318202701_3".iri()
                )
            )
        )
    }

    private fun assertNamespacesMatch(namespaces: Collection<Namespace>) {
        assertContentEquals(
            namespaces,
            listOf(
                "http://www.w3.org/1999/02/22-rdf-syntax-ns#".namespace("rdf"),
                "http://www.w3.org/2000/01/rdf-schema#".namespace("rdfs"),
                "http://www.w3.org/2002/07/owl#".namespace("owl"),
                "http://www.w3.org/2001/XMLSchema#".namespace("xsd"),
                "http://xmlns.com/foaf/0.1/".namespace("foaf"),
                "http://bods.openownership.org/vocabulary/".namespace("bods"),
                "http://bods.openownership.org/resource/".namespace("bodsr"),
            )
        )
    }
}
