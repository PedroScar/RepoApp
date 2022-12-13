package com.example.repoapp.model.data.vo

data class GitRepositoriesVO(
    val repositoryName: String,
    val stargazersCount: Int,
    val forksCount: Int,
    val authorImageUrl: String,
    val authorName: String
)