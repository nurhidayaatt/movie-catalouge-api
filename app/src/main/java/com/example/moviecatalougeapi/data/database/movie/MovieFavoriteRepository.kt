package com.example.moviecatalougeapi.data.database.movie

import androidx.lifecycle.LiveData
import com.example.moviecatalougeapi.data.model.movie.ResultMovie


class MovieFavoriteRepository(private val moviefavoriteDao : MovieFavoriteDao) {

    val allMovieFavorites : LiveData<List<ResultMovie>> = moviefavoriteDao.loadMovieFavorites()

    fun movieFaviriteId(id: Int) = moviefavoriteDao.loadMovieId(id)

    suspend fun searchMovieFavorite(search: String) = moviefavoriteDao.searchMovie(search)

    suspend fun insert(movie: ResultMovie) {
        moviefavoriteDao.insertMovieFavorites(movie)
    }

    suspend fun delete(movie: ResultMovie) {
        moviefavoriteDao.deleteMovieFavorites(movie)
    }

}