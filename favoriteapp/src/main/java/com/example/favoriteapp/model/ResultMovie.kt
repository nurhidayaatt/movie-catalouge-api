package com.example.favoriteapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultMovie(
    var id: Int,
    var overview: String ?= null,
    var backdropPath: String ?= null,
    var posterPath: String ?= null,
    var releaseDate: String ?= null,
    var title: String ?= null,
    var voteAverage: Double,
    var voteCount: Int
): Parcelable