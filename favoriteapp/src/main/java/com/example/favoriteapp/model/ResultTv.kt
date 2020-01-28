package com.example.favoriteapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultTv(
    var id: Int,
    var overview: String ?= null,
    var backdropPath: String ?= null,
    var posterPath: String ?= null,
    var firstAirDate: String ?= null,
    var name: String ?= null,
    var voteAverage: Double,
    var voteCount: Int
): Parcelable