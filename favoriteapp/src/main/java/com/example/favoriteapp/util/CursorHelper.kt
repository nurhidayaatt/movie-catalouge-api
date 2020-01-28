package com.example.favoriteapp.util

import android.database.Cursor
import com.example.favoriteapp.model.ResultMovie
import com.example.favoriteapp.model.ResultTv

object CursorHelper {
    fun convertToMovieFavorite(cursor: Cursor): List<ResultMovie> {
        val favorites = mutableListOf<ResultMovie>()
        while(cursor.moveToNext()) {
            val movie = ResultMovie(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                posterPath = cursor.getString(cursor.getColumnIndexOrThrow("posterPath")),
                backdropPath = cursor.getString(cursor.getColumnIndexOrThrow("backdropPath")),
                releaseDate = cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
                overview = cursor.getString(cursor.getColumnIndexOrThrow("overview")),
                voteAverage = cursor.getDouble(cursor.getColumnIndexOrThrow("voteAverage")),
                voteCount = cursor.getInt(cursor.getColumnIndexOrThrow("voteCount"))
            )
            favorites.add(movie)
        }
        return favorites
    }

    fun convertToTvFavorite(cursor: Cursor): List<ResultTv> {
        val favorites = mutableListOf<ResultTv>()
        while (cursor.moveToNext()) {
            val tv = ResultTv(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                posterPath = cursor.getString(cursor.getColumnIndexOrThrow("posterPath")),
                backdropPath = cursor.getString(cursor.getColumnIndexOrThrow("backdropPath")),
                firstAirDate = cursor.getString(cursor.getColumnIndexOrThrow("firstAirDate")),
                overview = cursor.getString(cursor.getColumnIndexOrThrow("overview")),
                voteAverage = cursor.getDouble(cursor.getColumnIndexOrThrow("voteAverage")),
                voteCount = cursor.getInt(cursor.getColumnIndexOrThrow("voteCount"))
            )
            favorites.add(tv)
        }
        return favorites
    }
}