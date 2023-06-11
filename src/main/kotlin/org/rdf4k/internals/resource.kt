package org.rdf4k.internals

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

internal fun resourceAsString(classpathLocation: String): String {
    return resourceAsInput(classpathLocation)
            .use { BufferedReader(InputStreamReader(it)).readText() }
}

internal fun resourceAsInput(classpathLocation: String): InputStream {
    val classLoader = Thread.currentThread().contextClassLoader
    return classLoader.getResourceAsStream(classpathLocation)
}
