package com.example.repoapp.ui

import com.example.repoapp.model.data.vo.GitRepositoriesVO

sealed class RepositoriesUiState {
    object Loading : RepositoriesUiState()
    data class Success(val response: List<GitRepositoriesVO>) : RepositoriesUiState()
    data class Error(val message: String) : RepositoriesUiState()
}