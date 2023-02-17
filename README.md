# RDF4K

![Coveralls](https://img.shields.io/coverallsCoverage/github/cosmin-marginean/rdf4k)

A Kotlin library for working with RDF. It uses [RDF4J](https://rdf4j.org/) internally
and the design is largely based on extension functions, providing a more fluent and functional approach.

#### With native RDF4J
```kotlin
val T1 = "http://test1.com/"
val T2 = "http://test2.com/"
val valueFactory = SimpleValueFactory.getInstance()
outputFile.outputStream().use { outputStream ->
    val rdfWriter = Rio.createWriter(RDFFormat.TURTLE, outputStream)
    rdfWriter.startRDF()
    rdfWriter.handleNamespace("t1", T1)
    rdfWriter.handleNamespace("t2", T2)
    rdfWriter.handleStatement(
        valueFactory.createStatement(
            valueFactory.createIRI(T1, "one"),
            valueFactory.createIRI(T2, "name"),
            valueFactory.createLiteral("John Smith")
        )
    )
    rdfWriter.handleStatement(
        valueFactory.createStatement(
            valueFactory.createIRI(T1, "two"),
            valueFactory.createIRI(T2, "name"),
            valueFactory.createLiteral("Angela Smith")
        )
    )
    rdfWriter.endRDF()
}
```

#### With RDF4K
```kotlin
val N1 = "http://test1.com/".namespace("t1")
val N2 = "http://test2.com/".namespace("t2")
outputFile.useRdfWriter(RDFFormat.TURTLE, listOf(N1, N2)) { rdfWriter ->
    rdfWriter.write(N1.iri("one"), N2.iri("name"), "John Smith".literal())
    rdfWriter.write(N1.iri("two"), N2.iri("name"), "Angela Smith".literal())
}
```
