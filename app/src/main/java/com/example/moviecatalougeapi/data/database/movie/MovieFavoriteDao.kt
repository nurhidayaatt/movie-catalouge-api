package com.example.moviecatalougeapi.data.database.movie

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviecatalougeapi.data.model.movie.ResultMovie

@Dao
interface MovieFavoriteDao {

    @Query("SELECT * FROM movie_favorite")
    fun loadMovieFavorites() : LiveData<List<ResultMovie>>

    @Query("SELECT id FROM movie_favorite WHERE id =:movieId")
    fun loadMovieId(movieId: Int) : Int

    @Query("SELECT * FROM movie_favorite WHERE title =:Title")
    suspend fun searchMovie(Title: String) : List<ResultMovie>

    @Query("SELECT backdropPath FROM movie_favorite")
    fun loadFavorite() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieFavorites(movie: ResultMovie)

    @Delete
    suspend fun deleteMovieFavorites(movie: ResultMovie)

    @Query("SELECT * FROM movie_favorite")
    fun getAllFavoriteCursor(): Cursor

    @Query("SELECT * FROM movie_favorite WHERE id = :movieId")
    fun getFavoriteMovieCursor(movieId: Int): Cursor

}