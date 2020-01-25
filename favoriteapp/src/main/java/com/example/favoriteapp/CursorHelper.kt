package com.example.favoriteapp

import android.database.Cursor

object CursorHelper {
    fun convertToListFavorite(cursor: Cursor): List<ResultMovie> {
        val favorites = mutableListOf<ResultMovie>()
        while(cursor.moveToNext()){
            val movie = ResultMovie(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                posterPath = cursor.getString(cursor.getColumnIndexOrThrow("posterPath")),
                backdropPath = cursor.getString(cursor.getColumnIndexOrThrow("backdropPath")),
                releaseDate =  cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
                overview =  cursor.getString(cursor.getColumnIndexOrThrow("overview")),
                voteAverage =  cursor.getDouble(cursor.getColumnIndexOrThrow("voteAverage")),
                voteCount = cursor.getInt(cursor.getColumnIndexOrThrow("voteCount"))
            ).apply {
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            }
            favorites.add(movie)
        }
        return favorites
    }
}