package com.example.moviecatalougeapi.ui.favorite.moviefavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDao
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteDatabase
import com.example.moviecatalougeapi.data.database.movie.MovieFavoriteRepository
import com.example.moviecatalougeapi.data.model.movie.ResultMovie
import com.example.moviecatalougeapi.data.model.tv.ResultTv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val movieFavoriteDao: MovieFavoriteDao = MovieFavoriteDatabase.getInstance(application).movieFavoriteDao()
    private val repository: MovieFavoriteRepository =
        MovieFavoriteRepository(
            movieFavoriteDao
        )

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private var _movies : LiveData<List<ResultMovie>> = repository.allMovieFavorites
    val movies : LiveData<List<ResultMovie>> get() = _movies

    fun deleteFavorite(data: ResultMovie) {
        scope.launch {
            repository.delete(data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
