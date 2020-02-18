package com.example.moviecatalougeapi.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.tv.TvFavoriteDatabase

class FavoriteProvider: ContentProvider() {

    companion object{
        private const val AUTHORITY = "com.example.moviecatalougeapi"
        private const val TABLE_MOVIE = "movie_favorite"
        private const val TABLE_TV = "tv_favorite"
        private const val MOVIE = 1
        private const val MOVIE_ID = 2
        private const val TV = 3
        private const val TV_ID = 4

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE)
            uriMatcher.addURI(AUTHORITY, "$TABLE_MOVIE/#", MOVIE_ID)
            uriMatcher.addURI(AUTHORITY, TABLE_TV, TV)
            uriMatcher.addURI(AUTHORITY, "$TABLE_TV/#", TV_ID)
        }

    }

    private lateinit var dbMovie : MovieFavoriteDatabase
    private lateinit var dbTv : TvFavoriteDatabase

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return null
    }

    override fun query(uri: Uri, strings: Array<out String>?, s: String?, strings1: Array<out String>?, s1: String?): Cursor? {
        if (context == null){
            return null
        }
        val cursor: Cursor?
        val favoriteMovies = dbMovie.movieFavoriteDao()
        val favoriteTvs = dbTv.tvFavoriteDao()
        when (uriMatcher.match(uri)) {
            in MOVIE..MOVIE_ID -> {
                cursor = when(uriMatcher.match(uri)){
                    MOVIE -> favoriteMovies.getAllFavoriteCursor()
                    MOVIE_ID -> favoriteMovies.getFavoriteMovieCursor(ContentUris.parseId(uri).toInt())
                    else -> {
                        null
                    }
                }
                cursor?.setNotificationUri(context?.contentResolver, uri)
                return cursor
            }
            in TV..TV_ID -> {
                cursor = when(uriMatcher.match(uri)){
                    TV -> favoriteTvs.getAllFavoriteCursor()
                    TV_ID -> favoriteTvs.getFavoriteMovieCursor(ContentUris.parseId(uri).toInt())
                    else -> {
                        null
                    }
                }
                cursor?.setNotificationUri(context?.contentResolver, uri)
                return cursor
            }
            else -> throw IllegalArgumentException("Unknown Uri")
        }
    }

    override fun onCreate(): Boolean {
        dbMovie = MovieFavoriteDatabase.getInstance(context!!)
        dbTv = TvFavoriteDatabase.getInstance(context!!)
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
            MOVIE -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_MOVIE"
            MOVIE_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_MOVIE"
            TV -> "vnd.android.cursor.dir/$AUTHORITY.$TABLE_TV"
            TV_ID -> "vnd.android.cursor.item/$AUTHORITY.$TABLE_TV"
            else -> throw IllegalArgumentException("Unknown Uri $p0")
        }
    }
}