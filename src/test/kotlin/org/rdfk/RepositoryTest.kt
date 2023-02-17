package org.rdfk

import org.rdfk.repository.createIfNotPresent
import org.testng.annotations.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RepositoryTest : RdfContainerTest() {

    @Test
    fun `create repository`() {
        val repositoryId = "test-${UUID.randomUUID()}"
        assertTrue(createRepository(repositoryId))
        assertFalse(createRepository(repositoryId))
    }
}
