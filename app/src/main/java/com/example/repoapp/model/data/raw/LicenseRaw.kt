package com.example.repoapp.model.data.raw

import com.google.gson.annotations.SerializedName

data class LicenseRaw(
    @SerializedName("key") var key: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("spdx_id") var spdxId: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("node_id") var nodeId: String? = null
)