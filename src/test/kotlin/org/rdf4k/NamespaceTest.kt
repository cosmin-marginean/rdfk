package org.rdf4k

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.eclipse.rdf4j.model.impl.SimpleNamespace

class NamespaceTest : StringSpec({

    "String.namespace" {
        "https://mynamespace.com/".namespace("test") shouldBe SimpleNamespace("test", "https://mynamespace.com/")
    }

    "Namespace.iri" {
        "https://mynamespace.com/".namespace("test")
            .iri("1") shouldBe valueFactory.createIRI("https://mynamespace.com/", "1")
    }
})