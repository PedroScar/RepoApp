package com.example.repoapp.model.mappers

import com.example.repoapp.model.data.raw.GitRepoRaw
import com.example.repoapp.model.data.vo.GitRepositoriesVO
import javax.inject.Inject

class GithubRepositoriesMapper @Inject constructor() : Function1<GitRepoRaw, List<GitRepositoriesVO>> {
    override fun invoke(p1: GitRepoRaw): List<GitRepositoriesVO> {
        return p1.items?.map { item ->
            GitRepositoriesVO(
                repositoryName = item.name ?: "Unknown",
                stargazersCount = item.stargazersCount ?: 0,
                forksCount = item.forksCount ?: 0,
                authorImageUrl = item.owner?.avatarUrl ?: "",
                authorName = item.owner?.login ?: ""
            )
        } ?: emptyList()
    }
}