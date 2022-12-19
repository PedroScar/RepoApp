package com.example.repoapp.model.api

import com.example.repoapp.model.data.raw.GitRepoRaw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject

class GithubRepositoriesService @Inject constructor(
    private var api: GithubRepositoriesAPI
) {
    suspend fun fetchRepositoriesList(page: Int): Flow<Result<GitRepoRaw>> {
        return flow {
            emit(Result.success(api.fetchRepositoriesList(page = page)))
        }.catch { exception ->
            emit(Result.failure(RuntimeException(exception.message)))
        }
    }
}