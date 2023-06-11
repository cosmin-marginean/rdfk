# RDF4K

![GitHub release (latest by date)](https://img.shields.io/github/v/release/cosmin-marginean/rdf4k)
![Coveralls](https://img.shields.io/coverallsCoverage/github/cosmin-marginean/rdf4k)
![cosmin-marginean](https://circleci.com/gh/cosmin-marginean/rdf4k.svg?style=shield)

A Kotlin library for working with RDF. It uses [RDF4J](https://rdf4j.org/) internally
and the design is largely based on extension functions, providing a more fluent and functional approach.


[Documentation](https://cosmin-marginean.github.io/rdf4k/dokka/rdf4k/)

## Usage
```groovy
dependencies {
    implementation "io.resoluteworks:rdf4k:0.9.6"
}
```

### Reading RDF
```kotlin
// Read an RDF file
File("input.ttl").parseRdfIndexed(RDFFormat.TURTLE) { index, statement ->
    println(statement)
}

// Read input stream with custom RDF handler
val myRdfHandler = object : AbstractRDFHandler() {
    override fun handleStatement(st: Statement) {
        println(st)
    }
}
inputStream.parseRdf(RDFFormat.TURTLE, myRdfHandler)
```

### Writing RDF
```kotlin
val RES = "http://test.com/res/".namespace("res")
val DEFS = "http://test.com/defs/".namespace("defs")
File("input.ttl").useRdfWriter(RDFFormat.TURTLE, listOf(RES, DEFS)) { rdfWriter ->
    val statements = mutableListOf<Statement>()
    statements.add(RES.iri("one"), DEFS.iri("name"), "John Smith".literal())
    statements.add(RES.iri("two"), DEFS.iri("name"), "Angela Smith".literal())
    statements.add(RES.iri("two"), DEFS.iri("age"), 23.literal())
    rdfWriter.write(statements)
}
```

### Write and query a repository
```kotlin
        val MY_NAMESPACE = "http://test.com/".namespace("res")

// Write statements to an RDF repository
repository.add(resourceAsRdfModel("input.ttl"))

// Write statements to an RDF repository in batches
repository.withStatementsBatch(10_000) { batch ->
    batch.add(resourceAsRdfModel("input.ttl"))
}

// Querying
repository.sparqlSelect("SELECT ?s ?p ?o WHERE { ?s ?p ?o . }", "s" to MY_NAMESPACE.iri("one"))
        .forEach { row ->
            println(row.iri("p"))
            println(row.str("o"))
        }

// Querying using a .sparql from classpath
repository.sparqlSelectClasspath("queries/tuple-query.sparql",
        "s" to MY_NAMESPACE.iri("one")
)
repository.sparqlGraphClasspath("queries/graph-query.sparql",
        "s" to MY_NAMESPACE.iri("one")
)
```
