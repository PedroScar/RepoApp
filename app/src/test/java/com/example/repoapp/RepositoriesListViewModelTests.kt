package com.example.repoapp

import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.repository.GithubRepository
import com.example.repoapp.ui.RepositoriesUiState
import com.example.repoapp.ui.repolist.RepositoriesListViewModel
import com.example.utils.BaseUnitTest
import com.example.utils.captureValues
import com.example.utils.getValueForTest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility.await
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.times
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.TimeUnit

class RepositoriesListViewModelTests : BaseUnitTest() {

    private var actualPageNumber = 1
    private var repository: GithubRepository = mock()
    private var repositories = listOf(
        GitRepositoriesVO(
            repositoryName = "Repo Name",
            stargazersCount = 1000,
            forksCount = 1000,
            authorImageUrl = "nice URL",
            authorName = "Nice name"
        )
    )

    private var expected = Result.success(repositories)
    private var exception = RuntimeException("Something went wrong")

    @Test
    fun getNewPageFromRepository(): Unit = runBlocking  {
        mockSuccessfulCase()

        verify(repository, times(1)).fetchRepositoriesList(1)
    }

    @Test
    fun emitsNewPageFromRepository()  {
        val viewModel = mockSuccessfulCase()

        await().atMost(5, TimeUnit.SECONDS).until { viewModel.actualPageNumber == 2 }

        Assert.assertEquals(RepositoriesUiState.Success(repositories), viewModel.uiState().getValueForTest())
    }

    @Test
    fun emitErrorWhenServiceFails() {
        val viewModel = mockErrorCase()


        await().atMost(5, TimeUnit.SECONDS).until { viewModel.uiState().value == RepositoriesUiState.Error("Something went wrong") }

      Assert.assertEquals(RepositoriesUiState.Error(exception.message!!), viewModel.uiState().getValueForTest())
    }

    @Test
    fun increasePageNumberAfterSuccessFetch() {
        val viewModel = mockSuccessfulCase()

        await().until { viewModel.actualPageNumber == 2 }

        Assert.assertEquals(actualPageNumber + 1, viewModel.actualPageNumber)
    }

    @Test
    fun addAllNewPageItensIntoAllRepoList()  {
        val viewModel = mockSuccessfulCase()

        val initialAllRepoValue = 0

        await().atMost(5, TimeUnit.SECONDS).until { viewModel.allRepositoriesList.size == initialAllRepoValue + repositories.size }

        Assert.assertEquals(viewModel.allRepositoriesList.size, initialAllRepoValue + repositories.size)
    }

    @Test
    fun startStopLoaderCycle() {
        val viewModel = mockSuccessfulCase()

        viewModel.uiState().captureValues {
            viewModel.fetchRepoPage()

            await().atMost(5, TimeUnit.SECONDS).until { values.last() == RepositoriesUiState.Success(repositories) }

            Assert.assertEquals(RepositoriesUiState.Loading, values[0])
            Assert.assertEquals(RepositoriesUiState.Success(repositories), values.last())
        }
    }

    private fun mockSuccessfulCase(): RepositoriesListViewModel {
        runBlocking {
            whenever(repository.fetchRepositoriesList(actualPageNumber)).thenReturn(
                flow {
                    emit(expected)
                }
            )
        }
        return RepositoriesListViewModel(repository)
    }

    private fun mockErrorCase(): RepositoriesListViewModel {
        runBlocking {
            whenever(repository.fetchRepositoriesList(actualPageNumber)).thenReturn(
                flow {
                    emit(Result.failure(exception))
                }
            )
        }

        return RepositoriesListViewModel(repository)
    }
}