package com.example.repoapp.model.repository

import com.example.repoapp.model.api.GithubRepositoriesService
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import com.example.repoapp.model.mappers.GithubRepositoriesMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubRepository @Inject constructor(
    private val service: GithubRepositoriesService,
    private val mapper: GithubRepositoriesMapper
) {
    suspend fun fetchRepositoriesList(page: Int): Flow<Result<List<GitRepositoriesVO>>> =
        service.fetchRepositoriesList(page = page).map {
            if (it.isSuccess)
                Result.success(mapper(it.getOrNull()!!))
            else
                Result.failure(it.exceptionOrNull()!!)
        }
}