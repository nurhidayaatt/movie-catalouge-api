package com.example.moviecatalougeapi.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.moviecatalougeapi.BuildConfig
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase

class FavoriteProvider: ContentProvider() {

    companion object{
        private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
        private const val TABLE_NAME = "movies"
        private const val MOVIE = 1
        private const val MOVIE_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, MOVIE)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", MOVIE_ID)
        }

    }

    private lateinit var dbMovie : MovieFavoriteDatabase

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        if (context == null){
            return null
        }
        val cursor: Cursor?
        val favoriteMovies = dbMovie.movieFavoriteDao()
        val code = uriMatcher.match(p0)
        if (code in MOVIE..MOVIE_ID){
            cursor = when(uriMatcher.match(p0)){
                MOVIE -> favoriteMovies.getAllFavoriteCursor()
                MOVIE_ID -> favoriteMovies.getFavoriteMovieCursor(ContentUris.parseId(p0).toInt())
                else -> {
                    null
                }
            }
            //cursor?.setNotificationUri(context?.contentResolver, p0)
            return cursor
        }else throw IllegalArgumentException("Unknown Uri")
    }

    override fun onCreate(): Boolean {
        dbMovie = MovieFavoriteDatabase.getInstance(context!!)
        return true
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return -1
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return -1
    }

    override fun getType(p0: Uri): String? {
        return when(uriMatcher.match(p0)){
            MOVIE -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME"
            MOVIE_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown Uri $p0")
        }
    }
}