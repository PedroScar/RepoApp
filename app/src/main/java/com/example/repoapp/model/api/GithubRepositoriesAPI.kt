package com.example.repoapp.model.api

import com.example.repoapp.model.data.raw.GitRepoRaw
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubRepositoriesAPI {
    @GET("search/repositories")
    suspend fun fetchRepositoriesList(
        @Query("q") q: String = "language:kotlin",
        @Query("sort") sort: String = "stars",
        @Query("page") page: Int,
    ) : GitRepoRaw
}