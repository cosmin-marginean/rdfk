# RDF4K

![GitHub release (latest by date)](https://img.shields.io/github/v/release/cosmin-marginean/rdf4k)
![Coveralls](https://img.shields.io/coverallsCoverage/github/cosmin-marginean/rdf4k)

A Kotlin library for working with RDF. It uses [RDF4J](https://rdf4j.org/) internally
and the design is largely based on extension functions, providing a more fluent and functional approach.

#### With native RDF4J
```kotlin
val RES = "http://test.com/res/"
val DEFS = "http://test.com/defs/"
val valueFactory = SimpleValueFactory.getInstance()
outputFile.outputStream().use { outputStream ->
    val rdfWriter = Rio.createWriter(RDFFormat.TURTLE, outputStream)
    rdfWriter.startRDF()
    rdfWriter.handleNamespace("res", RES)
    rdfWriter.handleNamespace("defs", DEFS)
    rdfWriter.handleStatement(
        valueFactory.createStatement(
            valueFactory.createIRI(RES, "one"),
            valueFactory.createIRI(DEFS, "name"),
            valueFactory.createLiteral("John Smith")
        )
    )
    rdfWriter.handleStatement(
        valueFactory.createStatement(
            valueFactory.createIRI(RES, "two"),
            valueFactory.createIRI(DEFS, "name"),
            valueFactory.createLiteral("Angela White")
        )
    )
    rdfWriter.endRDF()
}
```

#### With RDF4K
```kotlin
val RES = "http://test.com/res/".namespace("res")
val DEFS = "http://test.com/defs/".namespace("defs")
outputFile.useRdfWriter(RDFFormat.TURTLE, listOf(RES, DEFS)) { rdfWriter ->
    rdfWriter.write(RES.iri("one"), DEFS.iri("name"), "John Smith".literal())
    rdfWriter.write(RES.iri("two"), DEFS.iri("name"), "Angela Smith".literal())
}
```

## Usage
```groovy
dependencies {
    implementation "io.resoluteworks:rdf4k:0.9.4"
}
```

## Examples

For more details please check [examples](https://github.com/cosmin-marginean/rdf4k/blob/main/src/test/kotlin/org/rdf4k/ExamplesDetailed.kt)
and [docs](https://cosmin-marginean.github.io/rdf4k/dokka/rdf4k/)

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

### RDF Repository
```kotlin
// Write statements to an RDF repository connection in batches 
repository.useConnectionBatch(10_000) { batch ->
    batch.add(resourceToRdfModel("input.ttl"))
}

// Querying
repository.connection.use { connection ->
    connection.prepareTupleQuery("SELECT ?s ?p ?o WHERE { ?s ?p ?o . }")
        .bindings("s" to NAMESPACE_RES.iri("one"))
        .evaluate()
        .forEach { row ->
            println(row.iri("p"))
            println(row.str("o"))
        }
        
}

// Querying using a .sparql from classpath
repository.connection.use { connection ->
    connection.prepareTupleQueryClasspath("queries/query.sparql")
        .bindings("s" to NAMESPACE_RES.iri("one"))
        .evaluate()
}
```
