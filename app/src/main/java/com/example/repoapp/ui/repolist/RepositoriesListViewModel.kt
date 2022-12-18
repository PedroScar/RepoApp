package com.example.repoapp.ui.repolist

import androidx.lifecycle.viewModelScope
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.repository.GithubRepository
import com.example.repoapp.ui.BaseViewModel
import com.example.repoapp.ui.RepositoriesUiState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoriesListViewModel @Inject constructor(
    private val repository: GithubRepository
) : BaseViewModel<RepositoriesUiState>() {

    val allRepositoriesList = ArrayList<GitRepositoriesVO>()
    var actualPageNumber = 1

    init {
        this.fetchRepoPage()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchRepoPage() {
        repositoriesUiState.value = RepositoriesUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRepositoriesList(page = actualPageNumber).collect { result ->
                when {
                    result.isFailure -> repositoriesUiState.value = RepositoriesUiState.Error(message = result.exceptionOrNull()!!.message!!)
                    result.isSuccess -> {
                        if (repositoriesUiState.value == RepositoriesUiState.Loading)
                            with(result.getOrNull()!!) {
                                allRepositoriesList.addAll(this)
                                actualPageNumber += 1
                                GlobalScope.launch(Dispatchers.Main) {
                                    repositoriesUiState.value = RepositoriesUiState.Success(result.getOrNull()!!)
                                }
                            }
                    }
                }
            }
        }
    }
}