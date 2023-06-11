package org.rdf4k.rio

import org.eclipse.rdf4j.rio.RDFFormat
import org.eclipse.rdf4j.rio.RDFWriterRegistry

fun fileRdfFormat(fileName: String): RDFFormat? {
    return RDFFormat.matchFileName(fileName, RDFWriterRegistry.getInstance().keys).orElse(null)
}
