package com.example.repoapp

import com.example.repoapp.model.api.GithubRepositoriesService
import com.example.repoapp.model.data.raw.GitRepoRaw
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.mappers.GithubRepositoriesMapper
import com.example.repoapp.model.repository.GithubRepository
import com.example.utils.BaseUnitTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GithubRepositoryTests: BaseUnitTest() {

    private val service : GithubRepositoriesService = mock()
    private val mapper : GithubRepositoriesMapper = mock()
    private val repositories = mock<List<GitRepositoriesVO>>()
    private val playlistsRaw = mock<GitRepoRaw>()
    private val exception = RuntimeException("Something went wrong")

    @Test
    fun getRepositoriesFromService() = runTest {
        val repository = mockSuccessfulCase()

        repository.fetchRepositoriesList(1)

        verify(service, times(1)).fetchRepositoriesList(1)
    }

    @Test
    fun emitMappedRepositoriesFromService() = runTest {
        val repository = mockSuccessfulCase()

        Assert.assertEquals(repositories, repository.fetchRepositoriesList(1).first().getOrNull())
    }

    @Test
    fun propagateErrors() = runTest {
        val repository = mockFailureCase()

        Assert.assertEquals(exception, repository.fetchRepositoriesList(1).first().exceptionOrNull())
    }

    @Test
    fun delegateBusinessLogicToMapper() = runTest {
        val repository = mockSuccessfulCase()

        repository.fetchRepositoriesList(1).first()

        Mockito.verify(mapper, times(1)).invoke(playlistsRaw)
    }

    private suspend fun mockFailureCase(): GithubRepository {
        whenever(service.fetchRepositoriesList(1)).thenReturn(
            flow {
                emit(Result.failure(exception))
            }
        )

        return GithubRepository(service, mapper)
    }

    private suspend fun mockSuccessfulCase(): GithubRepository {
        whenever(service.fetchRepositoriesList(1)).thenReturn(
            flow {
                emit(Result.success(playlistsRaw))
            }
        )

        whenever(mapper.invoke(playlistsRaw)).thenReturn(repositories)

        return GithubRepository(service, mapper)
    }
}