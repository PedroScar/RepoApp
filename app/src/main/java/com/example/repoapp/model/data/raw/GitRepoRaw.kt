package com.example.repoapp.model.data.raw

import com.google.gson.annotations.SerializedName

data class GitRepoRaw(
    @SerializedName("total_count") var totalCount: Int? = null,
    @SerializedName("incomplete_results") var incompleteResults: Boolean? = null,
    @SerializedName("items") var items: List<ItemsRaw>? = arrayListOf()
)