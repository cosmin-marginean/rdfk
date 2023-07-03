package org.rdf4k

import io.kotest.core.annotation.AutoScan
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeEachListener
import io.kotest.core.listeners.BeforeProjectListener
import io.kotest.core.test.TestCase
import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager
import org.eclipse.rdf4j.repository.manager.RepositoryManager
import org.rdf4k.rio.resourceAsRdfModel
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import java.util.*

@AutoScan
class TestContainer : BeforeProjectListener, BeforeEachListener, AfterProjectListener {

    companion object {
        private val log = LoggerFactory.getLogger(TestContainer::class.java)

        lateinit var repository: Repository
        lateinit var repositoryManager: RepositoryManager
        lateinit var container: GenericContainer<*>
        lateinit var repositoryUrl: String

        fun createRepository(repositoryId: String): Boolean {
            val model = resourceAsRdfModel("graphdb-repository.ttl")
                .replaceRepositoryId(repositoryId)
            return repositoryManager.createIfNotPresent(repositoryId, model)
        }

        fun getRepository(repositoryId: String): Repository? {
            return repositoryManager.getRepository(repositoryId)
        }

        fun newRepository(): Repository {
            val repositoryId = "rdf4k-${UUID.randomUUID()}"
            createRepository(repositoryId)
            return repositoryManager.getRepository(repositoryId)
        }
    }

    override suspend fun beforeProject() {
        container = GenericContainer("ontotext/graphdb:10.1.3")
            .withExposedPorts(7200)
        container.start()
        repositoryUrl = "http://localhost:${container.firstMappedPort}"
        log.info("RDF Repository URL is $repositoryUrl")
    }

    override suspend fun beforeEach(testCase: TestCase) {
        repositoryManager = RemoteRepositoryManager(repositoryUrl)
        repositoryManager.init()
        repository = newRepository()
    }

    override suspend fun afterProject() {
        container.stop()
    }
}
