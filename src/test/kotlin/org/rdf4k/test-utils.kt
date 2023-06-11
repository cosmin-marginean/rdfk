package org.rdf4k

import org.eclipse.rdf4j.model.Namespace
import org.eclipse.rdf4j.model.vocabulary.XSD

val T1 = "http://test1.com/".namespace("t1")
val T2 = "http://test2.com/".namespace("t2")

val TEST_NAMESPACES = listOf<Namespace>(
        T1,
        T2,
        XSD.NS
)
