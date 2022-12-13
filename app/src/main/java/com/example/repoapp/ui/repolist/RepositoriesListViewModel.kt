package com.example.repoapp.ui.repolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositoriesListViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    val newPage = MutableLiveData<List<GitRepositoriesVO>>()
    val loader = MutableLiveData<Boolean>()
    val errorDialog = MutableLiveData<Boolean>()
    val allRepositoriesList = ArrayList<GitRepositoriesVO>()
    var actualPageNumber = 1

    init {
        this.fetchRepoPage()
    }

    fun fetchRepoPage() {
        viewModelScope.launch(Dispatchers.IO) {
            loader.postValue(true)
            repository.fetchRepositoriesList(page = actualPageNumber).collect { result ->
                when {
                    result.isFailure -> errorDialog.postValue(true)
                    result.isSuccess -> {
                        if (loader.value == true)
                            with(result.getOrNull()!!) {
                                allRepositoriesList.addAll(this)
                                newPage.postValue(this)
                                actualPageNumber += 1
                            }
                    }
                }

                loader.postValue(false)
            }
        }
    }
}