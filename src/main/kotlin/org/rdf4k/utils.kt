package org.rdf4k

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriterRegistry
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun fileRdfFormat(fileName: String): RDFFormat? {
    return RDFFormat.matchFileName(fileName, RDFWriterRegistry.getInstance().keys).orElse(null)
}

fun resourceAsString(classpathLocation: String): String {
    return resourceAsInput(classpathLocation)
        .use { BufferedReader(InputStreamReader(it)).readText() }
}

internal fun resourceAsInput(classpathLocation: String): InputStream {
    val classLoader = Thread.currentThread().contextClassLoader
    return classLoader.getResourceAsStream(classpathLocation)
}
