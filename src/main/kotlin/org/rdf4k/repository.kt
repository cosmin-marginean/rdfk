package org.rdf4k

import org.eclipse.rdf4j.model.Model
import org.eclipse.rdf4j.model.Statement
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.config.RepositoryConfig
import org.eclipse.rdf4j.repository.manager.RepositoryManager
import org.slf4j.LoggerFactory

private val log = LoggerFactory.getLogger("org.rdf4k")

fun RepositoryManager.createIfNotPresent(repositoryId: String, config: Model): Boolean {
    return if (hasRepositoryConfig(repositoryId)) {
        log.info("RDF Repository $repositoryId exists, skipping.")
        false
    } else {
        log.info("Creating RDF repository $repositoryId at $location")
        val repositoryConfig = RepositoryConfig.create(config, null)
        addRepositoryConfig(repositoryConfig)
        true
    }
}

fun Repository.withStatementsBatch(batchSize: Int, use: (StatementsBatch) -> Unit) {
    connection.use { connection ->
        connection.useBatch(batchSize) { batch ->
            use(batch)
        }
    }
}

fun Repository.add(statements: Collection<Statement>) {
    connection.use {
        it.add(statements)
    }
}

internal fun Model.replaceRepositoryId(newId: String): Model {
    val repoNameStatement = find { it.predicate == PREDICATE_REPO_NAME }!!
    removeIf { it.predicate == PREDICATE_REPO_NAME }
    add(statement(repoNameStatement.subject, repoNameStatement.predicate, newId.literal()))
    return this
}

private val PREDICATE_REPO_NAME = "http://www.openrdf.org/config/repository#repositoryID".iri()
