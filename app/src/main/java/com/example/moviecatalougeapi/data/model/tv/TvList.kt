package com.example.moviecatalougeapi.data.model.tv


import com.google.gson.annotations.SerializedName

data class TvList(
    val page: Int,
    val results: List<ResultTv>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)