package org.rdfk

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriterRegistry
import java.io.InputStream

fun fileRdfFormat(fileName: String): RDFFormat? {
    return RDFFormat.matchFileName(fileName, RDFWriterRegistry.getInstance().keys).orElse(null)
}

internal fun resourceAsInput(classpathLocation: String): InputStream {
    val classLoader = Thread.currentThread().contextClassLoader
    return classLoader.getResourceAsStream(classpathLocation)
}
