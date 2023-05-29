package org.rdf4k

import org.eclipse.rdf4j.repository.Repository
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager
import org.eclipse.rdf4j.repository.manager.RepositoryManager
import org.rdf4k.repository.createIfNotPresent
import org.rdf4k.repository.replaceRepositoryId
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import java.util.*

abstract class RdfContainerTest {

    lateinit var repository: Repository
    lateinit var repositoryManager: RepositoryManager

    @AfterSuite
    fun afterAll() {
        ContainerState.terminate()
    }

    @BeforeMethod
    fun beforeMethod() {
        repositoryManager = RemoteRepositoryManager(ContainerState.repositoryUrl)
        repositoryManager.init()
        val repositoryId = "rdf4k-${UUID.randomUUID()}"
        createRepository(repositoryId)
        repository = repositoryManager.getRepository(repositoryId)
    }

    fun createRepository(repositoryId: String): Boolean {
        val model = resourceAsRdfModel("graphdb-repository.ttl")
                .replaceRepositoryId(repositoryId)
        return repositoryManager.createIfNotPresent(repositoryId, model)
    }

    fun getRepository(repositoryId: String): Repository? {
        return repositoryManager.getRepository(repositoryId)
    }
}

object ContainerState {
    var repositoryUrl: String? = null
    var container: GenericContainer<*>? = null

    private val log = LoggerFactory.getLogger(ContainerState::class.java)

    init {
        if (repositoryUrl == null) {
            container = GenericContainer("ontotext/graphdb:10.1.3")
                    .withExposedPorts(7200)
            container!!.start()
            repositoryUrl = "http://localhost:${container!!.firstMappedPort}"
            log.info("RDF Repository URL is $repositoryUrl")
        }
    }

    fun terminate() {
        container!!.stop()
    }
}
