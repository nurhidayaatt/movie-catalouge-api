package com.example.moviecatalougeapi.ui.favorite.moviefavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDao
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteRepository
import com.example.moviecatalougeapi.data.model.movie.ResultMovie

class MovieFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val movieFavoriteDao: MovieFavoriteDao = MovieFavoriteDatabase.getInstance(application).movieFavoriteDao()
    private val repository: MovieFavoriteRepository =
        MovieFavoriteRepository(
            movieFavoriteDao
        )

    private var _movies : LiveData<List<ResultMovie>> = repository.allMovieFavorites
    val movies : LiveData<List<ResultMovie>> get() = _movies

}
