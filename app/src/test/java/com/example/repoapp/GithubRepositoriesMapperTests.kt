package com.example.repoapp

import com.example.repoapp.model.data.raw.GitRepoRaw
import com.example.repoapp.model.data.raw.ItemsRaw
import com.example.repoapp.model.data.raw.OwnerRaw
import com.example.repoapp.model.mappers.GithubRepositoriesMapper
import com.example.utils.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GithubRepositoriesMapperTests : BaseUnitTest() {

    private val gitRepoRaw = GitRepoRaw(items = arrayListOf(
        ItemsRaw(
            name = "Item Name",
            stargazersCount = 1000,
            forksCount = 800,
            owner = OwnerRaw(
                avatarUrl = "www.avatar.url",
                login = "owner name"
            )
        )
    ))

    private val mapper = GithubRepositoriesMapper()
    private val gitRepoVO = mapper(gitRepoRaw)
    private val gitRepoVOItem = gitRepoVO[0]

    @Test
    fun keepSameRepositoryName() {
        assertEquals(gitRepoRaw.items?.get(0)?.name, gitRepoVOItem.repositoryName)
    }

    @Test
    fun keepSameStargazersCount() {
        assertEquals(gitRepoRaw.items?.get(0)?.stargazersCount, gitRepoVOItem.stargazersCount)
    }

    @Test
    fun keepSameForksCount() {
        assertEquals(gitRepoRaw.items?.get(0)?.forksCount, gitRepoVOItem.forksCount)
    }
    @Test
    fun keepSameAuthorImageUrl() {
        assertEquals(gitRepoRaw.items?.get(0)?.owner?.avatarUrl, gitRepoVOItem.authorImageUrl)
    }
    @Test
    fun keepSameAuthorName() {
        assertEquals(gitRepoRaw.items?.get(0)?.owner?.login, gitRepoVOItem.authorName)
    }
}