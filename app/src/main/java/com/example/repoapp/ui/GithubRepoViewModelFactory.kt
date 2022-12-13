package com.example.repoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.repoapp.model.repository.GithubRepository
import com.example.repoapp.ui.repolist.RepositoriesListViewModel
import javax.inject.Inject

class GithubRepoViewModelFactory @Inject constructor(
    private val repository: GithubRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RepositoriesListViewModel(repository) as T
    }

}