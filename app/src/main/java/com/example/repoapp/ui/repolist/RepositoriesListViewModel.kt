package com.example.repoapp.ui.repolist

import androidx.lifecycle.viewModelScope
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.repository.GithubRepository
import com.example.repoapp.ui.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class RepositoriesListViewModel @Inject constructor(
    private val repository: GithubRepository
) : BaseViewModel<RepositoriesUiState>() {

    val allRepositoriesList = ArrayList<GitRepositoriesVO>()
    var actualPageNumber = 1

    init {
        this.fetchRepoPage()
    }

    fun fetchRepoPage() {
        viewModelScope.launch(ioDispatcher) {
            repository.fetchRepositoriesList(page = actualPageNumber)
                .onStart { setState(RepositoriesUiState.Loading) }
                .collect { result ->
                    when {
                        result.isFailure -> setState(RepositoriesUiState.Error(message = result.exceptionOrNull()!!.message!!))
                        result.isSuccess -> {
                            with(result.getOrNull()!!) {
                                allRepositoriesList.addAll(this)
                                actualPageNumber += 1
                                setState(RepositoriesUiState.Success(this))
                            }
                        }
                    }
                }
        }
    }

    private fun setState(state: RepositoriesUiState) {
        viewModelScope.launch(uiDispatcher) {
            repositoriesUiState.value = state
        }
    }
}

