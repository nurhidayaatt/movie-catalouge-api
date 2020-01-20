package com.example.moviecatalougeapi.data.model.movie

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie_favorite")
data class ResultMovie(
    @PrimaryKey
    var id: Int,
    var overview: String ?= null,
    @SerializedName("backdrop_path")
    var backdropPath: String ?= null,
    @SerializedName("poster_path")
    var posterPath: String ?= null,
    @SerializedName("release_date")
    var releaseDate: String ?= null,
    var title: String ?= null,
    @SerializedName("vote_average")
    var voteAverage: Double,
    @SerializedName("vote_count")
    var voteCount: Int
): Parcelable