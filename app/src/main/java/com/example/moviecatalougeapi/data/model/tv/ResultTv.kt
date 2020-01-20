package com.example.moviecatalougeapi.data.model.tv

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tv_favorite")
data class ResultTv(
    @PrimaryKey
    var id: Int,
    var overview: String ?= null,
    @SerializedName("backdrop_path")
    var backdropPath: String ?= null,
    @SerializedName("poster_path")
    var posterPath: String ?= null,
    @SerializedName("first_air_date")
    var firstAirDate: String ?= null,
    var name: String ?= null,
    @SerializedName("vote_average")
    var voteAverage: Double,
    @SerializedName("vote_count")
    var voteCount: Int
): Parcelable