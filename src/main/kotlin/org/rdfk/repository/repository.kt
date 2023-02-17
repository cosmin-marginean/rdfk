package org.rdfk.repository

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.config.RepositoryConfig
import org.eclipse.rdf4j.repository.manager.RepositoryManager
import org.rdfk.iri
import org.rdfk.literal
import org.rdfk.statement
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("org.rdfk.repository")

fun RepositoryManager.createIfNotPresent(repositoryId: String, bootstrapModel: Model): Boolean {
    return if (hasRepositoryConfig(repositoryId)) {
        log.info("Repository $repositoryId exists, skipping.")
        false
    } else {
        log.info("Creating RDF repository $repositoryId at $location")
        val repositoryConfig = RepositoryConfig.create(bootstrapModel, null)
        addRepositoryConfig(repositoryConfig)
        true
    }
}

fun Repository.useConnectionBatch(batchSize: Int, use: (ConnectionStatementBatch) -> Unit) {
    connection.use { connection ->
        connection.useBatch(batchSize) { batch ->
            use(batch)
        }
    }
}

internal fun Model.replaceRepositoryId(newId: String): Model {
    val repoNameStatement = find { it.predicate == PREDICATE_REPO_NAME }!!
    removeIf { it.predicate == PREDICATE_REPO_NAME }
    add(statement(repoNameStatement.subject, repoNameStatement.predicate, newId.literal()))
    return this
}

private val PREDICATE_REPO_NAME = "http://www.openrdf.org/config/repository#repositoryID".iri()
