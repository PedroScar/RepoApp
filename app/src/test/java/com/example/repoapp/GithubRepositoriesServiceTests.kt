package com.example.repoapp

import com.example.repoapp.model.api.GithubRepositoriesAPI
import com.example.repoapp.model.api.GithubRepositoriesService
import com.example.repoapp.model.data.raw.GitRepoRaw
import com.example.utils.BaseUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GithubRepositoriesServiceTests : BaseUnitTest() {

    private lateinit var service: GithubRepositoriesService
    private val api: GithubRepositoriesAPI = mock()
    private val repositories: GitRepoRaw = mock()

    @Test
    fun fetchRepositoriesFromAPI() = runTest {
        service = GithubRepositoriesService(api)

        service.fetchRepositoriesList(1).first()

        verify(api, times(1)).fetchRepositoriesList(page = 1)
    }

    @Test
    fun convertValuesToFlowResultAndEmitsThem() = runTest {
        mockSuccessfulCase()

        Assert.assertEquals(Result.success(repositories), service.fetchRepositoriesList(1).first())
    }

    @Test
    fun emitsErrorResultWhenNetworkFails() = runTest {
        mockErrorCase()

        Assert.assertEquals("Something went wrong", service.fetchRepositoriesList(1).first().exceptionOrNull()?.message)
    }

    private suspend fun mockErrorCase() {
        whenever(api.fetchRepositoriesList(page = 1)).thenThrow(RuntimeException("We got an error"))

        service = GithubRepositoriesService(api)
    }

    private suspend fun mockSuccessfulCase() {
        whenever(api.fetchRepositoriesList(page = 1)).thenReturn(repositories)

        service = GithubRepositoriesService(api)
    }
}